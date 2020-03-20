/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ProducerCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kafkasystem.KafkaSystem;

/**
 *
 * @author willy
 */
public abstract class Producer {
    
    protected Properties props;
    
    protected String sendKey = "";
    protected String sendTopic = "";
    
    Vector<HashMap> stackTestUnit = new Vector<>();
    
    protected String sendingInfo = "";
    
    
    //=====================================//
    // Section of defining general methods //
    //=====================================//
    
    //=========================//
    // Setting properties file //
    //=========================//
    public void initProperties(Properties props){
        this.props = props;
        this.sendKey = props.getProperty("message.key");
        this.sendTopic = props.getProperty("topic.name");
    }
    
    public Vector<HashMap> getStackTestUnit(){
        return this.stackTestUnit;
    }
    
    protected static StringBuilder readStringFromInFile(File f) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(KafkaSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb;
    }
    
    //===================================//
    // Finish of defined general methods //
    //===================================//
    
    //=======================================//
    // Start of defining the abstract method //
    //=======================================//
    
    //=======================//
    // Initializing producer //
    //=======================//
    public abstract void initProducer() throws SelfDefinedException.ProducerException;
    
    //======================//
    // Destory the producer //
    //======================//
    public abstract void terminatedProducer();
    
    //=====================================================//
    // Send message                                        //
    // The Sending way is dufferent in two different cases //
    //=====================================================//
    public abstract void send(File inputFile) throws InterruptedException, ExecutionException, Exception; 
    
    public abstract String getProducerType();
    
    public abstract String getSendingInfo();
    
    
}


