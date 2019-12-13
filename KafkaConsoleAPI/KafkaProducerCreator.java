/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KafkaConsoleAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

            System.out.println("Performed call back!");
            if (e != null){
                e.printStackTrace();
            }
        }
    }


    public void initKafkaProducer(Properties props) throws Exception{
        this.producer = new KafkaProducer<String, String>(props);
    }

    
    public RecordMetadata sendMessage(String topic, String key, String message) throws InterruptedException, ExecutionException {
        
        ProducerRecord<String, String> recordSynchronously
                = new ProducerRecord<>(topic, key, message);
        String returnInformation = "";
        
        //====================================================================//
        // Going to send the message,                                         //
        // The .get() will throws InterruptedException and ExecutionException //
        // while the sending message to kafka failed                          //
        //====================================================================//
        //System.out.println(producer.send(recordSynchronously, new ProducerCallback()).get());

        RecordMetadata computedResult = (RecordMetadata) this.producer.send(recordSynchronously).get();
        
        if (computedResult != null) {
            return computedResult;
        } else {
            return null;
        }
    }
}