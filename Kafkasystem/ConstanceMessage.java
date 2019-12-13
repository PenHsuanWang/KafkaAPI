/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kafkasystem;

/**
 *
 * @author willy
 */
public class ConstanceMessage {
    
    private static ConstanceMessage instance = null;
    
    private ConstanceMessage(){}
    
    static public ConstanceMessage getInstance(){
        if (instance == null){
            instance = new ConstanceMessage();
        } 
        return instance;
    }
    
    
    public static final String PROPERIES_SETTING_ERROR = "Producer API perperties setting file reading error. \nPlease check the file in the \\var\\KafkaProducer.propeties";
    public static final String PROPERIES_FILENOTFOUND = "Producer API perperties setting file NOT found. \nPlease create a perperties file \\var\\KafkaProducer.properties";
    public static final String FAILED_TO_CREATE_PRODUCER_AIP = "Can not create Producer AIP";
}
