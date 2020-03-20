/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProducerCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 *
 * @author willy
 */
public class KafkaProducer extends Producer {

    private static org.apache.kafka.clients.producer.KafkaProducer producer;
    
    RecordMetadata replyRecord;

    public KafkaProducer() {
        
    }

    private class ProducerCallback implements Callback {

        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {

            System.out.println("Performed call back!");
            if (e != null) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initProducer() {
        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(this.props);
    }

    @Override
    public void terminatedProducer() {
        try{
            this.producer.flush();
            this.producer.close();
        } catch(Exception IExp){
            System.out.println("Closing producer failed");
        }
        this.producer = null;
    }

    @Override
    public void send(File inputFile) throws InterruptedException, ExecutionException, Exception {
        HashMap testUnitReturnInfo = new HashMap();

        StringBuilder sb = new StringBuilder();
        try {
            sb = readStringFromInFile(inputFile);
        } catch (FileNotFoundException fEx) {

        }

        String messageToSend = sb.toString();
            long startSendTimestamp = System.currentTimeMillis();
            this.replyRecord = sendMessage(messageToSend);
            long finalSendTimestamp = System.currentTimeMillis();
            if (this.replyRecord != null) {

                testUnitReturnInfo.put("SendingAPI", "Apache Producer API");
                testUnitReturnInfo.put("TestFile", inputFile.getPath());
                testUnitReturnInfo.put("Timestamp", replyRecord.timestamp());
                testUnitReturnInfo.put("Topic", replyRecord.topic());
                testUnitReturnInfo.put("Offset", replyRecord.offset());
                testUnitReturnInfo.put("SerializedValueSize", replyRecord.serializedValueSize());
                testUnitReturnInfo.put("SendMesgConsumedTime", finalSendTimestamp - startSendTimestamp);

                this.stackTestUnit.add(testUnitReturnInfo);
            } else {
            }
    }
    
    @Override
    public String getProducerType(){
        return "Apache Producer API";
    }
    
    @Override
    public String getSendingInfo() {
        String sendingInfo = "";
        for (int i = 0; i < this.stackTestUnit.size(); i++) {
            sendingInfo = sendingInfo + "Going to send message from file: " + this.stackTestUnit.get(i).get("TestFile") + " : \n";
            sendingInfo = sendingInfo + "    Sending by: " + this.stackTestUnit.get(i).get("SendingAPI") + "\n";
            sendingInfo = sendingInfo + "    Timestamp: " + this.stackTestUnit.get(i).get("Timestamp") + "\n";
            sendingInfo = sendingInfo + "    Topic: " + this.stackTestUnit.get(i).get("Topic") + "\n";
            sendingInfo = sendingInfo + "    Offset: " + this.stackTestUnit.get(i).get("Offset") + "\n";
            sendingInfo = sendingInfo + "    Serialized Value Size: " + this.stackTestUnit.get(i).get("SerializedValueSize") + "\n";
            sendingInfo = sendingInfo + "Send message time consumed: " + this.stackTestUnit.get(i).get("SendMesgConsumedTime") + " miliseconds \n\n";
        }
        return sendingInfo;
    }


    public RecordMetadata sendMessage(String message) throws InterruptedException, ExecutionException, Exception {
        
        System.out.println(this.sendKey);
        System.out.println(this.sendTopic);
        if (this.sendKey == null && this.sendTopic == null) {
            throw new Exception();
        }
        ProducerRecord<String, String> recordSynchronously
                = new ProducerRecord<>(this.sendTopic, this.sendKey, message);

        //====================================================================//
        // Going to send the message,                                         //
        // The .get() will throws InterruptedException and ExecutionException //
        // while the sending message to kafka failed                          //
        //====================================================================//
        
        RecordMetadata computedResult = (RecordMetadata) this.producer.send(recordSynchronously).get();

        if (computedResult != null) {
            return computedResult;
        } else {
            return null;
        }
    }

}
