/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.inventory.client.controller;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped; 
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext; 
import javax.inject.Named; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author idali
 */
@Named(value = "navigate") 
@RequestScoped
public class Navigate implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
     
    private static final String SPECIES_LIST_PATH = "/faces/pages/speciesListPage.xhtml";  
    private static final String OBSERVATION_PAGE_PATH = "/faces/pages/observation.xhtml";
    private static final String UPLOAD_PAGE_PATH = "/faces/pages/uploadSummary.xhtml";
 
    private ExternalContext externalContext;
 
    public Navigate() {

    }
 
    public void speciesListPage() {
        redirectPage(SPECIES_LIST_PATH); 
    }
     
    public void observationPage() {
        redirectPage(OBSERVATION_PAGE_PATH);
    }
    
    public void uploadSummaryPage() {
        redirectPage(UPLOAD_PAGE_PATH);
    }
    
    private void redirectPage(String path) {
        
        logger.info("path : {}", path);
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + path);
//            externalContext.redirect(path);
        } catch (IOException ex) {
        }
    } 
}
