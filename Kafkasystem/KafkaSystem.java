/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kafkasystem;

import KafkaConsoleAPI.KafkaProducerCreator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author willy
 */
public class KafkaSystem {

    private File[] inputFiles;
    private Object ooo = "1234";
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
       
        Properties kafkaProducerProps = new Properties();
        try {
            InputStream input = new FileInputStream("var\\KafkaProducer.properties");
            
            try {
                kafkaProducerProps.load(input);
            } catch (IOException ex) {
                Logger.getLogger(KafkaProducerCreator.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(KafkaProducerCreator.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        KafkaProducerCreator testProducer = new KafkaProducerCreator();
        KafkaProducerSenderGUI producerGUI = new KafkaProducerSenderGUI();
        
        KafkaSystem kafkaSystem = new KafkaSystem();
        
        //=======================//
        // Create Kafka producer //
        //=======================//
        testProducer.initKafkaProducer(kafkaProducerProps);
        
        producerGUI.brokerIP.setEditable(false);
        String ipPort = kafkaProducerProps.getProperty("bootstrap.servers");
        producerGUI.brokerIP.setText(ipPort.substring(0, ipPort.indexOf(":")));
        producerGUI.brokerPort.setEditable(false);
        producerGUI.brokerPort.setText(ipPort.substring(ipPort.indexOf(":")+1, ipPort.length()));
        producerGUI.topicName.setEditable(false);
        producerGUI.topicName.setText(kafkaProducerProps.getProperty("topic.name"));
        
        producerGUI.setView();

        producerGUI.fileChooserItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    File[] inputFiles = producerGUI.getFilesfromChooserAction(evt);
                    kafkaSystem.setFiles(inputFiles);
                } catch (ParseException ex) {
                    
                }
            }
        });
        
        producerGUI.frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.out.println("Uncomment following to open another window!");
                try {
                    java.io.FileWriter fw = new java.io.FileWriter("testOfOutputFile.txt");
                    fw.write("Uncomment following to open another window!");
                    fw.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                e.getWindow().dispose();
                System.out.println("JFrame Closed!");
            }
        });
        
        producerGUI.sendMessageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    producerGUI.sendMessageAction(evt, testProducer, kafkaProducerProps, kafkaSystem.getFiles());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(KafkaSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    
    public void setFiles(File[] inputFiles){
        this.inputFiles = inputFiles;
    }
    
    public File[] getFiles(){
        return this.inputFiles;
    }

}