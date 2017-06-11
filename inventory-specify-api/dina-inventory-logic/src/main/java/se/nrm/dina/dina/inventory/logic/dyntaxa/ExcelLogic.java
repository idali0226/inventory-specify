/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dina.inventory.logic.dyntaxa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.data.jpa.SMTPDao;
import se.nrm.dina.datamodel.impl.Agent;
import se.nrm.dina.datamodel.impl.Taxon;
import se.nrm.dina.datamodel.impl.Taxontreedef;
import se.nrm.dina.datamodel.impl.Taxontreedefitem;
import se.nrm.dina.dina.inventory.logic.util.QueryStringBuilder;
import se.nrm.dina.dina.inventory.logic.util.Util;

/**
 *
 * @author idali
 */
@Stateless
public class ExcelLogic implements Serializable {

 private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String EXCEL_FILE_PATH = "/Users/idali/Desktop/New_smtp_taxa_2017-06-07.xlsx";
    private static final String NAMED_QUERY_GET_AGENT_BY_NAME = "Agent.findByAgentName";

    private final int TAXON_TREE_DEF_ID = 11;
    private Timestamp timestamp;

    private Taxontreedef taxonTreeDef;
    private Taxontreedefitem item;

    private String genus;
    private String species;
    private String author;
    private String scientificName;
    private String source;
    private String parent;
    private String rankOfParent;
    private String agentName;
    private String strIsManuscript;
    private String comment;
    private String theRank;

    private String synonymOf;

    @EJB
    private SMTPDao smtpDao;

    public ExcelLogic() {

    }

    private void uploadNewHighTaxa(XSSFSheet sheet) {
         
        item = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, Util.getInstance().getRankId("Genus")));

        int numOfRows = sheet.getLastRowNum();
        IntStream.range(3, numOfRows + 1)
                .forEach(nbr -> {
                    uploadHighTaxon(sheet.getRow(nbr));
                });
    }

    private void uploadHighTaxon(XSSFRow row) {
        scientificName = row.getCell(0).getStringCellValue();
 
        Taxon theTaxon = new Taxon();
        theTaxon.setAuthor(row.getCell(2).getStringCellValue().trim());
        theTaxon.setCreatedByAgentID(getAgentFromDB(row.getCell(5).getStringCellValue().trim()));
        theTaxon.setTimestampCreated(timestamp);
        theTaxon.setRankID(Util.getInstance().getRankId(row.getCell(1).getStringCellValue()));
        theTaxon.setIsAccepted(Boolean.TRUE);
        theTaxon.setFullName(scientificName);
        theTaxon.setName(scientificName);
        theTaxon.setGuid(Util.getInstance().generateGUID().toString());
        theTaxon.setSource(row.getCell(4).getStringCellValue());
        theTaxon.setParentID(getParentTaxon(row.getCell(3).getStringCellValue().trim(), Util.getInstance().getRankId("Family")));
        theTaxon.setYesNo1(isManuscript(row.getCell(7).getStringCellValue()));
        theTaxon.setTaxonTreeDefItemID(item);
        theTaxon.setTaxonTreeDefID(taxonTreeDef);
        theTaxon.setIsHybrid(Boolean.FALSE);
        theTaxon = (Taxon) smtpDao.create(theTaxon);
        logger.info("the saved taxon : {}", theTaxon);
    }

    private boolean isManuscript(String strIsManuscript) {
        return strIsManuscript.toLowerCase().trim().equals("yes");
    }
 
    private Agent getAgentFromDB(String agentName) {
        
        logger.info("agent name : {}", agentName);
        String lastName = StringUtils.substringAfterLast(agentName, " "); 
        String firstName = StringUtils.substringBeforeLast(agentName, " ");
       
        if(firstName.equals("Iain")) {
            firstName = "Lain"; 
        } else if(agentName.equals("Peter Neerup Buhl")) {
            firstName = "Peter";
            lastName = "Neerup Buhl";
        }
         
        Map<String, String> map = new HashMap();
        map.put("firstName", firstName);
        map.put("lastName", lastName);

        return (Agent) smtpDao.getEntityByNamedQuery(NAMED_QUERY_GET_AGENT_BY_NAME, map);
    }
 
    private void uploadNewSpecies(XSSFSheet sheet) {

        int rankId = Util.getInstance().getRankId("Species");
        item = (Taxontreedefitem) smtpDao.getEntityByJPQL(QueryStringBuilder.getInstance()
                .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, rankId));

        int numOfRows = sheet.getLastRowNum() + 1; 
        IntStream.range(3, numOfRows)
                .filter(n -> sheet.getRow(n) != null)  
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    if (row.getCell(0) != null && !row.getCell(0).getStringCellValue().isEmpty()) {
                        genus = row.getCell(0).getStringCellValue();
                        species = row.getCell(1).getStringCellValue();

                        if (row.getCell(2) != null) {
                            author = row.getCell(2).getStringCellValue();
                        }

                        scientificName = row.getCell(3).getStringCellValue();
                        source = row.getCell(4).getStringCellValue();
                        parent = row.getCell(5).getStringCellValue();
                        rankOfParent = row.getCell(6).getStringCellValue();
                        agentName = row.getCell(7).getStringCellValue();
                        strIsManuscript = row.getCell(9).getStringCellValue();

                        if (row.getCell(10) != null) {
                            comment = row.getCell(10).getStringCellValue();
                        }

                        uploadTaxon(rankId);
                    }

                });
    }

    private void uploadTaxon(int rankId) {

        Taxon theTaxon = new Taxon();
        theTaxon.setAuthor(author);
        theTaxon.setCreatedByAgentID(getAgentFromDB(agentName));
        theTaxon.setTimestampCreated(timestamp);
        theTaxon.setRankID(rankId);
        theTaxon.setIsAccepted(Boolean.TRUE);
        theTaxon.setIsHybrid(Boolean.FALSE);
        theTaxon.setFullName(scientificName);
        theTaxon.setName(species);
        theTaxon.setGuid(Util.getInstance().generateGUID().toString());
        theTaxon.setSource(source);
        theTaxon.setParentID(getParentTaxon(parent, Util.getInstance().getRankId(rankOfParent)));
        theTaxon.setYesNo1(isManuscript(strIsManuscript));
        theTaxon.setTaxonTreeDefItemID(item);
        theTaxon.setTaxonTreeDefID(taxonTreeDef);
        theTaxon.setRemarks(comment);
        smtpDao.create(theTaxon);  
    }
    
    private Taxon getParentTaxon(String fullName, int rankId) {
        return (Taxon) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonParent(fullName, rankId, TAXON_TREE_DEF_ID));
    }
     
    private Taxon getTaxon(String fullName, boolean isAccepted) {
        return (Taxon) smtpDao.getEntityByJPQL(QueryStringBuilder.getInstance()
                              .buildGetTaxon(fullName, TAXON_TREE_DEF_ID, isAccepted));
    }
     
        
    private void uploadSynonyms() {
        
        item = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, Util.getInstance().getRankId(theRank)));

         
        Taxon synomyOfTaxon = getTaxon(synonymOf, false);  
        if(synomyOfTaxon != null) {
            Taxon theTaxon = new Taxon();
            theTaxon.setAuthor(author);
            theTaxon.setCreatedByAgentID(getAgentFromDB(agentName));
            theTaxon.setTimestampCreated(timestamp);
            theTaxon.setRankID(Util.getInstance().getRankId(theRank));
            theTaxon.setIsAccepted(Boolean.FALSE);
            theTaxon.setIsHybrid(Boolean.FALSE);
            theTaxon.setFullName(scientificName);
            theTaxon.setName(species);
            theTaxon.setGuid(Util.getInstance().generateGUID().toString());
            theTaxon.setSource(source);
             
            theTaxon.setParentID(synomyOfTaxon.getParentID()); 
            theTaxon.setTaxonTreeDefItemID(item);
            
            if(synomyOfTaxon.getIsAccepted()) {
                theTaxon.setAcceptedID(synomyOfTaxon); 
            } else {
                theTaxon.setAcceptedID(synomyOfTaxon.getAcceptedID());
            }
            
            theTaxon.setTaxonTreeDefID(taxonTreeDef);
            theTaxon.setRemarks(comment);
            theTaxon.setYesNo1(Boolean.FALSE);
            
            theTaxon = (Taxon) smtpDao.create(theTaxon); 
            logger.info("the saved taxon : {}", theTaxon);
        } else {
            logger.error("synonymOfTaxon not found: {}", synonymOf);
        } 
    }
    
    

    
    private void uploadNewSynonyms(XSSFSheet sheet) {  
        
        int numOfRows = sheet.getLastRowNum();
        
        theRank = "Species";
        IntStream.range(4, numOfRows + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    if(row.getCell(0) != null) {
                        genus = row.getCell(0).getStringCellValue().trim();
                        species = row.getCell(1).getStringCellValue().trim();
                        author = row.getCell(2).getStringCellValue().trim();
                        scientificName = row.getCell(5).getStringCellValue().trim(); 
                        synonymOf = row.getCell(6).getStringCellValue().trim(); 
                        source = row.getCell(7).getStringCellValue(); 
                        if(source.length() > 64) {
                            source = StringUtils.substring(source, 0, 63);
                        }
                        agentName = row.getCell(8).getStringCellValue();  
                        uploadSynonyms();
                    }
                });
    }
    
    private void addOldHigherTaxonSynonyms(XSSFSheet sheet) {
        logger.info("addOldHigherTaxonSynonyms");
        
        XSSFRow row = sheet.getRow(3);
        
        genus = row.getCell(0).getStringCellValue().trim();  
        scientificName = genus;
        species = genus;
        author = row.getCell(2).getStringCellValue().trim();
        synonymOf = row.getCell(3).getStringCellValue().trim();
        agentName = "Ida Li";
        theRank = "Genus"; 
         
        uploadSynonyms();
    }
    
    public void oldSpeciesSynonyms(XSSFSheet sheet) {
        logger.info("oldSpeciesSynonyms");
        
        theRank = "Species";
        int numOfRows = sheet.getLastRowNum();
        IntStream.range(3, numOfRows + 1)
                .filter(n -> sheet.getRow(n) != null)  
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    if(row.getCell(0) != null) {
                        genus = row.getCell(0).getStringCellValue().trim();
                        species = row.getCell(1).getStringCellValue().trim();
                        author = row.getCell(2).getStringCellValue().trim();
                        scientificName = row.getCell(5).getStringCellValue().trim(); 
                        synonymOf = row.getCell(6).getStringCellValue().trim();
                        source = row.getCell(7).getStringCellValue().trim();
                        agentName = row.getCell(8).getStringCellValue().trim();
 
                        uploadSynonyms();
                    }
                });
    }

    
    private void collectionsToDyntaxa(XSSFSheet sheet) {
        logger.info("collectionsToDyntaxa");
         
        int rankId = Util.getInstance().getRankId("Species");
        item = (Taxontreedefitem) smtpDao.getEntityByJPQL(QueryStringBuilder.getInstance()
                                            .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, rankId));
        IntStream.range(15, 22)
                .filter(n -> sheet.getRow(n) != null)  
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    if(row.getCell(0) != null) {
                        genus = row.getCell(0).getStringCellValue().trim();
                        species = row.getCell(1).getStringCellValue().trim();
                        author = row.getCell(2).getStringCellValue().trim();
                        scientificName = row.getCell(3).getStringCellValue().trim();   
                        source = row.getCell(4).getStringCellValue().trim();   
                        agentName = row.getCell(5).getStringCellValue().trim();   
                        comment = row.getCell(6).getStringCellValue().trim();   
                        parent = genus; 
                        rankOfParent = "Genus";  
                        strIsManuscript = "no";
                        uploadTaxon(rankId); 
                    } 
                });
        
        IntStream.range(26, 32)
                .filter(n -> sheet.getRow(n) != null)  
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    if(row.getCell(0) != null) { 
                        scientificName = row.getCell(3).getStringCellValue().trim();     
                        comment = row.getCell(6).getStringCellValue().trim();    
                        rankOfParent = "Genus";    
                        synonymOf = row.getCell(4).getStringCellValue().trim();   
                        agentName = "Ida Li";
                         
                        Taxon synonymOfTaxon = getTaxon(synonymOf, true);
                        Taxon theTaxon = getTaxon(scientificName, false);
                        
                        theTaxon.setIsAccepted(Boolean.FALSE);
                        theTaxon.setRemarks(comment);
                        theTaxon.setAcceptedID(synonymOfTaxon);
                        theTaxon.setModifiedByAgentID(getAgentFromDB(agentName));
                        theTaxon.setTimestampModified(timestamp);
                         
                        smtpDao.merge(theTaxon);
                    } 
                }); 
    }
     
    public void readInExcelFile(String action) {
        
        logger.info("readInExcelFile : {}", action);
       
        timestamp = new Timestamp(System.currentTimeMillis());
        taxonTreeDef = (Taxontreedef) smtpDao.findByReference(TAXON_TREE_DEF_ID, Taxontreedef.class);
        
        try (FileInputStream file = new FileInputStream(new File(EXCEL_FILE_PATH))) {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
             
            switch (action) {
                case "correctionsToDyntaxa":         
                    collectionsToDyntaxa(workbook.getSheetAt(0));               // done corrections to DynTaxa
                    break;
                case "highTaxa":                                                // done new higher taxa to add   // this need to run before sheet 3
                    uploadNewHighTaxa(workbook.getSheetAt(4));
                    break; 
                case "oldSynonyms":
                    oldSpeciesSynonyms(workbook.getSheetAt(1));                 // done Old species synonyms to add
                    break;
                case "oldHigherTaxonSynonyms":                                  // done old higher taxon synonym to add
                    addOldHigherTaxonSynonyms(workbook.getSheetAt(2));
                    break;
                case "newTaxa":                                                 // done new species to add
                    uploadNewSpecies(workbook.getSheetAt(3));
                    break;  
                case "newSynonyms":                                             // new species synonyms to add
                    uploadNewSynonyms(workbook.getSheetAt(5));
                    break; 
                default:
                    break;
            }
             
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        } 
    }  
    
    
  //    private void uploadSynonyms1() {
//        
//        item = (Taxontreedefitem) smtpDao.getEntityByJPQL(
//                QueryStringBuilder.getInstance()
//                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, Util.getInstance().getRankId(theRank)));
//
//         
//        Taxon synomyOfTaxon = (Taxon) smtpDao.getEntityByJPQL( 
//                                    QueryStringBuilder.getInstance()
//                                            .buildGetTaxon(synonymOf, TAXON_TREE_DEF_ID, false));
//       
//        if(synomyOfTaxon != null) {
//            Taxon theTaxon = new Taxon();
//            theTaxon.setAuthor(author);
//            theTaxon.setCreatedByAgentID(getAgentFromDB(agentName));
//            theTaxon.setTimestampCreated(timestamp);
//            theTaxon.setRankID(Util.getInstance().getRankId(theRank));
//            theTaxon.setIsAccepted(Boolean.FALSE);
//            theTaxon.setIsHybrid(Boolean.FALSE);
//            theTaxon.setFullName(scientificName);
//            theTaxon.setName(species);
//            theTaxon.setGuid(Util.getInstance().generateGUID().toString());
//            theTaxon.setSource(source);
//            theTaxon.setParentID(synomyOfTaxon.getParentID()); 
//            theTaxon.setTaxonTreeDefItemID(item);
//            theTaxon.setAcceptedID(synomyOfTaxon);
//            theTaxon.setTaxonTreeDefID(taxonTreeDef);
//            theTaxon.setRemarks(comment);
//            theTaxon = (Taxon) smtpDao.create(theTaxon);
//            theTaxon.setYesNo1(Boolean.FALSE);
//            logger.info("the saved taxon : {}", theTaxon);
//        } else {
//            logger.error("synonymOfTaxon not fild: {}", synonymOf);
//        }
//        
//    }  
    
    //    private String getLastName(String name) {
//        if (name.contains(" ")) {
//            int index = name.indexOf(" ");
//            return name.substring(index + 1);
//        }
//        return name;
//    }
    
    
    
}
