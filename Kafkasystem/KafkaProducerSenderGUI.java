/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kafkasystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.ParseException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author willy
 */
public class KafkaProducerSenderGUI extends JFrame {
    
    private static JFrame frame = new JFrame("Kafka Producer Sender");
    
    private JPanel left = new JPanel();
    private JScrollPane right = new JScrollPane();
    private JScrollPane top = new JScrollPane();
    private JPanel middle = new JPanel();
    private JPanel bottom = new JPanel();
    
    private JScrollPane listFSelectedFiles = new JScrollPane();
    private JScrollPane showsSendingMessage = new JScrollPane();
    private JScrollPane showsCallback = new JScrollPane();
    
    private JTextArea showFileInput = new JTextArea(5, 25);
    
    private JMenuBar menubar = new JMenuBar();
    private JMenu menu = new JMenu();
    public JMenuItem fileChooserItem = new JMenuItem();
    
    JTextField brokerIP = new JTextField();
    JTextField brokerPort = new JTextField();

    public void setView() {

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
        
        frame.getContentPane().add(left, BorderLayout.WEST);
        frame.getContentPane().add(right, BorderLayout.EAST);
        left.add(top, BorderLayout.NORTH);
        left.add(middle, BorderLayout.CENTER);
        left.add(bottom, BorderLayout.SOUTH);
        frame.setBounds(0, 0, 800, 600);
        //frame.setLayout(new BorderLayout());


        
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


}
