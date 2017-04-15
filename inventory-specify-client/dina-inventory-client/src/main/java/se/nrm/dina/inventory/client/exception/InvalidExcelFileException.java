/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.inventory.client.exception;

import javax.ejb.ApplicationException;

/**
 *
 * @author idali
 */
@ApplicationException
public class InvalidExcelFileException extends InventoryClientException {

    public InvalidExcelFileException(String message) {
        super(message);
    }

    public InvalidExcelFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidExcelFileException(Throwable cause) {
        super(cause);
    }
    
    public static String getExceptionName() {
        return InvalidExcelFileException.class.getName();
    }
}
