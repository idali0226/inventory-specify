/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.inventory.client.vo;
 
import java.util.List; 

/**
 *
 * @author idali
 */ 
public class ExcelData {
       
    private final String excelfilename;     
    private final int preparaedById;
    private final String preparedDate;
    private final String loanNumber;
    private final List<ObservationData> coDataList;
    
    public ExcelData(String excelfilename, int preparedById, String preparedDate, String loanNumber, List<ObservationData> coDataList) { 
        this.excelfilename = excelfilename; 
        this.preparaedById = preparedById;
        this.preparedDate = preparedDate;
        this.loanNumber = loanNumber;
        this.coDataList = coDataList;
    }

    public String getExcelfilename() {
        return excelfilename;
    }

    public int getPreparaedById() {
        return preparaedById;
    }

    public String getPreparedDate() {
        return preparedDate;
    }
 
    public List<ObservationData> getCoDataList() {
        return coDataList;
    } 

    public String getLoanNumber() {
        return loanNumber;
    } 
}
