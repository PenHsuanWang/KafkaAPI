/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kafkasystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;

/**
 *
 * @author willy
 */
public class KafkaProducerSenderGUI extends JFrame {
    
    public static JFrame frame = new JFrame("Kafka Producer Sender");
    
    private JPanel left = new JPanel();
    private JScrollPane right = new JScrollPane();
    private JScrollPane top = new JScrollPane();
    private JPanel panelProducerTypeSet = new JPanel();
    private JPanel middle = new JPanel();
    private JScrollPane bottom = new JScrollPane();
    
    private JScrollPane listFSelectedFiles = new JScrollPane();
    private JScrollPane showsSendingMessage = new JScrollPane();
    private JScrollPane showsCallback = new JScrollPane();
    
    public JTextArea showFileInput = new JTextArea(5, 25);
    public JTextArea showSendMessageReply = new JTextArea();
    public JTextArea showProducerDetails = new JTextArea();
    
    private JMenuBar menubar = new JMenuBar();
    private JMenu menu = new JMenu();
    public JMenuItem fileChooserItem = new JMenuItem();
    
    
    JLabel lableBrokerIP = new JLabel("Setting Broker IP");
    JLabel lableBrokerPort = new JLabel("Setting Broker Port");
    JLabel lableTopicName = new JLabel("Setting Topic");
    public JTextField brokerIP = new JTextField(15);
    public JTextField brokerPort = new JTextField(15);
    public JTextField topicName = new JTextField(15);
    
    private ButtonGroup producerTypeGroup = new ButtonGroup();
    public JRadioButton radioButtonKafkaProducerAIP = new JRadioButton();
    public JRadioButton radioButtonWECFramework = new JRadioButton();
    
    public JButton setProducerPropertiesManually = new JButton();
    public JCheckBox performProducerAIP = new JCheckBox();
    
    
    public JButton sendMessageButton = new JButton();
    public JButton sendWecDocMessageButton = new JButton();
    
    public void setView(Properties kafkaProducerProps) {

        menu.setText("File"); // setting the menu text
        fileChooserItem.setText("Open.."); // setting the menu item text
        menu.add(fileChooserItem); // add item in to menu
        menubar.add(menu); // add menu into menubar
        frame.add(menubar);
        frame.setJMenuBar(menubar); // set the menubar to the position
        
        frame.setLayout(new GridLayout(1,2,2,3));
        //left.setLayout(new GridLayout(3,1,5,2));
        left.setLayout(new GridBagLayout());
        left.setBorder(BorderFactory.createLineBorder(Color.black));
        right.setBorder(BorderFactory.createLineBorder(Color.black));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 3;
        c.weighty = 1.5;
        //c.gridheight = 2;
        c.insets = new Insets(1,1,1,1);
        c.fill = GridBagConstraints.BOTH;
        left.add(top, c);
        c.gridx = 0;
        c.gridy = 0;
        //c.gridheight = 2;
        c.weighty = 0;
        left.add(panelProducerTypeSet, c);
        c.gridx = 0;
        c.gridy = 1;
        //c.gridheight = 2;
        left.add(middle, c);
        c.gridx = 0;
        c.gridy = 2;
        //c.gridheight = 2;
        c.weighty = 1;
        left.add(bottom, c);
        
        producerTypeGroup.add(radioButtonKafkaProducerAIP);
        radioButtonKafkaProducerAIP.setHorizontalAlignment(SwingConstants.LEFT);
        radioButtonKafkaProducerAIP.setText("General Kafka Producer");
        
        producerTypeGroup.add(radioButtonWECFramework);
        radioButtonWECFramework.setHorizontalAlignment(SwingConstants.LEFT);
        radioButtonWECFramework.setText("General Kafka Producer");

        panelProducerTypeSet.add(radioButtonKafkaProducerAIP);
        panelProducerTypeSet.add(radioButtonWECFramework);
        //radioButtonKafkaProducerAIP.setSelected(true);
        radioButtonWECFramework.setSelected(true);
        
        top.setBorder(BorderFactory.createLineBorder(Color.black));
        panelProducerTypeSet.setBorder(BorderFactory.createLineBorder(Color.black));
        middle.setBorder(BorderFactory.createLineBorder(Color.black));
        middle.setSize(WIDTH, 400);
        bottom.setBorder(BorderFactory.createLineBorder(Color.black));
        
        top.add(showFileInput);
        showFileInput.setEditable(false);
        top.setViewportView(showFileInput);
        
        bottom.add(showProducerDetails);
        showProducerDetails.setEditable(false);
        bottom.setViewportView(showProducerDetails);
        
        right.add(showSendMessageReply);
        showSendMessageReply.setEditable(false);
        right.setViewportView(showSendMessageReply);
        
        frame.getContentPane().add(left, BorderLayout.WEST);
        frame.getContentPane().add(right, BorderLayout.EAST);
        //left.add(top, BorderLayout.NORTH);
        //left.add(top, c0);
        //left.add(middle, c1);
        //left.add(bottom, c2);
        frame.setBounds(0, 0, 800, 600);
        //frame.setLayout(new BorderLayout());

        
        
        //=====================================//
        // TextField and bottum's group layout //
        // Of Broker setting for send message  //
        //=====================================//
        setProducerPropertiesManually.setText("Setting properties");
        performProducerAIP.setText("Perfor Producer");
        
        sendMessageButton.setText("Send Message");
        sendWecDocMessageButton.setText("Batch Sending");
        GroupLayout displayParsingInfoPanelLayout = new GroupLayout(middle);
        middle.setLayout(displayParsingInfoPanelLayout);
        displayParsingInfoPanelLayout.setHorizontalGroup(
                displayParsingInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, displayParsingInfoPanelLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(displayParsingInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(setProducerPropertiesManually)
                                .addComponent(lableBrokerIP)
                                .addComponent(lableBrokerPort)
                                .addComponent(lableTopicName)
                                .addComponent(sendMessageButton))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addGroup(displayParsingInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(performProducerAIP)
                                .addComponent(brokerIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(brokerPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(topicName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(sendWecDocMessageButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(10))
        );
        displayParsingInfoPanelLayout.setVerticalGroup(
                displayParsingInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(displayParsingInfoPanelLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(displayParsingInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(performProducerAIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(setProducerPropertiesManually))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        
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
                        .addGap(10)
                        .addGroup(displayParsingInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(sendMessageButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(sendWecDocMessageButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(20, Short.MAX_VALUE))
        );

        //=============================================================//
        // Setting Producer API from properties setting file           //
        // If the properties setting file do not provide properties,   //
        // Please setting broker IP, port, and the topic going to send //
        //=============================================================//
        

        //frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void setProducerPropertiesTextBoxFromFile(Properties kafkaProducerProps) {
        try {
            String ipPort = kafkaProducerProps.getProperty("bootstrap.servers");
            brokerIP.setText(ipPort.substring(0, ipPort.indexOf(":")));
            brokerPort.setText(ipPort.substring(ipPort.indexOf(":") + 1, ipPort.length()));
            topicName.setText(kafkaProducerProps.getProperty("topic.name"));
            brokerIP.setEditable(false);
            brokerPort.setEditable(false);
            topicName.setEditable(false);

        } catch (Exception ex) {
            brokerIP.setText("please provide Kafka broker's IP");
            brokerPort.setText("please provide Kafka broker's port");
            topicName.setText("please provide Topic Name");
            brokerIP.setEditable(true);
            brokerPort.setEditable(true);
            topicName.setEditable(true);
            performProducerAIP.setSelected(false);
        }
    }
    
    public void resetProducerPropertiesTextBox() {
        brokerIP.setText("input Kafka broker's IP");
        brokerPort.setText("input Kafka broker's port");
        topicName.setText("input Topic Name");
        brokerIP.setEditable(true);
        brokerPort.setEditable(true);
        topicName.setEditable(true);
        performProducerAIP.setSelected(false);
    }
    
    
    public void setProducerRunningInfoFromPropertiesFile(Properties kafkaProducerProps) {
        String ipPort = kafkaProducerProps.getProperty("bootstrap.servers");
        showProducerDetails.setText(
                "Kafka producer API created successfully! \n"
                + "  producer connected to kafka broker: \n"
                + "  ip:    " + ipPort.substring(0, ipPort.indexOf(":")) + "\n"
                + "  port:  " + ipPort.substring(ipPort.indexOf(":") + 1, ipPort.length()) + "\n"
                + "  topic: " + kafkaProducerProps.getProperty("topic.name")
        );
    }
    
    //====================================//
    // Design of exception message dialog //
    //====================================//
    public static void showException(String title, String content, int type) {
        JOptionPane.showMessageDialog(frame, content, title, type);
    }    
}