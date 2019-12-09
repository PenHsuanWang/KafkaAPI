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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import static kafkasystem.KafkaProducerSenderGUI.showException;
import org.apache.kafka.clients.producer.RecordMetadata;

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

        
        Vector<HashMap> stackTestUnit = new Vector<>();

        //=======================//
        // Create Kafka producer //
        //=======================//
        testProducer.initKafkaProducer(kafkaProducerProps);
        producerGUI.setView(kafkaProducerProps);

        producerGUI.fileChooserItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    File[] inputFiles = getFilesfromChooserAction(evt, producerGUI);
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
                    
                    for (HashMap stackTestUnit1 : stackTestUnit) {
                        System.out.print(Double.parseDouble(stackTestUnit1.get("SerializedValueSize").toString())/(1024*1024)+"MB, spend time: ");
                        System.out.print(stackTestUnit1.get("SendMesgConsumedTime")+"    ");
                        System.out.println("Sending speed from producer to Kafka broker:  " + Double.parseDouble(stackTestUnit1.get("SerializedValueSize").toString())/(1024*1024)/Double.parseDouble(stackTestUnit1.get("SendMesgConsumedTime").toString())*Math.pow(10, 6)+" MB/s");
                    }
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
                    sendMessageAction(evt, producerGUI, testProducer, kafkaProducerProps, kafkaSystem.getFiles(), stackTestUnit);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(KafkaSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    //=====================//
    // End of main methods //
    //=====================//
    
    public void setFiles(File[] inputFiles){
        this.inputFiles = inputFiles;
    }
    
    public File[] getFiles(){
        return this.inputFiles;
    }
    
    
    //============================//
    // Action Methods of listener //
    //============================//
    
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
    
    
    
    public static void sendMessageAction(java.awt.event.ActionEvent evt, KafkaProducerSenderGUI producerGUI, KafkaConsoleAPI.KafkaProducerCreator testProducer, Properties kafkaProducerProps, File[] readFiles, Vector<HashMap> stackTestUnit) throws FileNotFoundException {
        String displayMessageInfo = "";
        if (readFiles == null){
            showException("Selected Files are null", "No files are selected!! \nPlease select file for sending first!!", 2);
        }
        HashMap testUnitReturnInfo = new HashMap();
        for (int i = 0; i < readFiles.length; i++) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(readFiles[i]));
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
                        long startSendTimestamp = System.currentTimeMillis();
                        displayMessageInfo = displayMessageInfo+"Going to send message from file: "+ readFiles[i].getPath()+" : \n";
                        
                        RecordMetadata replyRecord = testProducer.sendMessage(kafkaProducerProps.getProperty("topic.name"), kafkaProducerProps.getProperty("message.key") , sb.toString());
                        long finalSendTimestamp = System.currentTimeMillis();
                        if (replyRecord != null) {

                            displayMessageInfo = displayMessageInfo + "Message send successfully :\n";
                            displayMessageInfo = displayMessageInfo + "    Timestamp: " + replyRecord.timestamp() + "\n";
                            displayMessageInfo = displayMessageInfo + "    Topic: " + replyRecord.topic() + "\n";
                            displayMessageInfo = displayMessageInfo + "    Offset: " + replyRecord.offset() + "\n";
                            displayMessageInfo = displayMessageInfo + "    Serialized Value Size: " + replyRecord.serializedValueSize() + "\n";
                            displayMessageInfo = displayMessageInfo + "Send message time consumed: " + (finalSendTimestamp - startSendTimestamp) + " miliseconds \n\n";
                            
                            testUnitReturnInfo.put("TestFile", "readFiles[i].getPath()");
                            testUnitReturnInfo.put("Timestamp", replyRecord.timestamp());
                            testUnitReturnInfo.put("Topic", replyRecord.topic());
                            testUnitReturnInfo.put("Offset", replyRecord.offset());
                            testUnitReturnInfo.put("SerializedValueSize", replyRecord.serializedValueSize());
                            testUnitReturnInfo.put("SendMesgConsumedTime", finalSendTimestamp-startSendTimestamp);
                            
                            stackTestUnit.add(testUnitReturnInfo);
                        
                        } else {
                            displayMessageInfo = displayMessageInfo + "Error! Failed to send message from file: " + readFiles[i].getPath() + " : \n";
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(KafkaSystem.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ExecutionException ex) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        ex.printStackTrace(pw);
                        
                        displayMessageInfo = displayMessageInfo + "Error! Failed to send message from file: "+ readFiles[i].getPath()+" : \n";
                        displayMessageInfo = displayMessageInfo + sw.toString();
                    } catch (org.apache.kafka.common.errors.RecordTooLargeException recoardLardeEx) {

                    }
                }
            } catch (IOException ioe) {

            }
        }
        producerGUI.showSendMessageReply.setText(displayMessageInfo);
    }

}