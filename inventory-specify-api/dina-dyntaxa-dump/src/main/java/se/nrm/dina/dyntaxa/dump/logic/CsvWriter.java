/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dyntaxa.dump.logic;

import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.nrm.dina.dyntaxa.dump.vo.TaxonVO;

/**
 *
 * @author idali
 */
public class CsvWriter {
    
    private String[] record;
    
    public void write(List<TaxonVO> taxonList) {
        
        try {
            String csv = "/Users/idali/Desktop/dyntaxa.csv";
            CSVWriter writer;

            List<String[]> list = new ArrayList();
            writer = new CSVWriter(new FileWriter(csv));

            record = new String[3];
            record[0] = "Species";
            record[1] = "Genus / Subgenus";
            record[2] = "Rank";
            list.add(record);

            taxonList.stream().forEach(l -> {
                record = new String[3];
                record[0] = l.getTaxonName();
                record[1] = l.getParentName();
                record[2] = l.getParentRank();
                
                list.add(record);
            });
            
             
            writer.writeAll(list);
 
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(CSVWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
    
}
