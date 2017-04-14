/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dyntaxa.dump.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;  
import javax.ejb.Stateless;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.dyntaxa.dump.vo.TaxonVO;

/**
 *
 * @author idali
 */
@Stateless
public class ExcelReader implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String EXCEL_FILE_PATH = "/Users/idali/Desktop/Hexapoda.xlsx";
    private static final String NEW_TAXAN_EXCEL_FILE_PATH = "/Users/idali/Desktop/new_smtp_taxa_2017-04-11.xlsx";
    
    private String currentParent;
    private String currentRank;
    private String taxonName;
    
    public ExcelReader() {
        
    }
    
    // read Fredrik new taxon file
    public void readNewTaxonExcel() {
         try (FileInputStream file = new FileInputStream(new File(NEW_TAXAN_EXCEL_FILE_PATH))) {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            XSSFSheet sheet = workbook.getSheetAt(6);
            int numOfRows = sheet.getLastRowNum();
            IntStream.range(3, numOfRows + 1)
                .forEach(nbr -> {
                        XSSFRow row = sheet.getRow(nbr);
                        String scientificName = row.getCell(0).getStringCellValue();
                        String rank = row.getCell(1).getStringCellValue();
                        String author = row.getCell(2).getStringCellValue();
                        String parent = row.getCell(3).getStringCellValue();
                        String source = row.getCell(4).getStringCellValue();
                        String agent = row.getCell(5).getStringCellValue();
                        String isManuscript = row.getCell(7).getStringCellValue();
                        
                        System.out.println("isManuscript : " + Boolean.valueOf(isManuscript) + isManuscript);

                        if (agent.contains(" ")) {
                            int index = agent.indexOf(" ");
                            System.out.println("last name : " + agent.substring(index + 1)); 
                            System.out.println("first name : " + agent.split(" ")[0]);
                        }
                    });

        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
 
     
    // read dyntaxa dump file
    public List<TaxonVO> read() {
        
        logger.info("read excel"); 
        List<TaxonVO> list = new ArrayList();
        try (FileInputStream file = new FileInputStream(new File(EXCEL_FILE_PATH))) {
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            XSSFSheet sheet = workbook.getSheetAt(0); 
            int rowEndNumber = sheet.getLastRowNum();
               
            IntStream.range(1, rowEndNumber + 1)
                    .forEach(nbr -> {
                        XSSFRow row = sheet.getRow(nbr); 
                        String rank = row.getCell(1).getStringCellValue();    
                        if (rank.equals("Genus") || rank.equals("Subgenus")) {
                            currentParent = row.getCell(2).getStringCellValue();
                            currentRank = rank;
                        } else if (rank.equals("Species")) {
                            
                            taxonName = row.getCell(2).getStringCellValue();
                            TaxonVO vo = new TaxonVO(taxonName , currentParent, currentRank);
                            System.out.println(taxonName + " --- " + currentParent + " --- " + currentRank);
                            list.add(vo);
                        }
                    } ); 
            System.out.println("row number : " + rowEndNumber);
        } catch (IOException ex) {
            System.out.println("error: " + ex.getMessage());
        }
        return list;
    } 
}
