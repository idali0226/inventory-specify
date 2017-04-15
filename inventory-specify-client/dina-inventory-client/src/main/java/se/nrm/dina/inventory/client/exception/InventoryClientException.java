/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.inventory.client.exception;

/**
 *
 * @author idali
 */
public class InventoryClientException extends RuntimeException  {
    
    public InventoryClientException() {
    }

    public InventoryClientException(String s) {
        super(s);
    }

    public InventoryClientException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InventoryClientException(Throwable throwable) {
        super(throwable);
    }  
}
