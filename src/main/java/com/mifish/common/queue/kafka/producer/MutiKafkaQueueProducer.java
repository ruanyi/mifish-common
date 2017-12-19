package com.mifish.common.queue.kafka.producer;

import com.mifish.common.queue.QueueProducer;
import com.mifish.common.queue.SendCallback;
import com.mifish.common.queue.model.SendResult;
import com.mifish.common.util.ThreadUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-19 22:16
 */
public class MutiKafkaQueueProducer implements QueueProducer {

    private org.apache.kafka.clients.producer.KafkaProducer[] kafkaProducers;

    private String brokers;

    private AtomicInteger counter = new AtomicInteger();

    private int retryCount = 2;

    private int retryIntervalMillis = 20;

    private long sendTimeout = 2000;

    private int poolSize;

    private Map<String, String> producerConfigs;

    @Override
    public void start() {
        kafkaProducers = new org.apache.kafka.clients.producer.KafkaProducer[poolSize];
        for (int i = 0; i < poolSize; i++) {
            Properties props = new Properties();
            props.put("bootstrap.servers", this.brokers);
            props.put("acks", "1");
            if (this.producerConfigs != null) {
                for (String key : this.producerConfigs.keySet()) {
                    props.put(key, this.producerConfigs.get(key));
                }
            }
            kafkaProducers[i] = new org.apache.kafka.clients.producer.KafkaProducer<>(props, new StringSerializer(),
                    new StringSerializer());
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public SendResult send(String topic, Object value) {
        if (StringUtils.isBlank(topic)) {
            throw new IllegalArgumentException("topic can't be blank");
        }
        for (int i = 1; i <= retryCount; i++) {
            try {
                org.apache.kafka.clients.producer.KafkaProducer kafkaProducer = kafkaProducers[counter
                        .getAndIncrement() % kafkaProducers.length];
                String msgId = UUID.randomUUID().toString();
                ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(topic, msgId, value);
                Future<RecordMetadata> task = kafkaProducer.send(producerRecord);
                RecordMetadata recordMetadata = null;
                if (this.sendTimeout > 0) {
                    recordMetadata = task.get(this.sendTimeout, TimeUnit.MILLISECONDS);
                } else {
                    recordMetadata = task.get();
                }
                SendResult sendResult = new SendResult();
                sendResult.setSuccess(true);
                sendResult.setMsgId(msgId);
                sendResult.setOffset(recordMetadata.offset());
                return sendResult;
            } catch (Exception e) {
                if (i < retryCount) {
                    ThreadUtil.sleep(this.retryIntervalMillis);
                    continue;
                }
                SendResult failureResult = new SendResult();
                failureResult.setSuccess(false);
                return failureResult;
            }
        }
        SendResult failureResult = new SendResult();
        failureResult.setSuccess(false);
        return failureResult;
    }

    /**
     * asyncSend
     *
     * @param topic
     * @param value
     * @param callback
     */
    @Override
    public void asyncSend(String topic, Object value, SendCallback callback) {
        if (StringUtils.isBlank(topic)) {
            throw new IllegalArgumentException("topic can't be blank");
        }
        for (int i = 1; i <= retryCount; i++) {
            try {
                org.apache.kafka.clients.producer.KafkaProducer kafkaProducer = kafkaProducers[counter
                        .getAndIncrement() % kafkaProducers.length];
                String msgId = UUID.randomUUID().toString();
                ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(topic, msgId, value);
                Future<RecordMetadata> task = kafkaProducer.send(producerRecord, (r, e) -> {
                    if (e != null) {
                        callback.onException(e);
                    } else {
                        SendResult result = new SendResult();
                        result.setSuccess(true);
                        callback.onSuccess(result);
                    }
                });
            } catch (Exception e) {
                if (i < retryCount) {
                    ThreadUtil.sleep(this.retryIntervalMillis);
                    continue;
                }
            }
        }
    }
}
