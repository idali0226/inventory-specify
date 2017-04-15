/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.inventory.client.exception.handler;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 *
 * @author idali
 */
public class DinaExceptionHandlerFactory extends ExceptionHandlerFactory {
    
    private final ExceptionHandlerFactory base;
   
    public DinaExceptionHandlerFactory(ExceptionHandlerFactory base) {
        this.base = base;
    }
    

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new DinaExceptionHandler(base.getExceptionHandler());
    }
    
}
