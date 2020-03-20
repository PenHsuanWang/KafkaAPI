/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kafkasystem;

import ProducerCreator.KafkaProducer;
import ProducerCreator.Producer;
import SelfDefinedException.ProducerException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static kafkasystem.KafkaProducerSenderGUI.showException;


/**
 *
 * @author willy
 */
public class KafkaSystem {

    private File[] inputFiles;    
    
    private KafkaProducerSenderGUI producerGUI = new KafkaProducerSenderGUI();
    private Producer producerAPI;
    
    private static int sendingTimes = 1;
    private static int batchSendingInterval = 100; // milisecond

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ConstanceMessage messageSetting = ConstanceMessage.getInstance();
        
        KafkaSystem kafkaSystem = new KafkaSystem();
        
        //===============================================================//
        // Start to read the kafka producer API properties' setting file //
        //===============================================================//
        Properties kafkaProducerProps = new Properties();
        try {
            InputStream input = new FileInputStream("var\\KafkaProducer.properties");
            try {
                kafkaProducerProps.load(input);
            } catch (IOException ex) {
                showException("Producer AIP property file reading failed", messageSetting.PROPERIES_SETTING_ERROR, 2);  
            }
        } catch (FileNotFoundException ex) {
            showException("Producer AIP property file reading failed", messageSetting.PROPERIES_FILENOTFOUND, 2);
        }
        //=============================//
        // End of reading setting file //
        //=============================//
        
        //==================//
        // Initializing GUI //
        //==================//
        //KafkaProducerSenderGUI producerGUI = new KafkaProducerSenderGUI();
        kafkaSystem.producerGUI.setView(kafkaProducerProps);
        
        //=================//
        // Create Producer //
        //=================//
        kafkaSystem.producerAPI = null;
        if (kafkaSystem.producerGUI.radioButtonKafkaProducerAIP.isSelected()) {
            kafkaSystem.producerAPI = new ProducerCreator.KafkaProducer();
        } else {
            kafkaSystem.producerAPI = new ProducerCreator.WecTransport();
        }
        
        Vector<HashMap> stackTestUnit = new Vector<>();

        //==========================================================================//
        // Try to create kafka producer automatically from reading properties files //
        //==========================================================================//
        try {
            kafkaSystem.producerGUI.resetProducerPropertiesTextBox();
            kafkaSystem.producerAPI.initProperties(kafkaProducerProps);
            try{
                kafkaSystem.producerAPI.initProducer();
            } catch(ProducerException producerEx){
                kafkaSystem.producerGUI.showProducerDetails.setText(producerEx.getErrorMessage());
                kafkaSystem.producerAPI.terminatedProducer();
            }
            kafkaSystem.producerGUI.setProducerPropertiesTextBoxFromFile(kafkaProducerProps);
            kafkaSystem.producerGUI.setProducerRunningInfoFromPropertiesFile(kafkaProducerProps);
            kafkaSystem.producerGUI.performProducerAIP.setSelected(true);
        } catch (Exception ex) {
            kafkaSystem.producerGUI.resetProducerPropertiesTextBox();
            kafkaSystem.producerGUI.showProducerDetails.setText("Fail to create Producer! \n check properties setting!");
            kafkaSystem.producerGUI.performProducerAIP.setSelected(false);
            kafkaSystem.producerAPI.terminatedProducer();
        }
        //========================================================//
        // If fail to reading properties files to create producer //
        // Let the setting test area become ediable and setting   //
        // those properties manually                              //
        //========================================================//

        //==================================================//
        // Finish of initialization of Kafka System program //
        //==================================================//
        
        /*
        * There has two following action will recreate producer!
        * First: changing the properties configuration. Such as, changing broker ip, port. topic
        * Second: changing the producer type. Such change producer type to WecTransport, vise and versa.
        * Both this two action will terminate the current producer and recreate a new one!
        *
        * All the fields of the class "Producer" will be flash,  
        * Most import one will be the StackTestUnit, which is a vector for storing the result from send testing.
        */
        
        //==========================//
        // Resetting the properties //
        // Recreate the producer    //
        //==========================//
        kafkaSystem.producerGUI.setProducerPropertiesManually.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setProducerPropertiesAction(evt, kafkaSystem.producerGUI, kafkaSystem.producerAPI);
            }
        });
        
        kafkaSystem.producerGUI.radioButtonWECFramework.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                kafkaSystem.recreateProducerChangeType(evt, kafkaProducerProps);
            }
        });
        
        kafkaSystem.producerGUI.radioButtonKafkaProducerAIP.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                kafkaSystem.recreateProducerChangeType(evt, kafkaProducerProps);
            }
        });
        
        //=====================================//
        // After resetting the properties      //
        // Trigger for recreate a new producer //
        //=====================================//
        kafkaSystem.producerGUI.performProducerAIP.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                runningProducerCheckBoxAction(evt, kafkaSystem.producerGUI, kafkaSystem.producerAPI);
            }
        });
        
        kafkaSystem.producerGUI.fileChooserItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    File[] inputFiles = getFilesfromChooserAction(evt, kafkaSystem.producerGUI);
                    kafkaSystem.setFiles(inputFiles);
                } catch (ParseException ex) {
                    
                }
            }
        });
        
        kafkaSystem.producerGUI.frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.out.println("Uncomment following to open another window!");
                try {
                    java.io.FileWriter fw = new java.io.FileWriter("testOfOutputFile.txt");
                    //fw.write("Uncomment following to open another window!");
                    
                    for (HashMap stackTestUnit1 : kafkaSystem.producerAPI.getStackTestUnit()) {
                        fw.write(Double.parseDouble(stackTestUnit1.get("SerializedValueSize").toString())/(1024*1024)+"MB, spend time: ");
                        fw.write(stackTestUnit1.get("SendMesgConsumedTime")+"    ");
                        fw.write("Sending speed from producer to Kafka broker:  " + Double.parseDouble(stackTestUnit1.get("SerializedValueSize").toString())/(1024*1024)/Double.parseDouble(stackTestUnit1.get("SendMesgConsumedTime").toString())*Math.pow(10, 3)+" MB/s \n");
                    }
                    fw.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                e.getWindow().dispose();
                System.out.println("JFrame Closed!");
            }
        });
        
        kafkaSystem.producerGUI.sendMessageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    sendMessageAction(evt, kafkaSystem.producerGUI, kafkaSystem.producerAPI, kafkaSystem.getFiles());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(KafkaSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
    }
    //=====================//
    // End of main methods //
    //=====================//
    
    
    //==================//
    // Internel Methods //
    //==================//
    public void setFiles(File[] inputFiles){
        this.inputFiles = inputFiles;
    }
    
    public File[] getFiles(){
        return this.inputFiles;
    }
    
        
        
    //============================//
    // Action Methods of listener //
    //============================//
    
    public static void setProducerPropertiesAction(java.awt.event.ActionEvent evt, KafkaProducerSenderGUI producerGUI, Producer producer) {
        producerGUI.resetProducerPropertiesTextBox();
        producer.terminatedProducer();
        producerGUI.performProducerAIP.setSelected(false);
        producerGUI.showProducerDetails.setText("Stop current producer, recreate producer API!! \b");
    }
    
    public static void runningProducerCheckBoxAction(java.awt.event.ActionEvent evt, KafkaProducerSenderGUI producerGUI, ProducerCreator.Producer producer){

        Properties manuallySettingKafkaProducerProps = new Properties();
        manuallySettingKafkaProducerProps.setProperty("bootstrap.servers", producerGUI.brokerIP.getText() + ":" + producerGUI.brokerPort.getText());
        manuallySettingKafkaProducerProps.setProperty("topic.name", producerGUI.topicName.getText());

        manuallySettingKafkaProducerProps.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        manuallySettingKafkaProducerProps.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        manuallySettingKafkaProducerProps.setProperty("message.key", "SECS");
        
        try {
            //producer.terminatedProducer();
            producer.initProperties(manuallySettingKafkaProducerProps);
            try{
                producer.initProducer();
            } catch (ProducerException producerEx){
                producerGUI.showProducerDetails.setText(producerEx.getErrorMessage());
            } catch (Exception ex) {
                System.out.println("Can not create producer, please check properties setting!");
                //producerGUI.resetProducerPropertiesTextBox();
                producerGUI.performProducerAIP.setSelected(false);
                producerGUI.showProducerDetails.setText(" Try to create producer but FAILED\n Please check the properties setting! \n");
                return;
            }
            producerGUI.setProducerPropertiesTextBoxFromFile(manuallySettingKafkaProducerProps);
            producerGUI.setProducerRunningInfoFromPropertiesFile(manuallySettingKafkaProducerProps);
                //producerGUI.performProducerAIP.setSelected(true);

        } catch (Exception ex) {
            producerGUI.resetProducerPropertiesTextBox();
            producer.terminatedProducer();
            producerGUI.performProducerAIP.setSelected(false);
            producerGUI.showProducerDetails.setText("Stop current producer, recreate producer AIP!! \n");
            
        }

    }
    
    public void recreateProducerChangeType(java.awt.event.ActionEvent evt, Properties props){
        this.producerAPI.terminatedProducer();
        this.producerAPI = null;
        if(this.producerGUI.radioButtonWECFramework.isSelected()){
            this.producerAPI = (ProducerCreator.WecTransport) new ProducerCreator.WecTransport();
        } else if (this.producerGUI.radioButtonKafkaProducerAIP.isSelected()){
            this.producerAPI = (ProducerCreator.KafkaProducer)new ProducerCreator.KafkaProducer();
        }
        this.producerAPI.initProperties(props);
        try {
            this.producerAPI.initProducer();
            System.out.println(this.producerAPI.getProducerType());
        } catch (ProducerException ex) {
            this.producerGUI.showProducerDetails.setText(ex.getErrorMessage());
        }
    }
    
    public static File[] getFilesfromChooserAction(java.awt.event.ActionEvent evt, KafkaProducerSenderGUI producerGUI) throws ParseException {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);

        int returnValue = fc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File[] inFile = fc.getSelectedFiles();
            String showInFilePath = "";
            for (int i = 0; i < inFile.length; i++) {
                showInFilePath = showInFilePath + inFile[i].getAbsolutePath() + "\n";
            }
            producerGUI.showFileInput.setText(showInFilePath);
            return inFile;
        }
        return null;
    }
    
   
    public static void sendMessageAction(java.awt.event.ActionEvent evt, KafkaProducerSenderGUI producerGUI, ProducerCreator.Producer testProducer, File[] readFiles) throws FileNotFoundException {
        if (readFiles == null) {
            showException("Selected Files are null", "No files are selected!! \nPlease select file for sending first!!", 2);
        }
        int iCount = 0;
        try {
            while (iCount < KafkaSystem.sendingTimes) {
                for (int i = 0; i < readFiles.length; i++) {
                    testProducer.send(readFiles[i]);
                }
                producerGUI.showSendMessageReply.setText(testProducer.getSendingInfo());
                iCount++;
                try {
                    Thread.sleep(KafkaSystem.batchSendingInterval);
                } catch (InterruptedException ex) {
                    Logger.getLogger(KafkaSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(KafkaProducer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            //Logger.getLogger(KafkaProducer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Sending message Timeout! please check the status of broker which producer sending, and check ip/port setting is correct!");
            JOptionPane.showMessageDialog(null, ex, "Sending Timeuut", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(KafkaProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
