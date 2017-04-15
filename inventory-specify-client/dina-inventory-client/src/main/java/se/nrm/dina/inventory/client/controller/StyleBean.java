package se.nrm.dina.inventory.client.controller;

import java.io.Serializable; 
import javax.enterprise.context.SessionScoped; 
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory; 

/**
 *
 * @author idali
 */
@SessionScoped
@Named
public class StyleBean implements Serializable {
     
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
                                                   
 
     
     
    private final static String CURRENT = "current";
    private final static String ADD_NEW_SAMPLE = "add.png";
    private final static String CANCEL_NEW_SAMPLE = "delete.png";
    
    private String menu1 = CURRENT;
    private String menu2; 
    
    private String addNewSampleButton = ADD_NEW_SAMPLE; 
       
    public StyleBean() { 
    }
 
    public String getMenu1() {
        return menu1;
    }

    public void setMenu1(String menu1) {
        this.menu1 = menu1;
    }

    public String getMenu2() {
        return menu2;
    }
 
    public void setMenu2(String menu2) { 
        this.menu2 = menu2;
    }
    
    public void setAddNewSampleButon(boolean addNew) { 
        addNewSampleButton = addNew ? CANCEL_NEW_SAMPLE : ADD_NEW_SAMPLE;
    }

    public String getAddNewSampleButton() {
        logger.info("addNewSampleButton : {}", addNewSampleButton);
        return addNewSampleButton;
    }
    
    
    
    public void setCurrentTab(int tab) {

        this.menu1 = "";
        this.menu2 = ""; 
        if (tab == 1) {
            this.menu1 = CURRENT; 
        } else { 
            this.menu2 = CURRENT; 
        }  
    } 
}
