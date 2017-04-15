/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.inventory.client.controller;
 
import java.io.FileInputStream; 
import java.io.FileNotFoundException;
import java.io.InputStream;  
import java.io.Serializable; 
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;   
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 

/**
 *
 * @author idali
 */
@Named(value = "download") 
@SessionScoped
public class FileDownloader implements Serializable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
     
    private final String LOCAL_FILE_PATH = "/Users/idali/Desktop/";
    private final String REMOTE_FILE_PATH = "/home/admin/wildfly-8.1.0-0/testfiles/";
    private final String C_FILE = "SMTP_Data_Dryinidae_MO_C.xls";
    private final String D_FILE = "SMTP_Data_Heloridae_D_EM.xls"; 
     
    private final String TEST_FILE_PATH; 
    private final static String LOCAL_HOST = "localhost";
    
    private final static String MIMETYPE = "application/octet-stream";
     
    private final String servername;
 
      
    private StreamedContent file; 
     
    public FileDownloader() {
        logger.info(this.getClass().getName());
        
        servername = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
        logger.info("server name : {}", servername);
        TEST_FILE_PATH = servername.equals(LOCAL_HOST) ? LOCAL_FILE_PATH : REMOTE_FILE_PATH;
    }
    
    public void download(boolean isCfile ) { 
        logger.info("download : {}", isCfile);

        String fileName = isCfile ? C_FILE : D_FILE;
        try {
            String fileurl = TEST_FILE_PATH + fileName;
            InputStream stream = new FileInputStream(fileurl);
            file = new DefaultStreamedContent(stream, MIMETYPE, fileName);

        } catch (FileNotFoundException ex) {
            logger.error(ex.getMessage());
        }
    }

    public StreamedContent getFile() {
        return file;
    }
}
