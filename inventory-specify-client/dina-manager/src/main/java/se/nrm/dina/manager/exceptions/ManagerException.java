/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.manager.exceptions;

import javax.ejb.ApplicationException;

/**
 *
 * @author idali
 */
@ApplicationException
public class ManagerException extends RuntimeException   {
    
    private int errorCode;
    
    public ManagerException() {
    }

    public ManagerException(String s) {
        super(s);
    }
 
    public ManagerException(String s, int errorCode) {
        super(s);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    } 
}
