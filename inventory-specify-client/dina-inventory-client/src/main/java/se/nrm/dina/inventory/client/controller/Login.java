/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.inventory.client.controller;

import java.io.Serializable;     
import javax.enterprise.context.SessionScoped;    
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  
import se.nrm.dina.inventory.client.util.CommonMessages; 

/**
 *
 * @author idali
 */
@Named("login")
@SessionScoped
public class Login implements Serializable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass()); 
    
    private final HttpSession session;
    private FacesContext context; 
    private HttpServletRequest request; 

    private String username;
    private String password;
   
    @Inject
    private Navigate navigate;
    
    @Inject
    private MessageBean msg;
 
    
    public Login() { 
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

    public void login() {

        logger.info("login");

        String loginFailedMessage = null;
        context = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) context.getExternalContext().getRequest();
 
        try {
            request.login(username, password);  
        } catch (ServletException ex) { 
            loginFailedMessage = CommonMessages.getInstance().INCORRECT_USERNAME_OR_PASSWORD; 
        } 
        if (loginFailedMessage == null) {
            session.setAttribute(CommonMessages.getInstance().LOGGED_IN_USER, username);
            navigate.speciesListPage();
        } else { 
            msg.addError("Login failed", loginFailedMessage);
            try {
                request.logout();
            } catch (ServletException ex) {
                logger.error(CommonMessages.getInstance().LOGOUT_FAILED, ex.getMessage());
            } 
        }
    }

    public void logout() {
        logger.info("logout");
          
        context = FacesContext.getCurrentInstance(); 
        request = (HttpServletRequest) context.getExternalContext().getRequest();
         
        try {  
            request.logout(); 
             
            session.removeAttribute(CommonMessages.getInstance().LOGGED_IN_USER); 
            session.invalidate();  
            navigate.speciesListPage(); 
        } catch (ServletException e) {
            logger.error(CommonMessages.getInstance().LOGOUT_FAILED, e.getMessage()); 
        } 
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }  

    public boolean isIsTest() {
        return false;
    }  
}
