/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kafkasystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 *
 * @author willy
 */
public class KafkaProducerSenderGUI extends JFrame {
    
    public static JFrame frame = new JFrame("Kafka Producer Sender");
    
    private JPanel left = new JPanel();
    private JScrollPane right = new JScrollPane();
    private JScrollPane top = new JScrollPane();
    private JPanel middle = new JPanel();
    private JPanel bottom = new JPanel();
    
    private JScrollPane listFSelectedFiles = new JScrollPane();
    private JScrollPane showsSendingMessage = new JScrollPane();
    private JScrollPane showsCallback = new JScrollPane();
    
    public JTextArea showFileInput = new JTextArea(5, 25);
    public JTextArea showSendMessageReply = new JTextArea();
    
    private JMenuBar menubar = new JMenuBar();
    private JMenu menu = new JMenu();
    public JMenuItem fileChooserItem = new JMenuItem();
    
    
    JLabel lableBrokerIP = new JLabel("Setting Broker IP");
    JLabel lableBrokerPort = new JLabel("Setting Broker Port");
    JLabel lableTopicName = new JLabel("Setting Topic");
    public JTextField brokerIP = new JTextField(15);
    public JTextField brokerPort = new JTextField(15);
    public JTextField topicName = new JTextField(15);
    public JButton sendMessageButton = new JButton();
    
    public void setView(Properties kafkaProducerProps) {

        menu.setText("File"); // setting the menu text
        fileChooserItem.setText("Open.."); // setting the menu item text
        menu.add(fileChooserItem); // add item in to menu
        menubar.add(menu); // add menu into menubar
        frame.add(menubar);
        frame.setJMenuBar(menubar); // set the menubar to the position
        
        frame.setLayout(new GridLayout(1,2,2,3));
        left.setLayout(new GridLayout(3,1,5,2));
        left.setBorder(BorderFactory.createLineBorder(Color.black));
        right.setBorder(BorderFactory.createLineBorder(Color.black));

        top.setBorder(BorderFactory.createLineBorder(Color.black));
        middle.setBorder(BorderFactory.createLineBorder(Color.black));
        bottom.setBorder(BorderFactory.createLineBorder(Color.black));
        
        top.add(showFileInput);
        showFileInput.setEditable(false);
        top.setViewportView(showFileInput);
        
        right.add(showSendMessageReply);
        showSendMessageReply.setEditable(false);
        right.setViewportView(showSendMessageReply);
        
        frame.getContentPane().add(left, BorderLayout.WEST);
        frame.getContentPane().add(right, BorderLayout.EAST);
        left.add(top, BorderLayout.NORTH);
        left.add(middle, BorderLayout.CENTER);
        left.add(bottom, BorderLayout.SOUTH);
        frame.setBounds(0, 0, 800, 600);
        //frame.setLayout(new BorderLayout());

        
        
        //=====================================//
        // TextField and bottum's group layout //
        // Of Broker setting for send message  //
        //=====================================//
        sendMessageButton.setText("Send Message");
        GroupLayout displayParsingInfoPanelLayout = new GroupLayout(middle);
        middle.setLayout(displayParsingInfoPanelLayout);
        displayParsingInfoPanelLayout.setHorizontalGroup(
                displayParsingInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, displayParsingInfoPanelLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(displayParsingInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lableBrokerIP)
                                .addComponent(lableBrokerPort)
                                .addComponent(lableTopicName))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addGroup(displayParsingInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(brokerIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(brokerPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(topicName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(sendMessageButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23))
        );
        displayParsingInfoPanelLayout.setVerticalGroup(
                displayParsingInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(displayParsingInfoPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(displayParsingInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(brokerIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(lableBrokerIP))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(displayParsingInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(brokerPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(lableBrokerPort))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(displayParsingInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(topicName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(lableTopicName))
                        .addGap(20)
                        .addGroup(displayParsingInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(sendMessageButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(90, Short.MAX_VALUE))
        );

        brokerIP.setEditable(false);
        String ipPort = kafkaProducerProps.getProperty("bootstrap.servers");
        brokerIP.setText(ipPort.substring(0, ipPort.indexOf(":")));
        brokerPort.setEditable(false);
        brokerPort.setText(ipPort.substring(ipPort.indexOf(":")+1, ipPort.length()));
        topicName.setEditable(false);
        topicName.setText(kafkaProducerProps.getProperty("topic.name"));
        
        //frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
/*
    void fileChooserActionPerformed(ActionEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
*/

    /*
    public File[] getFilesfromChooserAction(java.awt.event.ActionEvent evt) throws ParseException {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);

        int returnValue = fc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File[] inFile = fc.getSelectedFiles();
            String showInFilePath = "";
            for(int i=0 ; i<inFile.length ; i++){
                showInFilePath=showInFilePath+inFile[i].getAbsolutePath()+"\n";
            }
            showFileInput.setText(showInFilePath);
            return inFile;
        }
        return null;
    }
    */
    
    /*
    public void sendMessageAction(java.awt.event.ActionEvent evt, KafkaConsoleAPI.KafkaProducerCreator testProducer, Properties kafkaProducerProps, File[] readFiles) throws FileNotFoundException {
        String displayMessageInfo = "";
        if (readFiles == null){
            showException("Selected Files are null", "No files are selected!! \nPlease select file for sending first!!", 2);
        }
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
        showSendMessageReply.setText(displayMessageInfo);
    }*/
    
    
    //====================================//
    // Design of exception message dialog //
    //====================================//
    public static void showException(String title, String content, int type) {
        JOptionPane.showMessageDialog(frame, content, title, type);
    }
    
    

}
