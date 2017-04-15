package se.nrm.dina.inventory.client.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List; 
import javax.enterprise.context.SessionScoped;
import javax.inject.Named; 

/**
 *
 * @author idali
 */
@Named("imageBeans")
@SessionScoped
public class ImageBeans implements Serializable {
    
//    private final Logger logger = LoggerFactory.getLogger(this.getClass()); 
    
    private final List<String> images;   
    
    public ImageBeans() { 
        images = new ArrayList();  
        images.add("/resources/images/randomImages/diptera-cc-by-nc.jpg");   
        images.add("/resources/images/randomImages/clytra.jpg");    
        images.add("/resources/images/randomImages/grashoppor.jpg");  
    }

    public String getImage() {
        return images.get(getRandomNumber());
    }
    
    private int getRandomNumber() {
        int low = 0;
        int high = 2;
        return (int)(Math.random() * (high - low));
    }

    public List<String> getImages() {
        return images;
    } 
}
