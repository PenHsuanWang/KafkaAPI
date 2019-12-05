/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kafkasystem;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import KafkaConsoleAPI.KafkaProducerCreator;

/**
 *
 * @author willy
 */
public class KafkaSystem {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        KafkaProducerCreator testProducer = new KafkaProducerCreator();
        testProducer.initKafkaProducer();
        try {
            testProducer.sendMessage();
        } catch (InterruptedException ex) {
            Logger.getLogger(KafkaSystem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(KafkaSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
