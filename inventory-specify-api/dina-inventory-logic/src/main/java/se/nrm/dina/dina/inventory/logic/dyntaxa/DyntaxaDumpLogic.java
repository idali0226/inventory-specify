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
import java.util.ArrayList; 
import java.util.List;
import java.util.Arrays;
import java.util.stream.IntStream;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
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

/**
 *
 * @author idali
 */
@Stateless
public class DyntaxaDumpLogic implements Serializable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String EXCEL_FILE_PATH = "/Users/idali/Desktop/Hexapoda.xlsx";

    private String taxonName;
    private int rankId;
    private String fullName;
    private String name;
    private String synonmys;
   

    private final int KINDOM_ID = 453806;
    private final int RANK_SUBPHYLUM_ID = 40;
    private final int RANK_CLASS_ID = 60; 
    private final int RANK_ORDER_ID = 100;
    private final int RANK_SUBORDER_ID = 110;
    private final int RANK_INFRAORDER_ID = 120;
    private final int RANK_SUPERFAMILY_ID = 130;
    private final int RANK_FAMILY_ID = 140;
    private final int RANK_SUBFAMILY_ID = 150;
    private final int RANK_TRIBE_ID = 160;
    private final int RANK_SUBTRIBE_ID = 170;
    private final int RANK_GENUS_ID = 180;
    private final int RANK_SUBGENUS_ID = 190;
    private final int RANK_SPECIES_ID = 220;
    private final int RANK_SUBSPECIES_ID = 230;

    private Taxon rootTaxon;
    private Taxon subphylumTaxon;
    private Taxon classTaxon;
    private Taxon orderTaxon;
    private Taxon subOrderTaxon; 
    private Taxon familyTaxon; 
    private Taxon tribeTaxon; 
    private Taxon genusTaxon;
    private Taxon subgenusTaxon;
    private Taxon speciesTaxon; 
 
    private int currentRankId;
    private Taxon currentParent;

    private Taxon theTaxon; 
    private Taxontreedefitem subphylumTreeDefItem;
    private Taxontreedefitem classTreeDefItem;
    private Taxontreedefitem orderTreeDefItem;
    private Taxontreedefitem suborderTreeDefItem;
    private Taxontreedefitem infraorderTreeDefItem;
    private Taxontreedefitem superfamilyTreeDefItem;
    private Taxontreedefitem familyTreeDefItem;
    private Taxontreedefitem subfamilyTreeDefItem;
    private Taxontreedefitem tribeTreeDefItem;
    private Taxontreedefitem subtribeTreeDefItem;
    private Taxontreedefitem genusTreeDefItem;
    private Taxontreedefitem subgenusTreeDefItem;
    private Taxontreedefitem speciesTreeDefItem;
    private Taxontreedefitem subspeciesTreeDefItem;
 
    private Cell cell;

    private final int TAXON_TREE_DEF_ID = 11;
    private Taxontreedef taxonTreeDef;
    private final int CREATED_BY_AGENT_ID = 5945;
    private Agent cratedByAgent;

    private Timestamp timestamp;

    @EJB
    private SMTPDao smtpDao;

    public DyntaxaDumpLogic() {

    }

 
    public void uploadSubphylum() {
        logger.info("uploadSubphylum - excel file path : {}", EXCEL_FILE_PATH); 

        initBaseData();
        subphylumTreeDefItem = initData(RANK_SUBPHYLUM_ID);
        classTreeDefItem = initData(RANK_CLASS_ID);

        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum(); 
        IntStream.range(1, rowEndNumber + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    String theRank = row.getCell(1).getStringCellValue(); 
                    if (theRank.equals("Subphylum")) { 
                        uploadRootTaxon(row);
                    } else if (theRank.equals("Class")) {
                        smtpDao.create(createTaxon(row, subphylumTaxon,  RANK_CLASS_ID, classTreeDefItem)); 
                    } 
                });
        
        logger.info("Number of rows : {}", rowEndNumber); 
    }
      
    public void uploadOrders() {
        logger.info("uploadOrders - excel file path : {}", EXCEL_FILE_PATH); 

        initBaseData();
        orderTreeDefItem = initData(RANK_SUBPHYLUM_ID);
        suborderTreeDefItem = initData(RANK_ORDER_ID);
        infraorderTreeDefItem = initData(RANK_INFRAORDER_ID);

        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum(); 
        IntStream.range(1, rowEndNumber + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    String theRank = row.getCell(1).getStringCellValue(); 
                    switch (theRank) {
                        case "Class":
                            taxonName = row.getCell(2).getStringCellValue().trim();
                            break;
                        case "Order":
                            if(classTaxon == null || classTaxon.getFullName().equals(taxonName)) {
                                classTaxon = getTaxon(taxonName, RANK_CLASS_ID);  
                            }
                            orderTaxon = (Taxon) smtpDao.create(createTaxon(row, classTaxon, RANK_ORDER_ID, orderTreeDefItem));
                            break;
                        case "Suborder":
                            subOrderTaxon = (Taxon) smtpDao.create(createTaxon(row, orderTaxon, RANK_SUBORDER_ID, suborderTreeDefItem));
                            break;
                        case "Infraorder":
                            smtpDao.create(createTaxon(row, subOrderTaxon, RANK_INFRAORDER_ID, infraorderTreeDefItem));
                            break;
                        default:
                            break;
                    }
                }); 
        logger.info("Number of rows : {}", rowEndNumber); 
    }
    
    public void uploadSuperfamily() {
        
        logger.info("uploadSuperfamily - excel file path : {}", EXCEL_FILE_PATH); 

        initBaseData();
        superfamilyTreeDefItem = initData(RANK_SUPERFAMILY_ID); 

        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum();
        IntStream.range(1, rowEndNumber + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    String theRank = row.getCell(1).getStringCellValue();
                    if (theRank.equals("Order") || theRank.equals("Suborder") || theRank.equals("Infraorder")) {
                        taxonName = row.getCell(2).getStringCellValue().trim();
                        currentRankId = getRankId(theRank);
                    } else if (theRank.equals("Superfamily")) {
                        if (currentParent == null || !currentParent.getFullName().equals(taxonName)) {
                            currentParent = getTaxon(taxonName, currentRankId);
                        } 
                        smtpDao.create(createTaxon(row, currentParent, RANK_SUPERFAMILY_ID, superfamilyTreeDefItem)); 
                    } 
                });  
    }
    
    public void uploadFamily() {
        logger.info("uploadFamily - excel file path : {}", EXCEL_FILE_PATH); 

        List<Taxon> list = new ArrayList();
        initBaseData();
        familyTreeDefItem = initData(RANK_FAMILY_ID); 

        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum();
        IntStream.range(1, rowEndNumber + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    String theRank = row.getCell(1).getStringCellValue();
                    if (theRank.equals("Order") || theRank.equals("Suborder") || theRank.equals("Infraorder") || theRank.equals("Superfamily")) {
                        taxonName = row.getCell(2).getStringCellValue().trim();
                        currentRankId = getRankId(theRank);
                    } else if (theRank.equals("Family")) {
                        if (currentParent == null || !currentParent.getFullName().equals(taxonName)) {
                            currentParent = getTaxon(taxonName, currentRankId);
                        } 
                        list.add(createTaxon(row, currentParent, RANK_FAMILY_ID, familyTreeDefItem)); 
//                        smtpDao.create(createTaxon(row, currentParent, RANK_FAMILY_ID, familyTreeDefItem)); 
                    } 
                });  
        smtpDao.bacthCreate(list);
    }
     
    public void uploadSubfamily() {
        logger.info("uploadSubfamily - excel file path : {}", EXCEL_FILE_PATH); 

        List<Taxon> list = new ArrayList();
        initBaseData();
        subfamilyTreeDefItem = initData(RANK_SUBFAMILY_ID); 

        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum();
        IntStream.range(1, rowEndNumber + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    String theRank = row.getCell(1).getStringCellValue();
                    if (theRank.equals("Family") ) {
                        taxonName = row.getCell(2).getStringCellValue().trim(); 
                    } else if (theRank.equals("Subfamily")) {
                        if (familyTaxon == null || !familyTaxon.getFullName().equals(taxonName)) {
                            familyTaxon = getTaxon(taxonName, RANK_FAMILY_ID);
                        } 
                        list.add(createTaxon(row, familyTaxon, RANK_SUBFAMILY_ID, subfamilyTreeDefItem)); 
//                        smtpDao.create(createTaxon(row, familyTaxon, RANK_SUBFAMILY_ID, subfamilyTreeDefItem)); 
                    } 
                });  
        smtpDao.bacthCreate(list);
    }
    
    public void uploadTribe() {
        logger.info("uploadTribe - excel file path : {}", EXCEL_FILE_PATH); 

        List<Taxon> list = new ArrayList();
        initBaseData();
        tribeTreeDefItem = initData(RANK_TRIBE_ID); 

        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum();
        IntStream.range(1, rowEndNumber + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    String theRank = row.getCell(1).getStringCellValue();
                    if (theRank.equals("Superfamily") || theRank.equals("Family") || theRank.equals("Subfamily")) {
                        taxonName = row.getCell(2).getStringCellValue().trim();
                        currentRankId = getRankId(theRank);
                    } else if (theRank.equals("Tribe")) {
                        if (currentParent == null || !currentParent.getFullName().equals(taxonName)) {
                            currentParent = getTaxon(taxonName, currentRankId);
                        }
                        list.add(createTaxon(row, currentParent, RANK_TRIBE_ID, tribeTreeDefItem)); 
//                        smtpDao.create(createTaxon(row, currentParent, RANK_TRIBE_ID, tribeTreeDefItem));
                    }
                }); 
        smtpDao.bacthCreate(list);
    }
    
    public void uploadSubtribe() {
        logger.info("uploadSubtribe - excel file path : {}", EXCEL_FILE_PATH); 

        List<Taxon> list = new ArrayList();
        initBaseData();
        subtribeTreeDefItem = initData(RANK_SUBTRIBE_ID); 

        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum();
        IntStream.range(1, rowEndNumber + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    String theRank = row.getCell(1).getStringCellValue();
                    if (theRank.equals("Tribe")) {
                        taxonName = row.getCell(2).getStringCellValue().trim();
                    } else if (theRank.equals("Subtribe")) {
                        if (tribeTaxon == null || !tribeTaxon.getFullName().equals(taxonName)) {
                            tribeTaxon = getTaxon(taxonName, RANK_TRIBE_ID);
                        }
//                        smtpDao.create(createTaxon(row, tribeTaxon, RANK_SUBTRIBE_ID, subtribeTreeDefItem)); 
                        list.add(createTaxon(row, tribeTaxon, RANK_SUBTRIBE_ID, subtribeTreeDefItem)); 
                    }
                });
        smtpDao.bacthCreate(list);
    }

    public void uploadGenus() {
        logger.info("uploadGenus - excel file path : {}", EXCEL_FILE_PATH);

        List<Taxon> list = new ArrayList();
        initBaseData();
        genusTreeDefItem = initData(RANK_GENUS_ID);

        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum();
        IntStream.range(1, rowEndNumber + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    String theRank = row.getCell(1).getStringCellValue();
                    if (theRank.equals("Family") || theRank.equals("Subfamily") || theRank.equals("Tribe") || theRank.equals("Subtribe")) {
                        taxonName = row.getCell(2).getStringCellValue().trim();
                        currentRankId = getRankId(theRank);
                    } else if (theRank.equals("Genus")) {
                        if (currentParent == null || !currentParent.getFullName().equals(taxonName)) {
                            currentParent = getTaxon(taxonName, currentRankId);
                        }

                        list.add(createTaxon(row, currentParent, RANK_GENUS_ID, genusTreeDefItem));
                        if (list.size() % 1000 == 0) {
                            smtpDao.bacthCreate(list);
                            list.clear();
                        }
                    }  
                }); 
        logger.info("list size : {}", list.size());
        if(!list.isEmpty()) {
            smtpDao.bacthCreate(list); 
        } 
        logger.info("Number of rows : {}", rowEndNumber);  
    }
    
    public void uploadSubgenus() {
        logger.info("uploadSubgenus - excel file path : {}", EXCEL_FILE_PATH);

        List<Taxon> list = new ArrayList();
        initBaseData();
        subgenusTreeDefItem = initData(RANK_SUBGENUS_ID);

        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum(); 
        IntStream.range(1, rowEndNumber + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    String theRank = row.getCell(1).getStringCellValue();

                    if (theRank.equals("Genus")) {
                        taxonName = row.getCell(2).getStringCellValue().trim(); 
                    } else if (theRank.equals("Subgenus")) {
                        if(genusTaxon == null || !genusTaxon.getFullName().equals(taxonName)) {
                            genusTaxon = getTaxon(taxonName, RANK_GENUS_ID);
                        }
                        list.add(createTaxon(row, genusTaxon, RANK_SUBGENUS_ID, subgenusTreeDefItem));
                        if (list.size() % 1000 == 0) {
                            smtpDao.bacthCreate(list);
                            list.clear();
                        } 
                    }
                });
        logger.info("list size : {}", list.size());
        if(!list.isEmpty()) {
            smtpDao.bacthCreate(list); 
        } 
        logger.info("Number of rows : {}", rowEndNumber); 
    }
     
    public void uploadSubspecies() {
        logger.info("uploadSubspecies - excel file path : {}", EXCEL_FILE_PATH);

        List<Taxon> list = new ArrayList();
        
        initBaseData();
        subspeciesTreeDefItem = initData(RANK_SUBSPECIES_ID);

        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum();
        IntStream.range(1, rowEndNumber + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    String theRank = row.getCell(1).getStringCellValue();
                    if (theRank.equals("Species")) {
                        taxonName = row.getCell(2).getStringCellValue().trim();
                    } else if (theRank.equals("Subspecies")) {
                        if (speciesTaxon == null || !speciesTaxon.getFullName().equals(taxonName)) {
                            speciesTaxon = getTaxon(taxonName, RANK_SPECIES_ID);
                        }
                        list.add(createTaxon(row, speciesTaxon, RANK_SUBSPECIES_ID, subspeciesTreeDefItem));
                        if (list.size() % 1000 == 0) {
                            smtpDao.bacthCreate(list);
                            list.clear();
                        } 
                    }
                });

        logger.info("list size : {}", list.size());
        if (!list.isEmpty()) {
            smtpDao.bacthCreate(list);
        } 
        logger.info("Number of rows : {}", rowEndNumber);
    }


    
    public void uploadSpeciesOnSubgenus() {
        logger.info("uploadSubgenus - excel file path : {}", EXCEL_FILE_PATH);

        List<Taxon> list = new ArrayList();
        
        initBaseData();
        speciesTreeDefItem = initData(RANK_SPECIES_ID);
        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum();
        IntStream.range(1, rowEndNumber + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    String theRank = row.getCell(1).getStringCellValue(); 
                    if (theRank.equals("Genus") || theRank.equals("Subgenus")  ) {
                        currentRankId = getRankId(theRank); 
                        if(currentRankId == RANK_SUBGENUS_ID) {
                            taxonName = row.getCell(2).getStringCellValue().trim(); 
                        } 
                    } else if (theRank.equals("Species")) {
                        if (currentRankId == RANK_SUBGENUS_ID) {
                            if (subgenusTaxon  == null || !subgenusTaxon.getFullName().equals(taxonName)) {
                                subgenusTaxon = getTaxon(taxonName, RANK_SUBGENUS_ID);
                            } 
                            list.add(createTaxon(row, subgenusTaxon, RANK_SPECIES_ID, speciesTreeDefItem));
                            if(list.size() % 1000 == 0) {
                                smtpDao.bacthCreate(list); 
                                list.clear();
                            } 
                        }
                    }
                }); 
        logger.info("list size : {}", list.size());
        if(!list.isEmpty()) {
            smtpDao.bacthCreate(list); 
        }
        logger.info("Number of rows : {}", rowEndNumber);
    }
     
    public void uploadSpeciesOnGenus() {
        logger.info("uploadSubgenus - excel file path : {}", EXCEL_FILE_PATH);

        List<Taxon> list = new ArrayList();
        
        initBaseData();
        speciesTreeDefItem = initData(RANK_SPECIES_ID);
        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum(); 
        IntStream.range(1, rowEndNumber + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    String theRank = row.getCell(1).getStringCellValue();

                    if (theRank.equals("Genus") || theRank.equals("Subgenus")  ) { 
                        currentRankId = getRankId(theRank); 
                        if(currentRankId == RANK_GENUS_ID) {
                            taxonName = row.getCell(2).getStringCellValue().trim();
                        }
                    } else if (theRank.equals("Species")) {
                        if (currentRankId == RANK_GENUS_ID) {
                            if (genusTaxon == null || !genusTaxon.getFullName().equals(taxonName)) {
                                genusTaxon = getTaxon(taxonName, RANK_GENUS_ID);
                            } 
                            list.add(createTaxon(row, genusTaxon, RANK_SPECIES_ID, speciesTreeDefItem));
                            if(list.size() % 1000 == 0) {
                                smtpDao.bacthCreate(list); 
                                list.clear();
                            }
                        }
                    }
                });
        
        logger.info("list size : {}", list.size());
        if(!list.isEmpty()) {
            smtpDao.bacthCreate(list); 
        } 
        logger.info("Number of rows : {}", rowEndNumber); 
    }
   
    public void uploadSynonmys() {
        logger.info("uploadSubgenus - excel file path : {}", EXCEL_FILE_PATH);

        List<Taxon> list = new ArrayList();
        initData();

        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum();
        IntStream.range(1, rowEndNumber + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                     
                    cell = row.getCell(8);
                    if (cell != null) {
                        synonmys = cell.getStringCellValue().trim();
                        if (!synonmys.isEmpty()) { 
                            rankId = getRankId(row.getCell(1).getStringCellValue().trim()); 
                            if(rankId < 220) {
                                taxonName = row.getCell(2).getStringCellValue().trim();

                                List<String> synomyList = new ArrayList<>();
                                if (synonmys.contains(";")) {
                                    synomyList = Arrays.asList(StringUtils.split(synonmys, ";"));
                                } else {
                                    synomyList.add(synonmys.trim());
                                }
                                synomyList.stream().forEach(s -> {
                                    if (theTaxon == null || !theTaxon.getFullName().equals(taxonName)) {
                                        theTaxon = getTaxon(taxonName, rankId);
                                    }
                                    list.add(createSynonmyTaxon(s));
                                    if (list.size() % 1000 == 0) {
                                        smtpDao.bacthCreate(list);
                                        list.clear();
                                    }
                                });
                            } 
                        }
                    } 
                });

        logger.info("list size : {}", list.size());
        if (!list.isEmpty()) {
            smtpDao.bacthCreate(list);
        }
        logger.info("Number of rows : {}", rowEndNumber);
    }
    
    public void uploadSpeciesSynonmys1() {
        logger.info("uploadSubgenus - excel file path : {}", EXCEL_FILE_PATH);

        List<Taxon> list = new ArrayList();
        initData();

        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum();
        IntStream.range(1, 24007)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    cell = row.getCell(8);
                    if (cell != null) {
                        synonmys = cell.getStringCellValue().trim();
                        if (!synonmys.isEmpty()) {
                            rankId = getRankId(row.getCell(1).getStringCellValue().trim());
                            if(rankId == 220) {
                                taxonName = row.getCell(2).getStringCellValue().trim();

                                List<String> synomyList = new ArrayList<>();
                                if (synonmys.contains(";")) {
                                    synomyList = Arrays.asList(StringUtils.split(synonmys, ";"));
                                } else {
                                    synomyList.add(synonmys.trim());
                                }
                                synomyList.stream().forEach(s -> {
                                    if (theTaxon == null || !theTaxon.getFullName().equals(taxonName)) {
                                        theTaxon = getTaxon(taxonName, rankId);
                                    }
                                    list.add(createSynonmyTaxon(s));
                                    if (list.size() % 1000 == 0) {
                                        smtpDao.bacthCreate(list);
                                        list.clear();
                                    }
                                });
                            } 
                        }
                    }
                });

        logger.info("list size : {}", list.size());
        if (!list.isEmpty()) {
            smtpDao.bacthCreate(list);
        }
        logger.info("Number of rows : {}", rowEndNumber);
    }

    public void uploadSpeciesSynonmys() {
        logger.info("uploadSubgenus - excel file path : {}", EXCEL_FILE_PATH);

        List<Taxon> list = new ArrayList();
        initData();

        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum(); 
        IntStream.range(24007, rowEndNumber + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    cell = row.getCell(8);
                    if (cell != null) {
                        synonmys = cell.getStringCellValue().trim();
                        if (!synonmys.isEmpty()) {
                            rankId = getRankId(row.getCell(1).getStringCellValue().trim());
                            if(rankId == 220) {
                                taxonName = row.getCell(2).getStringCellValue().trim();

                                List<String> synomyList = new ArrayList<>();
                                if (synonmys.contains(";")) {
                                    synomyList = Arrays.asList(StringUtils.split(synonmys, ";"));
                                } else {
                                    synomyList.add(synonmys.trim());
                                }
                                synomyList.stream().forEach(s -> {
                                    if (theTaxon == null || !theTaxon.getFullName().equals(taxonName)) {
                                        theTaxon = getTaxon(taxonName, rankId);
                                    }
                                    list.add(createSynonmyTaxon(s));
                                    if (list.size() % 1000 == 0) {
                                        smtpDao.bacthCreate(list);
                                        list.clear();
                                    }
                                });
                            } 
                        }
                    }
                });

        logger.info("list size : {}", list.size());
        if (!list.isEmpty()) {
            smtpDao.bacthCreate(list);
        }
        logger.info("Number of rows : {}", rowEndNumber);
    }
    
    public void uploadSubspeciesSynonmys() {
        logger.info("uploadSubspeciesSynonmys - excel file path : {}", EXCEL_FILE_PATH);

        List<Taxon> list = new ArrayList();
        initBaseData();
        subspeciesTreeDefItem = initData(RANK_SUBSPECIES_ID);
        

        XSSFSheet sheet = getExcelSheet();
        int rowEndNumber = sheet.getLastRowNum();
        IntStream.range(1, rowEndNumber + 1)
                .forEach(nbr -> {
                    XSSFRow row = sheet.getRow(nbr);
                    cell = row.getCell(8);
                    if (cell != null) {
                        synonmys = cell.getStringCellValue().trim();
                        if (!synonmys.isEmpty()) {
                            rankId = getRankId(row.getCell(1).getStringCellValue().trim());
                            if(rankId == 230) {
                                taxonName = row.getCell(2).getStringCellValue().trim();

                                List<String> synomyList = new ArrayList<>();
                                if (synonmys.contains(";")) {
                                    synomyList = Arrays.asList(StringUtils.split(synonmys, ";"));
                                } else {
                                    synomyList.add(synonmys.trim());
                                }
                                synomyList.stream().forEach(s -> {
                                    if (theTaxon == null || !theTaxon.getFullName().equals(taxonName)) {
                                        theTaxon = getTaxon(taxonName, rankId);
                                    }
                                    list.add(createSynonmyTaxon(s));
                                    if (list.size() % 1000 == 0) {
                                        smtpDao.bacthCreate(list);
                                        list.clear();
                                    }
                                });
                            } 
                        }
                    }
                });

        logger.info("list size : {}", list.size());
        if (!list.isEmpty()) {
            smtpDao.bacthCreate(list);
        }
        logger.info("Number of rows : {}", rowEndNumber);
    }
 
    private Taxon createSynonmyTaxon(String synonmy) {
         
        Taxon synTaxon = new Taxon();
        synTaxon.setAcceptedID(theTaxon);
        synTaxon.setCreatedByAgentID(cratedByAgent);
        synTaxon.setFullName(synonmy.trim());
    
        if (rankId >= 190) {
            synTaxon.setName(buildName(synonmy.trim()));
        } else {
            synTaxon.setName(synonmy.trim());
        }
        synTaxon.setSource(theTaxon.getSource());
        synTaxon.setIsAccepted(Boolean.FALSE);
        synTaxon.setParentID(theTaxon.getParentID());
        synTaxon.setRankID(rankId);
        synTaxon.setTimestampCreated(timestamp);
        synTaxon.setTaxonTreeDefID(taxonTreeDef);
        synTaxon.setTaxonTreeDefItemID(theTaxon.getTaxonTreeDefItemID());   
        return synTaxon;
    }
    
    
    
    
    
    
    private void uploadRootTaxon(XSSFRow row) {
        rootTaxon = (Taxon) smtpDao.findByReference(KINDOM_ID, Taxon.class);            // get the kindom as root 
        Taxon taxon = createTaxon(row, rootTaxon,  RANK_SUBPHYLUM_ID, subphylumTreeDefItem);
 
        subphylumTaxon = (Taxon) smtpDao.create(taxon); 
    }
    
    private String buildName(String taxonName) {
        if(taxonName.contains(" ")) {
            int index = taxonName.indexOf(" ");
            return taxonName.substring(index + 1);
        } 
        return taxonName;
    }


    private Taxon getTaxon(String fullName, int rankId) {
        return (Taxon) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonParent(fullName, rankId, TAXON_TREE_DEF_ID));
    }


    private XSSFSheet getExcelSheet() {
        try (FileInputStream file = new FileInputStream(new File(EXCEL_FILE_PATH))) {
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            return workbook.getSheetAt(0);
        } catch (IOException ex) {
            System.out.println("error: " + ex.getMessage());
            return null;
        }
    }
    
    private Taxon createTaxon(XSSFRow row, Taxon parent, int rankId, Taxontreedefitem item) {
        Taxon newTaxon = new Taxon();
        newTaxon.setParentID(parent);
        
        fullName = row.getCell(2).getStringCellValue().trim();
        newTaxon.setFullName(fullName); 
        
        name = fullName;
        if(rankId >= 190) {
            name = buildName(fullName);
        }
        newTaxon.setName(name);
  
        newTaxon.setNumber1((int) row.getCell(0).getNumericCellValue()); 
        cell = row.getCell(3);
        if (cell != null) {
            newTaxon.setAuthor(cell.getStringCellValue().trim());
        }

        cell = row.getCell(4);
        if (cell != null) {
            newTaxon.setCommonName(cell.getStringCellValue().trim());
        }

        cell = row.getCell(5);
        if (cell != null) {
            newTaxon.setGuid(cell.getStringCellValue().trim());
        }

        cell = row.getCell(7);
        if (cell != null) {
            newTaxon.setSource(cell.getStringCellValue().trim());
        }

        newTaxon.setCreatedByAgentID(cratedByAgent);
        newTaxon.setRankID(rankId);
        newTaxon.setIsAccepted(true);

        newTaxon.setTimestampCreated(timestamp);
        newTaxon.setTaxonTreeDefID(taxonTreeDef);
        newTaxon.setTaxonTreeDefItemID(item);
        return newTaxon;
    }
     
    private int getRankId(String rank) {
        switch (rank) {
            case "Order":
                return RANK_ORDER_ID; 
            case "Suborder":
                return RANK_SUBORDER_ID; 
            case "Infraorder":
                return RANK_INFRAORDER_ID; 
            case "Superfamily":
                return RANK_SUPERFAMILY_ID; 
            case "Family":
                return RANK_FAMILY_ID; 
            case "Subfamily":
                return RANK_SUBFAMILY_ID; 
            case "Tribe":
                return RANK_TRIBE_ID; 
            case "Subtribe":
                return RANK_SUBTRIBE_ID; 
            case "Genus":
                return RANK_GENUS_ID; 
            case "Subgenus":
                return RANK_SUBGENUS_ID; 
            case "Species":
                return RANK_SPECIES_ID; 
            case "Subspecies":
                return RANK_SUBSPECIES_ID; 
            default:
                return 0;
        }
    }
    
  
    private Taxontreedefitem initData(int rankid) { 
        return (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, rankid));
    }
    
    private void initBaseData() {
        timestamp = new Timestamp(System.currentTimeMillis());
        taxonTreeDef = (Taxontreedef) smtpDao.findByReference(TAXON_TREE_DEF_ID, Taxontreedef.class);
        cratedByAgent = (Agent) smtpDao.findByReference(CREATED_BY_AGENT_ID, Agent.class);
    }

    private void initData() {
        timestamp = new Timestamp(System.currentTimeMillis());
        taxonTreeDef = (Taxontreedef) smtpDao.findByReference(TAXON_TREE_DEF_ID, Taxontreedef.class);
        cratedByAgent = (Agent) smtpDao.findByReference(CREATED_BY_AGENT_ID, Agent.class);

        subphylumTreeDefItem = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, RANK_SUBPHYLUM_ID));
        classTreeDefItem = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, RANK_CLASS_ID));
        orderTreeDefItem = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, RANK_ORDER_ID));
        suborderTreeDefItem = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, RANK_SUBORDER_ID));
        infraorderTreeDefItem = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, RANK_INFRAORDER_ID));
        superfamilyTreeDefItem = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, RANK_SUPERFAMILY_ID));
        familyTreeDefItem = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, RANK_FAMILY_ID));
        subfamilyTreeDefItem = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, RANK_SUBFAMILY_ID));
        tribeTreeDefItem = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, RANK_TRIBE_ID));
        subtribeTreeDefItem = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, RANK_SUBTRIBE_ID));
        genusTreeDefItem = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, RANK_GENUS_ID));
        
        speciesTreeDefItem = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, RANK_SPECIES_ID));
        subspeciesTreeDefItem = (Taxontreedefitem) smtpDao.getEntityByJPQL(
                QueryStringBuilder.getInstance()
                        .buildGetTaxonTreeDefItem(TAXON_TREE_DEF_ID, RANK_SUBSPECIES_ID));
    }

}
