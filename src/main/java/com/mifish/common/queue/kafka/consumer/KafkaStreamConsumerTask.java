package com.mifish.common.queue.kafka.consumer;

import com.mifish.common.queue.MessageHandler;
import com.mifish.common.queue.model.MessageStatus;
import com.mifish.common.queue.model.QueueMessage;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Description:
 * <p>
 * copy and modify KafkaQueueConsumer
 *
 * @author: rls
 * Date: 2017-12-05 18:05
 */
public class KafkaStreamConsumerTask implements Runnable {

    /***group*/
    private String group;

    /***topic*/
    private String topic;

    /***kafkaStream*/
    private KafkaStream<String, String> kafkaStream;

    /***messageHandler*/
    private MessageHandler messageHandler;

    /***executor*/
    private ThreadPoolExecutor executor;

    /***表示该任务是否已经结束*/
    private volatile boolean done = false;

    /***stopRead*/
    private volatile boolean stopRead = false;

    /**
     * KafkaStreamConsumerTask
     *
     * @param group
     * @param topic
     * @param kafkaStream
     * @param messageHandler
     * @param executor
     */
    public KafkaStreamConsumerTask(String group, String topic, KafkaStream<String, String> kafkaStream,
                                   MessageHandler messageHandler, ThreadPoolExecutor executor) {
        this.group = group;
        this.topic = topic;
        this.kafkaStream = kafkaStream;
        this.messageHandler = messageHandler;
        this.executor = executor;
    }

    @Override
    public void run() {
        try {
            this.done = false;
            consume();
        } catch (Exception e) {
            StringBuilder err = new StringBuilder();
            err.append("group:").append(this.group);
            err.append("topic:").append(topic);
            err.append(" clientId:").append(kafkaStream.clientId());
        }
        this.done = true;
    }

    /**
     * doConsume
     * 真实消费kafka队列中的消息
     *
     * @return void
     */
    private void consume() {
        ConsumerIterator<String, String> itr = this.kafkaStream.iterator();
        while (itr.hasNext() && !this.stopRead) {
            MessageAndMetadata<String, String> metadata = itr.next();
            final String msgTopic = metadata.topic();
            final String key = StringUtils.replace(metadata.key(), "\r|\n", "");
            final String msg = StringUtils.replace(metadata.message(), "\r\n", "");
            final int partition = metadata.partition();
            final long offset = metadata.offset();
            final long startTime = System.currentTimeMillis();
            Callable<MessageStatus> task = () -> {
                try {
                    QueueMessage message = new QueueMessage(group, msgTopic, partition, msg);
                    message.addAttribute(QueueMessage.MSG_KEY, key);
                    message.addAttribute(QueueMessage.CLIENT_ID, kafkaStream.clientId());
                    message.addAttribute(QueueMessage.OFFSET, offset);
                    MessageStatus msgStatus = messageHandler.handle(message);
                    return msgStatus;
                } catch (Throwable t) {
                    //因为ThreadPoolExecutor线程池内部会try掉throwable,但是确没有任何错误信息输出
                    //所以这边需要try throwable,不然如果出现Error的时候(比如jar冲突之类的导致类初始化失败)无法知道具体是啥原因
                    StringBuilder err = new StringBuilder();
                    err.append("group:").append(group).append(",");
                    err.append("topic:").append(msgTopic).append(",");
                    err.append("clientId:").append(kafkaStream.clientId()).append(",");
                    err.append("partition:").append(partition).append(",");
                    err.append("key:").append(key).append(",");
                    err.append("offset:").append(offset).append(",");
                    err.append("msg:").append(msg).append("");
//                    LogUtils.error("ConsumeKafkaMessageFail", err.toString(), t);
//                    MessageLogger.logFailProc(topic, msg);
                    //保留原代码的逻辑，暂时不清楚这边有哪些坑waiting for me
                    if (t instanceof Error) {
                        throw (Error) t;
                    }
                    return MessageStatus.RETRY;
                }
            };
            while (true) {
                try {
                    executor.submit(task);
                    break;
                } catch (RejectedExecutionException e) {
                    //忽略,一直循环重试直到提交成功
                    //此处不要sleep,直接不断提交任务即可,不然会造成线程池里的线程没有充分利用上,但是消息又在等待处理
                    //this.sleep(this.submitRetryInterval);
                }
            }
        }
    }

    /**
     * 判断该任务是否已经结束
     *
     * @return
     */
    public boolean isDone() {
        return this.done;
    }

    /**
     * stopRead
     * 停止读消息，但是，已经读好的消息，则继续在业务线程池中进行处理，当前线程池可以退出了
     *
     * @return void
     */
    public synchronized void stopRead() {
        this.stopRead = true;
    }

    /**
     * getGroup
     *
     * @return
     */
    public String getGroup() {
        return group;
    }

    /**
     * getTopic
     *
     * @return
     */
    public String getTopic() {
        return topic;
    }

    /**
     * getMessageHandler
     *
     * @return
     */
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}
