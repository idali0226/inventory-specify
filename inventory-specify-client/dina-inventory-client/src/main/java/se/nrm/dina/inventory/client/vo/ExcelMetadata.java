/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.inventory.client.vo;

/**
 *
 * @author idali
 */
public class ExcelMetadata {
    
    private final String loanNumber;
    private final String taxonFamily;
    private final String determiner;
    private int determinerId;
    private final String submittedDate;
    
    public ExcelMetadata(final String loanNumber, final String taxonFamily, 
                                final String determiner, final String submittedDate) {
        this.loanNumber = loanNumber;
        this.taxonFamily = taxonFamily;
        this.determiner = determiner;
        this.submittedDate = submittedDate;
    }
    
    public ExcelMetadata(final String loanNumber, final String taxonFamily, final String determiner, 
                                final int determinerId, final String submittedDate) {
        this.loanNumber = loanNumber;
        this.taxonFamily = taxonFamily;
        this.determiner = determiner;
        this.determinerId = determinerId;
        this.submittedDate = submittedDate;
    }

    public int getDeterminerId() {
        return determinerId;
    }

    public void setDeterminerId(int determinerId) {
        this.determinerId = determinerId;
    }
         
    public String getLoanNumber() {
        return loanNumber;
    }

    public String getTaxonFamily() {
        return taxonFamily;
    }

    public String getDeterminer() {
        return determiner;
    }

    public String getSubmittedDate() {
        return submittedDate;
    } 
}
