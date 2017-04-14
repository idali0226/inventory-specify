/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dyntaxa.dump;
 
import java.util.List;
import se.nrm.dina.dyntaxa.dump.logic.ExcelReader;
import se.nrm.dina.dyntaxa.dump.logic.CsvWriter;
import se.nrm.dina.dyntaxa.dump.vo.TaxonVO;


/**
 *
 * @author idali
 */
public class Main {
     
    public static void main(String ...args) {
        System.out.println("test");
        
        new ExcelReader().readNewTaxonExcel();
//        List<TaxonVO> list = new ExcelReader().read();
//        new CsvWriter().write(list);
        
        System.out.println("Done....");
    }
}
