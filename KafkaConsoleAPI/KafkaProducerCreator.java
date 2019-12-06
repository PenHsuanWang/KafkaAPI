/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KafkaConsoleAPI;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;


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

    public void sendMessage(String topic, String key, String message) throws InterruptedException, ExecutionException {
        
        ProducerRecord<String, String> recordSynchronously
                = new ProducerRecord<>(topic, key, message);

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