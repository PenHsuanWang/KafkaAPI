/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KafkaConsoleAPI;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.ProducerFencedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;


/**
 *
 * @author willy
 */
public class KafkaProducerCreator{
    
    private static KafkaProducer producer;
    
    
    public KafkaProducerCreator(){
        
    }
    
    private class ProducerCallback implements Callback{
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e){
            
            System.out.println("HiHi");
            
            if (e != null){
                e.printStackTrace();
            }
        }
    }
    
    
    public void initKafkaProducer(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.1.39:9092");
        props.put("retry.backoff.ms", "10");
        props.put("retries", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<String, String>(props);
    }
    
    public void sendMessage() throws InterruptedException, ExecutionException {
        ProducerRecord<String, String> recordSynchronously
                = new ProducerRecord<>("testTopicAAA", "Precision Products", "FranceBBB");
        ProducerRecord<String, String> recordAsynchronously
                = new ProducerRecord<>("testTopicAAA", "Biomedical Meterials", "USA");

        System.out.println("Going to sent message");
        //====================================================================//
        // Going to send the message,                                         //
        // The .get() will throws InterruptedException and ExecutionException //
        // while the sending message to kafka failed                          //
        //====================================================================//
        producer.send(recordSynchronously, new ProducerCallback()).get();

        String aaa = this.producer.send(recordSynchronously).get().toString();
        if (aaa != null) {
            System.out.println("Message send and get: " + aaa);
        } else {
            System.out.println("Broker Not Reply!");
        }

    }
    
    
   
}
