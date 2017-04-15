package se.nrm.dina.inventory.client.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable; 
import javax.enterprise.context.SessionScoped; 
import javax.inject.Named; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author idali
 */
@Named(value = "languages")
@SessionScoped
public class Languages implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private String locale = "sv";   

    public Languages() {
    } 
    
    public void changelanguage(String locale) {
        logger.info("changelanguage : {}", locale);
        
        setLocale(locale);
    }
 
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) { 
        this.locale = locale;
    }
       
    public String getLanguage() {  
        return locale.equals("sv") ? "English" : "Svenska";
    }
      
    public boolean isIsSwedish() {
        return locale.equals("sv");
    }   
}
