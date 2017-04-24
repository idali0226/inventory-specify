/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.inventory.client.fileupload;

import java.io.File; 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;  
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest; 
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.record.RecordFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.inventory.client.exception.InvalidExcelFileException;
import se.nrm.dina.inventory.client.util.CommonMessages;
import se.nrm.dina.inventory.client.vo.ObservationData; 
import se.nrm.dina.inventory.client.vo.TaxanData;

/**
 *
 * @author idali
 */
@Stateless
public class ExcelFileHandler implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final static String LOCAL_TEMP_FILE_DIRECTORY = "/Users/idali/temp/inventory_temp_file";
    private final static String REMOTE_TEMP_FILE_DIRECTORY = "/home/admin/wildfly-8.1.0-0/excel/";
    private String TEMP_FILE_PATH = null;
 
    
    private String genus;
    private String species; 
    private String determiner;
    private String determinedDate;
    private int numOfMales;
    private int eventId;
    private int numOfFemales;
    private int total;
    private String storage;
    private String media; 
    private Map<String, Integer> obsvTitleMap; 
    
    private Map<String, String> taxonGuidMap;
    
    
    
    private String author;
    private String guid;
    private String source;
    private String notes; 
    private String computedName;
     
    private Map<String, Integer> taxonListTitleMap;
    private List<String> usedTaxonList;
     

    private String servername = null;
    private final static String LOCAL_HOST = "localhost";
    
    public ExcelFileHandler() { 
        
        if(TEMP_FILE_PATH == null || TEMP_FILE_PATH.isEmpty()) {
            servername = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
            logger.info("server name : {}", servername);
            TEMP_FILE_PATH = servername.equals(LOCAL_HOST) ? LOCAL_TEMP_FILE_DIRECTORY : REMOTE_TEMP_FILE_DIRECTORY; 
        } 
    }
 
    
    /**
     * getWorkbook method to build Workbook from excel file
     * 
     * @param excelFile
     * @param workbook
     * @return 
     * @throws InvalidExcelFileException
     */
    public Workbook buildWorkbook(String excelFile, Workbook workbook) {
        
        File file = new File(TEMP_FILE_PATH, excelFile);
        try {
            return WorkbookFactory.create(file);
        } catch (IOException | InvalidFormatException | RecordFormatException ex) { 
            throw new InvalidExcelFileException(ex.getMessage());
        }
    }





    
    
    
 
    
    

     
     

    
    
    
    

    

    
    private String getCellStringValue(Cell c) {
  
        return c == null ? "" : c.getCellType() == Cell.CELL_TYPE_STRING ?  
                                c.getStringCellValue() :  
                                String.valueOf((int)c.getNumericCellValue());
    }


 
    
    

     




    
 


}
