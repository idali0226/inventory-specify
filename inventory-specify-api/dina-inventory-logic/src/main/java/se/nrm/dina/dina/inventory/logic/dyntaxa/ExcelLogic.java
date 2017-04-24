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

    private static final String EXCEL_FILE_PATH = "/Users/idali/Desktop/new_smtp_taxa_2017-04-11.xlsx";
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
        theTaxon.setAuthor(row.getCell(2).getStringCellValue());
        theTaxon.setCreatedByAgentID(getAgentFromDB(row.getCell(5).getStringCellValue()));
        theTaxon.setTimestampCreated(timestamp);
        theTaxon.setRankID(Util.getInstance().getRankId(row.getCell(1).getStringCellValue()));
        theTaxon.setIsAccepted(Boolean.TRUE);
        theTaxon.setFullName(scientificName);
        theTaxon.setName(scientificName);
        theTaxon.setGuid(Util.getInstance().generateGUID().toString());
        theTaxon.setSource(row.getCell(4).getStringCellValue());
        theTaxon.setParentID(getTaxon(row.getCell(3).getStringCellValue(), Util.getInstance().getRankId("Family")));
        theTaxon.setYesNo1(isManuscript(row.getCell(7).getStringCellValue()));
        theTaxon.setTaxonTreeDefItemID(item);
        theTaxon.setTaxonTreeDefID(taxonTreeDef);
        theTaxon = (Taxon) smtpDao.create(theTaxon);
        logger.info("the saved taxon : {}", theTaxon);
    }

    private boolean isManuscript(String strIsManuscript) {
        return strIsManuscript.toLowerCase().trim().equals("yes");
    }

    private Taxon getTaxon(String fullName, int rankId) {
        return (Taxon) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonParent(fullName, rankId, TAXON_TREE_DEF_ID));
    }

    private Agent getAgentFromDB(String agentName) {
        String firstName = agentName.split(" ")[0];
        String lastName = getLastName(agentName);

        Map<String, String> map = new HashMap();
        map.put("firstName", firstName);
        map.put("lastName", lastName);

        return (Agent) smtpDao.getEntityByNamedQuery(NAMED_QUERY_GET_AGENT_BY_NAME, map);
    }

    private String getLastName(String name) {
        if (name.contains(" ")) {
            int index = name.indexOf(" ");
            return name.substring(index + 1);
        }
        return name;
    }

    private void uploadNewSpecies(XSSFSheet sheet) {

        int rankId = Util.getInstance().getRankId("Species");
        item = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, rankId));

        int numOfRows = sheet.getLastRowNum();
        IntStream.range(3, numOfRows)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    if (row != null) {
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
        theTaxon.setFullName(scientificName);
        theTaxon.setName(species);
        theTaxon.setGuid(Util.getInstance().generateGUID().toString());
        theTaxon.setSource(source);
        theTaxon.setParentID(getTaxon(parent, Util.getInstance().getRankId(rankOfParent)));
        theTaxon.setYesNo1(isManuscript(strIsManuscript));
        theTaxon.setTaxonTreeDefItemID(item);
        theTaxon.setTaxonTreeDefID(taxonTreeDef);
        theTaxon.setRemarks(comment);
        theTaxon = (Taxon) smtpDao.create(theTaxon);
        logger.info("the saved taxon : {}", theTaxon);
    }
    
    private void uploadSynonyms() {
         
        Taxon synomyOfTaxon = (Taxon) smtpDao.getEntityByJPQL( 
                                    QueryStringBuilder.getInstance()
                                            .buildGetTaxon(synonymOf));
       
        if(synomyOfTaxon != null) {
            Taxon theTaxon = new Taxon();
            theTaxon.setAuthor(author);
            theTaxon.setCreatedByAgentID(getAgentFromDB(agentName));
            theTaxon.setTimestampCreated(timestamp);
            theTaxon.setRankID(synomyOfTaxon.getRankID());
            theTaxon.setIsAccepted(Boolean.FALSE);
            theTaxon.setFullName(scientificName);
            theTaxon.setName(species);
            theTaxon.setGuid(Util.getInstance().generateGUID().toString());
            theTaxon.setSource(source);
            theTaxon.setParentID(synomyOfTaxon.getParentID()); 
            theTaxon.setTaxonTreeDefItemID(synomyOfTaxon.getTaxonTreeDefItemID());
            theTaxon.setAcceptedID(synomyOfTaxon);
            theTaxon.setTaxonTreeDefID(taxonTreeDef);
            theTaxon.setRemarks(comment);
            theTaxon.setYesNo1(false);
            theTaxon = (Taxon) smtpDao.create(theTaxon);
     
            logger.info("the saved taxon : {}", theTaxon);
        } else {
            logger.error("synonymOfTaxon not fild: {}", synonymOf);
        }
        
    }
    
    private void uploadNewSynonyms(XSSFSheet sheet) {  
        
        int numOfRows = sheet.getLastRowNum();
        IntStream.range(4, numOfRows + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    if(row.getCell(0) != null) {
                        genus = row.getCell(0).getStringCellValue();
                        species = row.getCell(1).getStringCellValue();
                        author = row.getCell(2).getStringCellValue();
                        scientificName = row.getCell(5).getStringCellValue(); 
                        synonymOf = row.getCell(6).getStringCellValue();
                        
                        source = row.getCell(7).getStringCellValue(); 
                        if(source.length() > 64) {
                            source = StringUtils.substring(source, 0, 64);
                        }
                        agentName = row.getCell(8).getStringCellValue(); 
                        if(row.getCell(10) != null) {
                            comment = row.getCell(10).getStringCellValue();
                        } 
                        uploadSynonyms();
                    }
                });
    }
    
    public void readInExcelFile(String action) {
         
        timestamp = new Timestamp(System.currentTimeMillis());
        taxonTreeDef = (Taxontreedef) smtpDao.findByReference(TAXON_TREE_DEF_ID, Taxontreedef.class);
        
        try (FileInputStream file = new FileInputStream(new File(EXCEL_FILE_PATH))) {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
             
            switch (action) {
                case "highTaxa":
                    uploadNewHighTaxa(workbook.getSheetAt(6));
                    break;
                case "newTaxa":
                    uploadNewSpecies(workbook.getSheetAt(5));
                    break;
                case "newSynonyms":
                    uploadNewSynonyms(workbook.getSheetAt(4));
                    break;
                default:
                    break;
            }
             
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    } 
}
