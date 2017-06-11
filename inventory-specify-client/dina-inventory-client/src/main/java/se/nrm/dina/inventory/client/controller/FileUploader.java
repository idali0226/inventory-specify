/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.inventory.client.controller;
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable; 
import java.util.ArrayList;  
import java.util.Date;
import java.util.HashMap;  
import java.util.List;
import java.util.Map; 
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;   
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.record.RecordFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook; 
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.inventory.client.exception.InvalidExcelFileException;  
import se.nrm.dina.inventory.client.services.SmtpServiceClient;
import se.nrm.dina.inventory.client.util.CommonMessages; 
import se.nrm.dina.inventory.client.util.Util;  
import se.nrm.dina.inventory.client.vo.ExcelData;
import se.nrm.dina.inventory.client.vo.ObservationData;
import se.nrm.dina.inventory.client.vo.TaxanData;
import se.nrm.dina.inventory.client.vo.TaxonVO; 

/**
 *
 * @author idali
 */
@Named(value = "fileUpload") 
@SessionScoped
public class FileUploader implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final static String LOCAL_TEMP_FILE_DIRECTORY = "/Users/idali/temp/inventory_temp_file";
//    private final static String REMOTE_TEMP_FILE_DIRECTORY = "/home/admin/wildfly-8.1.0-0/excel/";
    
    private String fileName; 
    private UploadedFile file;  
    private Workbook workbook; 
    private boolean isFileUpload; 
    
    private Map<String, Integer> obsTitleMap;
    private Map<String, Integer> taxonTitleMap;
    private final int MAX_COLUMN_NUMBER = 35;
     
    private List<ObservationData> obsList;
    private List<TaxanData> taxonList;
    
    
    private List<String> uniqeTaxonNames;
    
    private boolean openObservation;
    private boolean openTaxonList;
      
    private boolean isMetadataValid;
    private boolean isObservationValid;
    private boolean isTaxonListValid;
     
    private String selectedAgent;
    
    private Map<String, Integer> agents;
    private Date submittedDate;
    private final String maxDate;
      
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
    private String firstName;
    private String lastName;
    private String notes;
    
    private String loanNumber;
    private String author;
    private String guid;
    private String source;
    private String computedName;
    
    private Map<String, Integer> usedTaxonMap;
//    private List<UsedTaxon> usedTaxons;
    
    public FileUploader() {
        maxDate = Util.getNow();
        initData();
        selectedAgent = null;
    }
 
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
        usedTaxonMap = new HashMap<>();
        
        taxonTitleMap = new HashMap<>();
        taxonList = new ArrayList<>();
        uniqeTaxonNames = new ArrayList<>();
          
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
            saveTempFile();                                                         // save excel file in temp file directory
            buildWorkbook();                                                        // build workbook from excel file      
            validateSheets();                                                       // validate sheets (to verify all the sheets are there)
            readInLoanNumber(); 
            
            obsTitleMap = readInSheetTitles(workbook.getSheetAt(1), MAX_COLUMN_NUMBER); 
            isFileUpload = true;
        } catch(InvalidExcelFileException e) { 
            isFileUpload = false;
            msgBean.addError("Upload file failure", e.getMessage());  
        }   
    }
    

    
    public void openObservationPage() { 
        if(agents.isEmpty()) {
            agents = client.getAgents();                                               // get agent list
        }
        
        if(openObservation && obsList.isEmpty()) {  
            obsList = readInObservation();  
            
            uniqeTaxonNames = obsList.stream()
                                     .map(o -> o.getComputedName())
                                     .distinct()   
                                     .collect(Collectors.toList());
            isObservationValid = true;
        }  
        navigate.observationPage();
    } 
    
    /**
     * taxonListAction
     */
    public void taxonListAction() {
        logger.info("taxonListAction");
        openTaxonList = !openTaxonList;
        isTaxonListValid = false;
         
        if (openTaxonList && taxonList.isEmpty()) {
            TaxonVO vo = new TaxonVO(uniqeTaxonNames);
            usedTaxonMap = client.validateUsedTaxon(vo); 
            setTaxonIdIntoObj();
            
            Sheet sheet = workbook.getSheetAt(4);                                           // Taxonlist sheet

            taxonTitleMap = readInSheetTitles(sheet, MAX_COLUMN_NUMBER);                    // get all the column titles 
            try {
                taxonList = readInTaxon(sheet);
                isTaxonListValid = true; 
            } catch (InvalidExcelFileException e) {
                msgBean.addError(CommonMessages.getInstance().INVALID_EXCEL_FILE, e.getMessage());
            }   
        }
    }
    
    private void setTaxonIdIntoObj() {
        obsList.stream()
                .forEach(o -> {  
                    int taxonId = usedTaxonMap.get(o.getComputedName());
                    o.setTaxonId(taxonId);
                    if(o.getComputedName().trim().equals("Thecophora sp 1")) {
                        o.setDeterminationRemarks("sp 1");
                    } else if(o.getComputedName().trim().equals("Omphale sp in aetius-group")) {
                        o.setDeterminationRemarks("sp in aetius-group");
                    } else if(o.getComputedName().trim().equals("Formica sp 1")) {
                        o.setDeterminationRemarks("sp 1");
                    } else if(o.getComputedName().trim().equals("Helina sp 1")) {
                        o.setDeterminationRemarks("sp 1");
                    } else if(o.getComputedName().trim().equals("Helina sp 2")) {
                        o.setDeterminationRemarks("sp 2");
                    } else if(o.getComputedName().trim().equals("Phaonia sp 1")) {
                        o.setDeterminationRemarks("sp 1");
                    } else if(o.getComputedName().trim().equals("Halictophagus sp 1")) {
                        o.setDeterminationRemarks("sp 1");
                    } else if(o.getComputedName().trim().equals("Allantus sp 1")) {
                        o.setDeterminationRemarks("sp 1");
                    }
                });
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
     * Remove uploaded file
     */
    public void removefile() {
        logger.info("removefile");

        if (fileName != null) {
            File thisFile = new File(LOCAL_TEMP_FILE_DIRECTORY, fileName);
            if (thisFile.exists()) {
                thisFile.delete();
            }
        }
        fileName = null;
        workbook = null;
    }

    public void uploadData() {
        logger.info("uploadData");
          
        ExcelData data = new ExcelData(fileName, getSelectedAgentId(), Util.dateToString(submittedDate), loanNumber, obsList);
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
      
    private List<TaxanData> readInTaxon(Sheet sheet) { 
        logger.info("used taxon list : {}", usedTaxonMap);
        
        int index = taxonTitleMap.get("Taxon name (computed)");  
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
    
        
    private TaxanData buildTaxaData(Row row) {
         
        Cell cell = row.getCell(taxonTitleMap.get("Genus"));
        genus = cell.getStringCellValue().trim();
    
        cell = row.getCell(taxonTitleMap.get("Species"));
        species = cell.getStringCellValue().trim();
  
        cell = row.getCell(taxonTitleMap.get("Author"));
        author = cell == null ? "" : cell.getStringCellValue().trim();
         
        cell = row.getCell(taxonTitleMap.get("GUID"));
        guid = cell == null ? "" : cell.getStringCellValue().trim();
  
        cell = row.getCell(taxonTitleMap.get("Source"));
        source = cell == null ? "" : cell.getStringCellValue().trim();
 
        if(taxonTitleMap.containsKey("Notes (errors, synonyms, undescribed etc)")) {
            cell = row.getCell(taxonTitleMap.get("Notes (errors, synonyms, undescribed etc)")); 
        } else if(taxonTitleMap.containsKey("Notes")) {
            cell = row.getCell(taxonTitleMap.get("Notes")); 
        } else {
            cell = null;
        }
        notes = cell == null ? "" : cell.getStringCellValue().trim();
         
        cell = row.getCell(taxonTitleMap.get("Taxon name (computed)"));
        computedName = cell.getStringCellValue().trim(); 
        
        String remarks = "";
        if(computedName.equals("Thecophora sp 1")) {
            remarks = "sp 1";
        }
        return new TaxanData(usedTaxonMap.get(computedName), genus, species, computedName, guid, author, notes, source, remarks);
    }
    
    private Predicate<Row> isInList(int index) {     
        return r ->  r.getCell(index) != null && uniqeTaxonNames.contains(getCellValue(r, index));
    }
    
    private String getCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        String value;
        try {
            value = cell.getCellType() == Cell.CELL_TYPE_NUMERIC ? 
                            String.valueOf(cell.getNumericCellValue()) : cell.getStringCellValue();
        } catch(java.lang.IllegalStateException e) { 
            value = row.getCell(0) + " " + row.getCell(1);
        } 
        return value;
    }
  
    private List<ObservationData> readInObservation() { 
        Sheet sheet = workbook.getSheetAt(1);
        
        media = "";
        storage = "";      
        
        return IntStream.range(1, sheet.getLastRowNum() + 1)
                .mapToObj(i -> sheet.getRow(i))
                .filter(isNotEmpty())
                .map(this::buildCollectionObjData)
                .collect(Collectors.toList());
    }
 
    private ObservationData buildCollectionObjData(Row row) {
 
        Cell cell = row.getCell(obsTitleMap.get("Genus"));
        genus = cell == null ? "" : cell.getStringCellValue();
         
        cell = row.getCell(obsTitleMap.get("Species"));
        species = cell == null ? "" : cell.getStringCellValue(); 
         
        if(obsTitleMap.containsKey("Determinator")) {
            cell = row.getCell(obsTitleMap.get("Determinator"));
        } else if(obsTitleMap.containsKey("Determined by")) {
            cell = row.getCell(obsTitleMap.get("Determined by"));
        } else {
            cell = null;
        }  
        determiner = cell == null ? "" : cell.getStringCellValue();
        resolveDeterminerName();
          
        if(obsTitleMap.containsKey("Det. date")) {
            cell = row.getCell(obsTitleMap.get("Det. date"));
        } else if(obsTitleMap.containsKey("Det. Date")) {
            cell = row.getCell(obsTitleMap.get("Det. Date"));
        } else {
            cell = null;
        }  
        getDeterminedDate(cell); 
         
        cell = row.getCell(obsTitleMap.get("EventID"));
        eventId = getCellIntValue(cell);
          
        
        cell = row.getCell(obsTitleMap.get("Males")); 
        numOfMales = cell == null ? 0 : getCellIntValue(cell);
        
        cell = row.getCell(obsTitleMap.get("Females")); 
        numOfFemales = cell == null ? 0 : getCellIntValue(cell);
    
        cell = row.getCell(obsTitleMap.get("Total"));
        total = cell == null ? 0 : getCellIntValue(cell); 
        
        getStorage(cell, row);
        
        cell = row.getCell(obsTitleMap.get("Media")); 
        media = cell == null ? media : cell.getStringCellValue();
        
        if(obsTitleMap.containsKey("Notes")) {
            cell = row.getCell(obsTitleMap.get("Notes")); 
            notes = cell == null ? "" : cell.getCellType() == Cell.CELL_TYPE_NUMERIC ? 
                            String.valueOf(cell.getNumericCellValue()) : cell.getStringCellValue();
        }
         
        return new ObservationData(eventId, genus, species, determiner, firstName, lastName,
                                    determinedDate, storage, media, notes, numOfMales, numOfFemales, total);
    }
    

    
    private void getStorage(Cell cell, Row row) {
        if(obsTitleMap.containsKey("Placed/Stored")) {
            cell = row.getCell(obsTitleMap.get("Placed/Stored")); 
        } else if(obsTitleMap.containsKey("Stored")) {
            cell = row.getCell(obsTitleMap.get("Stored")); 
        } else if(obsTitleMap.containsKey("Storage")) {
            cell = row.getCell(obsTitleMap.get("Storage")); 
        } else {
            cell = null;
        }
        storage = cell == null ? storage : cell.getStringCellValue(); 
        if(storage.endsWith(", Bengt Andersson")) {
            storage = StringUtils.substringBefore(storage, ", Bengt Andersson");
        }
    }
 
    private int getCellIntValue(Cell c) {
        try {
            return c == null ? 0 : c.getCellType() == Cell.CELL_TYPE_STRING
                                    ? Integer.parseInt(c.getStringCellValue())
                                    : (int) c.getNumericCellValue();
        } catch (NumberFormatException e) {
            return 0;
        } 
    }

    private void getDeterminedDate(Cell cell) {
        determinedDate = "";
        if (cell != null) {
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                determinedDate = cell.getStringCellValue();
                if (determinedDate.contains("/")) {                                      // convert year/month/day to year-month-day
                    determinedDate = StringUtils.replace(determinedDate, "/", "-");
                } else if(determinedDate.contains(".")) {
                    determinedDate = determinedDate.replace(".", "-");
                } if(determinedDate.length() == 4) {
                    determinedDate = determinedDate.concat("-01-01");
                }
                
                if(determinedDate.contains("xi")) {
                    determinedDate = determinedDate.replace("xi", "06");
                } else if(determinedDate.contains("X-2013")) {
                    determinedDate = "2013-10-01";  
                } else if(determinedDate.contains("III")) {
                    determinedDate = determinedDate.replace("III", "03");
                }
            } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                String dateInt = String.valueOf((int) cell.getNumericCellValue());
                if (dateInt.length() > 4) {
                    dateInt = dateInt.substring(0, 4);                                  // get first four digits as year
                }
                determinedDate = dateInt.concat("-01-01");
            } 
        }

    }

    private Predicate<Row> isNotEmpty() { 
        return r -> r != null && r.getCell(0) != null && !r.getCell(0).getStringCellValue().isEmpty();
    }
    
    public Map<String, Integer> readInSheetTitles(Sheet sheet, int maxNumColumn ) {
        Map<String, Integer> titleMap = new HashMap<>();
        Row row = sheet.getRow(0);
        IntStream.range(0, maxNumColumn)
                .filter(i -> row.getCell(i) != null)
                .mapToObj(i -> row.getCell(i))
                .forEach(c -> {
                    titleMap.put(c.getStringCellValue(), c.getColumnIndex());
                }); 
        return titleMap;
    }
  
    private void validateSheets() {
        logger.info("validateSheets");
        if (!workbook.getSheetName(0).equals("Metadata")) {
            throw new InvalidExcelFileException(CommonMessages.getInstance().MISSING_METADAT_SHEET); 
        }
        if (!workbook.getSheetName(1).equals("Observation")) {
            throw new InvalidExcelFileException(CommonMessages.getInstance().MISSING_OBSERVATION_SHEET);  
        }  
        if (!workbook.getSheetName(4).equals("TaxonList")) {
            throw new InvalidExcelFileException(CommonMessages.getInstance().MISSING_TAXONLIST_SHEET);  
        } 
    }
      
    private void buildWorkbook() {
        
        logger.info("buildWorkbook");
        
        File theFile = new File(LOCAL_TEMP_FILE_DIRECTORY, fileName); 
        try { 
            workbook = WorkbookFactory.create(theFile); 
        } catch (IOException | InvalidFormatException | RecordFormatException ex) { 
            throw new InvalidExcelFileException(ex.getMessage());
        }
    }
     
    private String saveTempFile() {
        logger.info("saveTempFile " );

        File targetFile;
        fileName = file.getFileName();
        try (InputStream initialStream = file.getInputstream()) {
            byte[] buffer = new byte[initialStream.available()];
            initialStream.read(buffer);

            targetFile = new File(LOCAL_TEMP_FILE_DIRECTORY, fileName);
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
    
    private void readInLoanNumber() {
        logger.info("readInLoanNumber");
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(4);
        Cell cell = row.getCell(0);
        if(cell != null) { 
            loanNumber = cell.getCellType() == Cell.CELL_TYPE_NUMERIC ? cell.getNumericCellValue() + "" : cell.getStringCellValue();
            if(loanNumber == null || loanNumber.isEmpty()) {
                throw new InvalidExcelFileException(CommonMessages.getInstance().INVALID_METADATA_MISSING_LOAN_NUMBER); 
            } 
        } else { 
            throw new InvalidExcelFileException(CommonMessages.getInstance().INVALID_METADATA_MISSING_LOAN_NUMBER); 
        } 
    }
 
    
    private void resolveDeterminerName() {
        switch (determiner) {
            case "M Forshage":
                firstName = "Mattias";
                lastName = "Forshage";
                break;
            case "Bo W Svensson":
                firstName = "Bo";
                lastName = "Svensson";
                break;              
            case "Mårtem Eriksson":
                firstName = "Mårten";
                lastName = "Eriksson";
                break;
            case "Jan Willem A van Zuijlen":
                firstName = "Jan";
                lastName = "van Zuijlen";
                break;
            case "Mareike Kiupel, 2014":
                firstName = "Mareike";
                lastName = "Kiupel";
                break;
            case "R. Hovmöller":
                firstName = "Rasmus";
                lastName = "Hovmöller";
                break;
            case "KA Johanson":
                firstName = "Kjell Arne";
                lastName = "Johanson";
                break;
            case "J. Kjærandsen":
                firstName = "Jostein";
                lastName = "Kjærandsen";
                break;
            case "SO Uiefors": 
                firstName = "Sven-Olof";
                lastName = "Ulefors";
                break;
            case "SO Ulefors": 
                firstName = "Sven-Olof";
                lastName = "Ulefors";
                break;
            case "S. Martinsson":
                firstName = "Svante";
                lastName = "Martinsson";
                break; 
            case "Svante Matinsson":
                firstName = "Svante";
                lastName = "Martinsson";
                break; 
            case "J.T. Smit":
                firstName = "John";
                lastName = "Smit";
                break; 
            case "Viktor Nilsson":
                firstName = "Viktor";
                lastName = "Nilsson-Örtman";
                break;
            case "A. Liston":
                firstName = "Andrew";
                lastName = "Liston";
                break;
            case "Lozan":
                firstName = "Aurel";
                lastName ="Lozan"; 
                break;
            case "Mathias Riedel":
                firstName = "Matthias";
                lastName ="Riedel"; 
                break;
            case "Mathias Jaschhof":
                firstName = "Matthias";
                lastName ="Jaschhof"; 
                break; 
            case "William Englund":
                firstName = "William";
                lastName ="Englung";
                break; 
            case "Emilia P. Narchuk":
                firstName = "Emilia";
                lastName ="Nartshuk"; 
                break;
            case "Fusu Lucian":
                firstName = "Lucian";
                lastName ="Fusu"; 
                break;
            case "Oleksandr (Sasha) Varga": 
                firstName = "Oleksandr (Sasha)";
                lastName ="Varga"; 
                break;
            default:
                getName();
                break;
        }  
    }
    
    private void getName() {
        firstName = "";
        lastName = "";
        firstName = determiner.split(" ")[0];
        if (determiner.contains(" ")) {
            int index = determiner.indexOf(" ");
            lastName = determiner.substring(index + 1);
        }
    }

     
    
    /**
     * Accumulator class to help accumulator long in foreach statement
     * @return 
     */
//    public static class Accumulator {
//
//        private int total = 0;
//          
//        public void increment() {
//            total++;
//        } 
//    } 
     
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

    public List<TaxanData> getTaxonList() {
        return taxonList;
    }

    public void setTaxonList(List<TaxanData> taxonList) {
        this.taxonList = taxonList;
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

    public String getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(String loanNumber) {
        this.loanNumber = loanNumber;
    }
    
    
}
