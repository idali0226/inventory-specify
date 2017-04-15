/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.inventory.client.controller;
 
import java.io.Serializable; 
import java.util.ArrayList;  
import java.util.Date;
import java.util.HashMap;  
import java.util.List;
import java.util.Map; 
import java.util.stream.Collectors;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;   
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook; 
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.inventory.client.exception.InvalidExcelFileException; 
import se.nrm.dina.inventory.client.fileupload.ExcelFileHandler;
import se.nrm.dina.inventory.client.services.SmtpServiceClient;
import se.nrm.dina.inventory.client.util.CommonMessages; 
import se.nrm.dina.inventory.client.util.Util;  
import se.nrm.dina.inventory.client.vo.ExcelData;
import se.nrm.dina.inventory.client.vo.ObservationData;
import se.nrm.dina.inventory.client.vo.TaxaData;

/**
 *
 * @author idali
 */
@Named(value = "fileUpload") 
@SessionScoped
public class FileUploader implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private String fileName; 
    private UploadedFile file;  
    private Workbook workbook; 
    private boolean isFileUpload; 
    
    private Map<String, Integer> obsTitleMap;
    private Map<String, Integer> taxonTitleMap;
    private final int MAX_COLUMN_NUMBER = 35;
    
//    private ExcelMetadata metadata;
    
    private List<ObservationData> obsList;
    private List<TaxaData> taxonList;
    
    private boolean openObservation;
    private boolean openTaxonList;
      
    private boolean isMetadataValid;
    private boolean isObservationValid;
    private boolean isTaxonListValid;
     
    private String selectedAgent;
    
    private Map<String, Integer> agents;
    private Date submittedDate;
    private final String maxDate;
    
    public FileUploader() {
        maxDate = Util.getNow();
        initData();
        selectedAgent = null;
    }
    
    @Inject
    private ExcelFileHandler fileHandler;
    
    @Inject
    private Navigate navigate;
    
    @Inject
    private MessageBean msgBean;
    
    @Inject
    private SmtpServiceClient client;
    
    private void initData() {
        
        agents = new HashMap<>();
        file = null;
        fileName = null;
        obsTitleMap = new HashMap<>();
        obsList = new ArrayList<>();
        
        taxonTitleMap = new HashMap<>();
        taxonList = new ArrayList<>();
          
        isObservationValid = false;
        openObservation = true;
        openTaxonList = false; 
        isTaxonListValid = false;
        
    }

    /**
     * File uploaded from client.
     *
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) {
        logger.info("handleFileUpload : {} -- {}", event.getFile(), event.getFile().getFileName());

        initData();
        file = event.getFile();
        fileName = file.getFileName();

        isFileUpload = false;
        try {
            fileHandler.saveTempFile(file);                                         // save excel file in temp file directory
            workbook = fileHandler.buildWorkbook(fileName, workbook);               // build workbook from excel file      
            fileHandler.validateSheets(workbook);                                   // validate sheets (to verify all the sheets are there)
             
            obsTitleMap = fileHandler.readInSheetTitles(workbook.getSheetAt(1), MAX_COLUMN_NUMBER);
//            fileType = fileHandler.findFileType(fileName, obsTitleMap).name().toLowerCase();
            isFileUpload = true;
        } catch(InvalidExcelFileException e) { 
            isFileUpload = false;
            msgBean.addError("Upload file failure", e.getMessage());  
        }   
    }
    
    public void openObservationPage() { 
        if(agents.isEmpty()) {
            agents = client.getAgents();
        }
        
        if(openObservation && obsList.isEmpty()) {
            Sheet sheet = workbook.getSheetAt(1);  
            Map<Integer, Integer> ceMap = new HashMap();    
            obsList = fileHandler.readInObservation(sheet, ceMap, obsTitleMap);  
            isObservationValid = true;
        } 
        
        navigate.observationPage();
    }
    
    public void handleAgentSelect() {
        logger.info("handleAgentSelect : {}", selectedAgent);
    }

    public void handleDateSelect() {
        logger.info("handleDateSelect");
    }
    
    public void dateBlur() {
        
    }
    
    public void observationListAction() {
        logger.info("observationListAction");
        
        openObservation = !openObservation;   
    } 
    
    /**
     * taxonListAction
     */
    public void taxonListAction() {
        logger.info("taxonListAction");
        openTaxonList = !openTaxonList;
        isTaxonListValid = false;
        if(openTaxonList && taxonList.isEmpty()) {
            Sheet sheet = workbook.getSheetAt(4);                                           // Taxonlist sheet
            
            taxonTitleMap = fileHandler.readInSheetTitles(sheet, MAX_COLUMN_NUMBER);        // get all the column titles
            List<String> taxonNameList = obsList.stream() 
                                            .map( o -> o.getComputedName())  
                                            .distinct()
                                            .collect(Collectors.toList()); 
              
            try {
                taxonList = fileHandler.readInTaxon(sheet, taxonTitleMap, taxonNameList); 
                isTaxonListValid = true;
            } catch(InvalidExcelFileException e) {
                msgBean.addError(CommonMessages.getInstance().INVALID_EXCEL_FILE, e.getMessage());
            } 
        }  
    }
    
    /**
     * Remove uploaded file
     */
    public void removefile() {
        logger.info("removefile");

        if (fileName != null) {
            fileHandler.removeFile(fileName);
        }
        fileName = null;
        workbook = null;  
    }
     
    
    public void uploadData() {
        logger.info("uploadData");
        ExcelData data = new ExcelData(fileName, getSelectedAgentId(), Util.dateToString(submittedDate),  obsList);
        client.upload(data);
        navigate.uploadSummaryPage();
    }

    public String getSelectedAgent() {
        return selectedAgent;
    }

    public void setSelectedAgent(String selectedAgent) {
        this.selectedAgent = selectedAgent;
    }
    
    private int getSelectedAgentId() {
        if(selectedAgent == null || selectedAgent.isEmpty()) {
            return 0;
        }  
        return NumberUtils.isNumber(selectedAgent) ? Integer.parseInt(selectedAgent) : 0;
    }
    
    
 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    


    public Map<String, Integer> getAgents() {
        return agents;
    }

    public void setAgents(Map<String, Integer> agents) {
        this.agents = agents;
    }
    
    public String getMaxDate() {
        return maxDate;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
     
     

    

     
    
    
    
    
    
    
    
    
    
    
    
    
     

    /**
     * Accumulator class to help accumulator long in foreach statement
     */
    public static class Accumulator {

        private int total = 0;
          
        public void increment() {
            total++;
        } 
    } 
     
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    } 

    public boolean isIsFileUpload() {
        return isFileUpload;
    }

    public void setIsFileUpload(boolean isFileUpload) {
        this.isFileUpload = isFileUpload;
    }
 
    public boolean isIsMetadataValid() {
        return isMetadataValid;
    }

    public void setIsMetadataValid(boolean isMetadataValid) {
        this.isMetadataValid = isMetadataValid;
    }

    public boolean isIsObservationValid() {
        return isObservationValid;
    }

    public void setIsObservationValid(boolean isObservationValid) {
        this.isObservationValid = isObservationValid;
    }

    public boolean isIsTaxonListValid() {
        return isTaxonListValid;
    }

    public void setIsTaxonListValid(boolean isTaxonListValid) {
        this.isTaxonListValid = isTaxonListValid;
    }

    public boolean isOpenObservation() {
        return openObservation;
    }

    public void setOpenObservation(boolean openObservation) {
        this.openObservation = openObservation;
    }

    public List<ObservationData> getObsList() {
        return obsList;
    }

    public void setObsList(List<ObservationData> obsList) {
        this.obsList = obsList;
    }

    public boolean isOpenTaxonList() {
        return openTaxonList;
    }

    public void setOpenTaxonList(boolean openTaxonList) {
        this.openTaxonList = openTaxonList;
    }

    public List<TaxaData> getTaxonList() {
        return taxonList;
    }

    public void setTaxonList(List<TaxaData> taxonList) {
        this.taxonList = taxonList;
    } 
}
