/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dina.inventory.logic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.datamodel.impl.Taxon;

/**
 *
 * @author idali
 */
public class ExcelWriter {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String EXCEL_FILE_PATH = "/Users/idali/Desktop/species_list.xlsx";
    
    private static final String[] columns = {"Full name", "Name", "Author", "guid", "Remarks", "Source"};
    
    public void writeCSVFile(List<Taxon> speciesList) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Species List");
            // Create a Row
            Row headerRow = sheet.createRow(0);
            // Creating cells
            for(int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }  
            
  
            // Create Other rows and cells with employees data
            int rowNum = 1;
            for(Taxon taxon: speciesList) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(taxon.getFullName());  
                row.createCell(1).setCellValue(taxon.getName()); 
                row.createCell(2).setCellValue(taxon.getAuthor()); 
                row.createCell(3).setCellValue(taxon.getGuid());
                row.createCell(4).setCellValue(taxon.getRemarks()); 
                row.createCell(5).setCellValue(taxon.getSource());
                
            }   
            
            // Resize all columns to fit the content size
            for(int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }   // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(EXCEL_FILE_PATH);
            workbook.write(fileOut);
            fileOut.close();
            // Closing the workbook
        }
    }
}
