package com.mifish.common.queue.kafka.consumer;

import com.mifish.common.queue.MessageHandler;
import com.mifish.common.queue.QueueConsumer;
import com.mifish.common.queue.model.ConsumerStatus;
import com.mifish.common.queue.model.TopicPathMeta;
import com.mifish.common.util.ThreadUtil;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-05 18:46
 */
public class MutiKafkaQueueConsumer implements QueueConsumer {

    /***logger*/
    private static final Logger logger = LoggerFactory.getLogger(MutiKafkaQueueConsumer.class);

    /***status*/
    private volatile ConsumerStatus status = ConsumerStatus.STOPPED;

    /***lock*/
    private Lock lock = new ReentrantLock();

    /***consumerConnector*/
    private volatile ConsumerConnector consumerConnector;

    /***messageHandler*/
    private MessageHandler messageHandler;

    /***zk*/
    private String zk;

    /***group*/
    private String group;

    /***topicPathMetas*/
    private Map<String, TopicPathMeta> topicPathMetas;

    /***consumerConfigs*/
    private Map<String, String> consumerConfigs = new HashMap<>();

    /***featherThreadPools*/
    private Map<String, ThreadPoolExecutor> featherThreadPools = new HashMap<>();

    /***consumeThreadPools*/
    private Map<String, ThreadPoolExecutor> consumeThreadPools = new HashMap<>();

    /***kafkaStreamConsumerTasks*/
    private List<KafkaStreamConsumerTask> kafkaStreamConsumerTasks = new ArrayList<>();

    /***charset*/
    private String charset = "utf-8";

    /**
     * MutiKafkaQueueConsumer
     *
     * @param zk
     * @param group
     * @param topicPath
     * @param messageHandler
     */
    public MutiKafkaQueueConsumer(String zk, String group, String topicPath, MessageHandler messageHandler) {
        this(zk, group, topicPath, messageHandler, null);
    }

    /**
     * MutiKafkaQueueConsumer
     * <p>
     * topicPath的格式：topic的配置信息
     * 支持多个topic，topic与topic之间以";"相隔
     * 单个topic的配置信息：topic,kafkaStreamSize,executorCorePoolSize,executorMaxPoolSize,executorQueueSize，中间以","分割
     * 其中，topic必须配置，其他参数选择性顺序配置，注意：第一：顺序；第二：假如没有配置，则使用默认值。
     *
     * @param zk
     * @param group
     * @param topicPath
     * @param messageHandler
     * @param consumerConfigs
     */
    public MutiKafkaQueueConsumer(String zk, String group, String topicPath, MessageHandler messageHandler,
                                  Map<String, String> consumerConfigs) {
        checkArgument(StringUtils.isNotBlank(zk), "zk cannot be blank in MutiKafkaQueueConsumer");
        checkArgument(StringUtils.isNotBlank(group), "group cannot be blank in MutiKafkaQueueConsumer");
        checkArgument(StringUtils.isNotBlank(topicPath), "topicPath cannot be blank in MutiKafkaQueueConsumer");
        checkArgument((messageHandler != null), "messageHandler cannot be null in MutiKafkaQueueConsumer");
        this.zk = zk;
        this.group = group;
        this.messageHandler = messageHandler;
        this.topicPathMetas = TopicPathMeta.parse2Map(topicPath);
        if (consumerConfigs != null) {
            this.consumerConfigs.putAll(consumerConfigs);
        }
    }


    /**
     * 启动一个队列消费者
     *
     * @return void
     */
    @Override
    public void start() {
        //只有已经停止了的，才能启动
        if (isStopped()) {
            try {
                lock.lock();
                if (isStopped()) {
                    //启动和停止公用一个锁,如果前一个stop操作还没完成则会一直等待不会冲突
                    this.status = ConsumerStatus.STARTING;
                    if (logger.isInfoEnabled()) {
                        logger.info("StartQueueConsumer ready to start,current status of the {} consumer :{}", this
                                .group, this.status);
                    }
                    this.initKafkaStreamConsumer();
                    this.status = ConsumerStatus.RUNNING;
                    if (logger.isInfoEnabled()) {
                        logger.info("StartQueueConsumer start the {} consumer successfully", this.group);
                    }
                }
            } catch (Throwable t) {
                logger.error("StartQueueConsumer start the {} consumer failed.", this.group, t);
                //停止该启动器
                this.asyncStop();
                //注意：注意：当一个消费者再次重启时，重启不成功，则跑出异常，而不是让这个消费者进程继续run下去
                throw t;
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * initKafkaStreamConsumer
     *
     * @return void
     */
    private void initKafkaStreamConsumer() {
        this.consumerConnector = KafkaConsumerConnectorBuilder.build(zk, group, consumerConfigs);
        Map<String, Integer> topicCountMap = new HashMap<>(topicPathMetas.size());
        for (TopicPathMeta topicPathMeta : topicPathMetas.values()) {
            topicCountMap.put(topicPathMeta.getTopic(), topicPathMeta.getKafkaStreamSize());
        }
        //注意：
        Map<String, List<KafkaStream<String, String>>> messageStreams = this.consumerConnector.createMessageStreams
                (topicCountMap,
                        KafkaConsumerConnectorBuilder.buildStringDecoder(this.charset), KafkaConsumerConnectorBuilder
                                .buildStringDecoder(this.charset));
        for (Map.Entry<String, List<KafkaStream<String, String>>> entry : messageStreams.entrySet()) {
            String topic = entry.getKey();
            List<KafkaStream<String, String>> streams = entry.getValue();
            ThreadPoolExecutor featherPool = new ThreadPoolExecutor(topicCountMap.get(topic), topicCountMap.get(topic),
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());
            this.featherThreadPools.put(topic, featherPool);
            ThreadPoolExecutor consumeThreadPool = initThreadPoolExecutor(topic);
            for (int i = 0; i < streams.size(); i++) {
                KafkaStreamConsumerTask streamConsumerTask = new KafkaStreamConsumerTask(this.group, topic, streams.get
                        (i), this.messageHandler, consumeThreadPool);
                this.kafkaStreamConsumerTasks.add(streamConsumerTask);
                featherPool.submit(streamConsumerTask);
            }
            if (logger.isInfoEnabled()) {
                logger.info("StartQueueConsumer {} Started {} consumer kafkaStream for the topic {}", this.group,
                        streams.size(), topic);
            }
        }
    }

    /**
     * initThreadPoolExecutor
     * <p>
     * 一个topic一个线程池
     * 假如topic不存在，则直接抛出异常
     *
     * @param topic
     * @return
     */
    private ThreadPoolExecutor initThreadPoolExecutor(String topic) {
        if (this.topicPathMetas.containsKey(topic)) {
            TopicPathMeta topicPathMeta = this.topicPathMetas.get(topic);
            ThreadPoolExecutor executor = new ThreadPoolExecutor(topicPathMeta.getExecutorCorePoolSize(),
                    topicPathMeta.getExecutorMaxPoolSize(), 1000L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(topicPathMeta.getExecutorQueueSize()), new ThreadPoolExecutor
                    .AbortPolicy());
            this.consumeThreadPools.put(topic, executor);
            if (logger.isInfoEnabled()) {
                logger.info("StartQueueConsumer Started executor:{},corePoolSize:{},maximumPoolSize{}," +
                                "keepAliveTime:1000ms,executorQueueSize:{}", topic, topicPathMeta
                                .getExecutorCorePoolSize(),
                        topicPathMeta.getExecutorMaxPoolSize(), topicPathMeta.getExecutorQueueSize());
            }
            return executor;
        }
        throw new RuntimeException("cannot initThreadPoolExecutor when topic:" + topic + " is not exist!please " +
                "checkout the property config.");
    }

    /**
     * 给处理机脚本定制的接口
     */
    public void asyncStop() {
        this.stop(true);
    }

    /**
     * syncStop
     * 同步停止
     */
    public void syncStop() {
        this.stop(false);
    }

    /**
     * stop
     *
     * @param async
     */
    @Override
    public void stop(boolean async) {
        if (isStopped() || isStopping()) {
            //停止中或者已停止直接忽略
            return;
        }
        try {
            lock.lock();
            if (isStarting() || isRunning()) {
                this.status = ConsumerStatus.STOPPING;
                new Thread(() -> {
                    stopReadKafka();
                    shutdownExecutor();
                    status = ConsumerStatus.STOPPED;
                    if (logger.isInfoEnabled()) {
                        logger.info("StopQueueConsumer the group {} consumer stoped.", group);
                    }
                }).start();
                if (logger.isInfoEnabled()) {
                    logger.info("StopQueueConsumer Stoping the topic {} consumer.", group);
                }
                if (!async) {
                    //同步等待
                    while (!isStopped()) {
                        ThreadUtil.sleep(200);
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 判断是否还有未处理完的任务
     *
     * @return
     */
    private boolean hasUnfinishedTasks() {
        if (this.featherThreadPools == null || this.featherThreadPools.isEmpty() || this.consumeThreadPools == null
                || this.consumeThreadPools.isEmpty()) {
            return false;
        }
        for (ExecutorService executorService : this.featherThreadPools.values()) {
            if (!executorService.isTerminated()) {
                return true;
            }
        }
        //关闭任务消费的线程池
        for (ExecutorService executorService : this.consumeThreadPools.values()) {
            if (!executorService.isTerminated()) {
                return true;
            }
        }
        return false;
    }

    /**
     * shutdownExecutor
     *
     * @return void
     */
    private void shutdownExecutor() {
        // 关闭读取kafka队列的线程池
        for (ExecutorService executorService : featherThreadPools.values()) {
            shutdownExecutorWithoutException(executorService);
        }
        if (logger.isInfoEnabled()) {
            logger.info("StopQueueConsumer group {} shutdown feather thread pools success", group);
        }
        //关闭任务消费的线程池
        for (ExecutorService executorService : consumeThreadPools.values()) {
            shutdownExecutorWithoutException(executorService);
        }
        if (logger.isInfoEnabled()) {
            logger.info("StopQueueConsumer group {} shutdown consume thread pools success", group);
        }
        while (hasUnfinishedTasks()) {
            // 等待工作线程池所有任务都处理完成
            ThreadUtil.sleep(200);
        }
        this.featherThreadPools.clear();
        this.consumeThreadPools.clear();
        if (logger.isInfoEnabled()) {
            logger.info("StopQueueConsumer group {} consumer clear all thread pools success", group);
        }
    }

    /**
     * shutdownExecutorWithoutException
     * <p>
     * 忽略异常
     *
     * @param executorService
     */
    private void shutdownExecutorWithoutException(ExecutorService executorService) {
        try {
            if (executorService != null) {
                executorService.shutdown();
            }
        } catch (Exception ex) {
            //ingore
        }
    }

    /**
     * 停止读kafka
     *
     * @return
     */
    private void stopReadKafka() {
        //读kafka stream.next()的时候会在内存中设置offset,先停止读kafka stream
        //都停止完成后最后再提交offset到zk
        for (KafkaStreamConsumerTask streamConsumerTask : this.kafkaStreamConsumerTasks) {
            //只能设置状态让线程内部自己停止读
            //不能强行中断线程,不然会导致可能刚好自动提交offset到zk了
            // 但是读出来的消息还没写到blocking queue导致丢消息了
            streamConsumerTask.stopRead();
        }
        if (logger.isInfoEnabled()) {
            logger.info("StopQueueConsumer group {} stop read kafka stream success", group);
        }
        if (consumerConnector != null) {
            //Shut down the connector
            this.consumerConnector.shutdown();
            if (logger.isInfoEnabled()) {
                logger.info("StopQueueConsumer group {} shutdown consumerConnector success", group);
            }
            while (true) {
                if (this.isAllStreamConsumerEnd()) {
                    // 提交offset,避免消息提交到blocking queue且处理成功了
                    // 但是关闭进程的时候定时的线程还没有成功提交offset到zk,导致下次要重复处理
                    // 提交到blocking queue成功就当成消息消费成功了
                    consumerConnector.commitOffsets(true);
                    break;
                } else {
                    ThreadUtil.sleep(50);
                }
            }
            //clear
            this.kafkaStreamConsumerTasks.clear();
            this.kafkaStreamConsumerTasks = null;
            if (logger.isInfoEnabled()) {
                logger.info("StopQueueConsumer group {} kafkaStreamConsumerTasks clear all success", group);
            }
        }
    }

    /**
     * 判断所有读取kafka stream的任务是否都已停止
     *
     * @return
     */
    private boolean isAllStreamConsumerEnd() {
        if (this.kafkaStreamConsumerTasks == null || this.kafkaStreamConsumerTasks.isEmpty()) {
            return true;
        }
        for (KafkaStreamConsumerTask streamConsumer : this.kafkaStreamConsumerTasks) {
            if (!streamConsumer.isDone()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 供ConsoleServer调用,用于观察运行状况
     *
     * @return
     */
    @Override
    public String status() {
        StringBuilder msg = new StringBuilder();
        msg.append("TopicPathMetas:").append(this.topicPathMetas.values()).append(",");
        msg.append("IsAllStreamConsumerEnd:").append(isAllStreamConsumerEnd()).append(",");
        msg.append("HasUnfinishedTasks:").append(hasUnfinishedTasks()).append(",");
        msg.append("FeatherThreadPools:").append(buildThreadPoolMonitorInfo(this.featherThreadPools)).append(",");
        msg.append("ConsumeThreadPools:").append(buildThreadPoolMonitorInfo(this.consumeThreadPools)).append(",");
        msg.append("ConsumerStatus:").append(getConsumerStatus()).append(",");
        msg.append("Consuming:").append(!isStopped());
        return msg.toString();
    }

    /**
     * 构建监控信息
     *
     * @param threadPoolExecutors
     * @return
     */
    private String buildThreadPoolMonitorInfo(Map<String, ThreadPoolExecutor> threadPoolExecutors) {
        if (threadPoolExecutors == null || threadPoolExecutors.isEmpty()) {
            return "[]";
        }
        StringBuilder result = new StringBuilder("[");
        for (Iterator<Map.Entry<String, ThreadPoolExecutor>> itr = threadPoolExecutors.entrySet().iterator(); itr
                .hasNext(); ) {
            Map.Entry<String, ThreadPoolExecutor> entry = itr.next();
            ThreadPoolExecutor executor = entry.getValue();
            result.append("{").append(entry.getKey()).append(",");
            result.append(executor.isTerminating()).append(",");
            result.append(executor.isTerminated()).append(",");
            result.append(executor.isShutdown()).append(",");
            result.append(executor.getActiveCount()).append(",");
            result.append(executor.getQueue().size()).append(",");
            result.append(executor.getTaskCount()).append("}");
            if (itr.hasNext()) {
                result.append(",");
            }
        }
        result.append("]");
        return result.toString();
    }

    /**
     * getConsumerStatus
     *
     * @return
     */
    @Override
    public ConsumerStatus getConsumerStatus() {
        return this.status;
    }

    /**
     * isStopped
     *
     * @return
     */
    @Override
    public boolean isStopped() {
        return ConsumerStatus.STOPPED == this.status;
    }

    /**
     * isStopping
     *
     * @return
     */
    @Override
    public boolean isStopping() {
        return ConsumerStatus.STOPPING == this.status;
    }

    /**
     * isRunning
     *
     * @return
     */
    @Override
    public boolean isRunning() {
        return ConsumerStatus.RUNNING == this.status;
    }

    /**
     * isStarting
     *
     * @return
     */
    @Override
    public boolean isStarting() {
        return ConsumerStatus.STARTING == this.status;
    }

    /**
     * setCharset
     *
     * @param charset
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }
}
