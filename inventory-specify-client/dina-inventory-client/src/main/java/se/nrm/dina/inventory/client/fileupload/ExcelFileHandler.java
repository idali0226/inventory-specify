/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.inventory.client.fileupload;

import java.io.File;
import java.io.FileOutputStream;
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
import se.nrm.dina.inventory.client.vo.TaxaData;

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
     * saveTempFile to save uploaded file into temp directory
     *
     * @param uploadFile
     * @return
     */
    public String saveTempFile(UploadedFile uploadFile) {
        logger.info("saveTempFile : {} ", uploadFile.getFileName());

        File targetFile;
        String fileName = uploadFile.getFileName();
        try (InputStream initialStream = uploadFile.getInputstream()) {
            byte[] buffer = new byte[initialStream.available()];
            initialStream.read(buffer);

            targetFile = new File(TEMP_FILE_PATH, fileName);
            try (OutputStream outStream = new FileOutputStream(targetFile)) {
                outStream.write(buffer);
                outStream.close();
            }
            initialStream.close();
            return targetFile.getPath();
        } catch (IOException ex) {
            throw new InvalidExcelFileException(ex.getMessage());
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

    public boolean validateSheets(Workbook workbook) {
        if (!workbook.getSheetName(0).equals("Metadata")) {
            throw new InvalidExcelFileException(CommonMessages.getInstance().MISSING_METADAT_SHEET); 
        }
        if (!workbook.getSheetName(1).equals("Observation")) {
            throw new InvalidExcelFileException(CommonMessages.getInstance().MISSING_OBSERVATION_SHEET);  
        }
        if (!workbook.getSheetName(2).equals("Trap")) {
            throw new InvalidExcelFileException(CommonMessages.getInstance().MISSING_TRAP_SHEET);   
        }
        if (!workbook.getSheetName(3).equals("CollectingEvent")) {
            throw new InvalidExcelFileException(CommonMessages.getInstance().MISSING_COLLECTINGEVENT_SHEET);    
        }
        if (!workbook.getSheetName(4).equals("TaxonList")) {
            throw new InvalidExcelFileException(CommonMessages.getInstance().MISSING_TAXONLIST_SHEET);  
        }
        return true;
    }
    
    public Map<String, Integer> readInSheetTitles(Sheet sheet, int maxNumColumn) {
        Map<String, Integer> map = new HashMap<>();
        Row row = sheet.getRow(0);
        IntStream.range(0, maxNumColumn)
                .filter(i -> row.getCell(i) != null)
                .mapToObj(i -> row.getCell(i))
                .forEach(c -> {
                    map.put(c.getStringCellValue(), c.getColumnIndex());
                });
        return map;
    }
 
    
 
    private int getCellIntValue(Cell c) {
        return  c == null ? 0 : c.getCellType() == Cell.CELL_TYPE_STRING ? 
                                Integer.parseInt(c.getStringCellValue()) : 
                                (int) c.getNumericCellValue();
    }
    
    public List<ObservationData> readInObservation(Sheet sheet, Map<Integer, Integer> map, Map<String, Integer> titleMap ) {
        this.obsvTitleMap = titleMap;

        logger.info("texon guid map : {}", taxonGuidMap );

        return IntStream.range(1, sheet.getLastRowNum() + 1)
                .mapToObj(i -> sheet.getRow(i))
                .filter(isNotEmpty())
                .map(this::buildCollectionObjData)
                .collect(Collectors.toList());
    }
    
    
    private ObservationData buildCollectionObjData(Row row) {

        resetData();
        Cell cell = row.getCell(obsvTitleMap.get("Genus"));
        genus = cell == null ? "" : cell.getStringCellValue();
    

        cell = row.getCell(obsvTitleMap.get("Species"));
        species = cell == null ? "" : cell.getStringCellValue();
        
        cell = row.getCell(obsvTitleMap.get("Determined by"));
        determiner = cell == null ? "" : cell.getStringCellValue();
    

        cell = row.getCell(obsvTitleMap.get("Det. date"));
        getDeterminedDate(cell); 
         
        cell = row.getCell(obsvTitleMap.get("EventID"));
        eventId = getCellIntValue(cell);
          
        cell = row.getCell(obsvTitleMap.get("Males")); 
        numOfMales = getCellIntValue(cell);
        
        cell = row.getCell(obsvTitleMap.get("Females")); 
        numOfFemales = getCellIntValue(cell);
    
        cell = row.getCell(obsvTitleMap.get("Total"));
        total = getCellIntValue(cell); 
        
        if(obsvTitleMap.containsKey("Placed/Stored")) {
            cell = row.getCell(obsvTitleMap.get("Placed/Stored")); 
        } else if(obsvTitleMap.containsKey("Stored")) {
            cell = row.getCell(obsvTitleMap.get("Stored")); 
        } else if(obsvTitleMap.containsKey("Storage")) {
            cell = row.getCell(obsvTitleMap.get("Storage")); 
        } else {
            cell = null;
        }
        storage = cell == null ? "" : cell.getStringCellValue();
        
        cell = row.getCell(obsvTitleMap.get("Media")); 
        media = cell == null ? "" : cell.getStringCellValue();
        
        cell = row.getCell(obsvTitleMap.get("Notes")); 
        notes = cell == null ? "" : cell.getStringCellValue();
        
        String fullName = genus + " " + species;
        logger.info("full name : {}", fullName);
         
        return new ObservationData(eventId, genus, species, determiner, determinedDate, 
                                   storage, media, notes, numOfMales, numOfFemales, total);
    }
    
//    private String getGuid(String fullName) {
//        return taxaDataList.stream()
//                .filter(t -> t.getComputedName().equals(fullName))
//                .findFirst()
//                .get()
//                .getGuid(); 
//    }
//    
    
    private void getDeterminedDate(Cell cell) {
        if(cell.getCellType() == Cell.CELL_TYPE_STRING) {
            determinedDate = cell.getStringCellValue();
            if(determinedDate.contains("/")) {
                determinedDate = StringUtils.replace(determinedDate, "/", "-");
            } 
        } else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            String dateInt = String.valueOf((int)cell.getNumericCellValue());
            if(dateInt.length() > 4) {
                dateInt = dateInt.substring(0, 4);
            }
            determinedDate = dateInt.concat("-01-01");
        }
    }
    
    
    public List<TaxaData> readInTaxon(Sheet sheet, Map<String, Integer> titleMap, List<String> usedTaxonList) {
        this.usedTaxonList = usedTaxonList;
        this.taxonListTitleMap = titleMap;
        
        logger.info("used taxon list : {}", usedTaxonList);
        
        int index = titleMap.get("Taxon name (computed)");
        logger.info("comput name index : {}", index);
        
         
        try {
            return IntStream.range(1, sheet.getLastRowNum() + 1) 
                                    .mapToObj(i -> sheet.getRow(i))
                                    .filter(isNotEmpty())
                                    .filter(isInList(index))  
                                    .map(this::buildTaxaData)
                                    .collect(Collectors.toList()); 
        } catch(InvalidExcelFileException e) {
            throw e;   
        } 
    }
     
     
     
    private Predicate<Row> isNotEmpty() {
       
        return r -> r != null && r.getCell(0) != null && !r.getCell(0).getStringCellValue().isEmpty();
    }
    
    
    
    
    private void resetData() {
        genus = "";
        species = ""; 
        eventId = 0; 
        numOfMales = 0;
        numOfFemales = 0;
        total = 0;
        storage = "";
        media = "";
        notes = "";
    }
    

    
    private String getCellStringValue(Cell c) {
  
        return c == null ? "" : c.getCellType() == Cell.CELL_TYPE_STRING ?  
                                c.getStringCellValue() :  
                                String.valueOf((int)c.getNumericCellValue());
    }


 
    
    
    private String getComputedName(String genus, String species) {
           
        StringBuilder sb = new StringBuilder();
        if (species.equals("sp.")) {
            sb.append(genus);
        } else {
            sb.append(genus);
            sb.append(" ");
            sb.append(species);
        } 
        return sb.toString();
    }
     


    private Predicate<Row> isInList(int index) { 
        return r ->  r.getCell(index) != null && usedTaxonList.contains(r.getCell(index).getStringCellValue());
    }

    
    private TaxaData buildTaxaData(Row row) {
         
        Cell cell = row.getCell(taxonListTitleMap.get("Genus"));
        genus = getCellStringValue(cell);
    
        cell = row.getCell(taxonListTitleMap.get("Species"));
        species = getCellStringValue(cell);
  
        cell = row.getCell(taxonListTitleMap.get("Author"));
        author = getCellStringValue(cell);
         
        cell = row.getCell(taxonListTitleMap.get("GUID"));
        guid = getCellStringValue(cell);
  
        cell = row.getCell(taxonListTitleMap.get("Source"));
        source = getCellStringValue(cell);
 
        if(taxonListTitleMap.containsKey("Notes (errors, synonyms, undescribed etc)")) {
            cell = row.getCell(taxonListTitleMap.get("Notes (errors, synonyms, undescribed etc)")); 
        } else if(taxonListTitleMap.containsKey("Notes")) {
            cell = row.getCell(taxonListTitleMap.get("Notes")); 
        } else {
            cell = null;
        }
        notes = getCellStringValue(cell);
        
//        computedName = getComputedName(genus, species); 
//        cell = row.getCell(taxonListTitleMap.get("Taxon name (computed)"));
        
        computedName = genus + " " + species;
        
        return new TaxaData(0, genus, species, computedName, guid, author, notes, source, "", null);
    }
    
 

    public void removeFile(String fileName) {
        File thisFile = new File(TEMP_FILE_PATH, fileName);
        if (thisFile.exists()) {
            thisFile.delete();
        }
    } 
}
