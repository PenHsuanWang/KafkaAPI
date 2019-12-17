/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SelfDefinedException;

/**
 *
 * @author willy
 */
public class ProducerException extends Exception {
    
    private String ErrorMessage = "";
    
    public ProducerException(String msg){
        super(msg);
    }
    
    public String getErrorMessage(){
        return this.ErrorMessage;
    }
    
}

