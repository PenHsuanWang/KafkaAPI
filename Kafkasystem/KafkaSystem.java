/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kafkasystem;

import KafkaConsoleAPI.KafkaProducerCreator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author willy
 */
public class KafkaSystem {

    private File[] inputFiles;
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        KafkaProducerCreator testProducer = new KafkaProducerCreator();
        KafkaProducerSenderGUI producerGUI = new KafkaProducerSenderGUI();
        
        KafkaSystem kafkaSystem = new KafkaSystem();
        
        //=======================//
        // Create Kafka producer //
        //=======================//
        testProducer.initKafkaProducer();
        
        
        producerGUI.setView();
        
        
        
        producerGUI.fileChooserItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    producerGUI.getFilesfromChooserAction(evt);
                } catch (ParseException ex) {
                    
                }
            }
        });
        
        
        
        //================//
        // Read the files //
        //================//
        try (BufferedReader br = new BufferedReader(new FileReader("Z:\\WEC_testOracleVersionPerformance\\SECS\\6933FD200.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } catch (IOException ex) {
                Logger.getLogger(KafkaSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (sb != null) {
                try {
                    testProducer.sendMessage("testTopicAAA", "SECS", sb.toString());
                } catch (InterruptedException ex) {
                    Logger.getLogger(KafkaSystem.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(KafkaSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(KafkaSystem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioe){
            
        }


    }
    
    
    public void setFiles(File[] inputFiles){
        this.inputFiles = inputFiles;
    }
    
    public File[] getFiles(){
        return this.inputFiles;
    }

    /*
    public void getFilesfromChooserAction(java.awt.event.ActionEvent evt) throws ParseException {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);

        int returnValue = fc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File[] inFile = fc.getSelectedFiles();
            setFiles(inFile);
        }
    }
    */

}