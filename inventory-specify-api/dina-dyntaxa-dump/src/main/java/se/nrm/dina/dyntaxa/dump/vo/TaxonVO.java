/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dyntaxa.dump.vo;

/**
 *
 * @author idali
 */
public class TaxonVO {
    
    private final String taxonName;
    private final String parentName;
    private final String parentRank;
     
    
    public TaxonVO(final String taxonName, final String parentName, final String parentRank ) {
        this.taxonName = taxonName;
        this.parentName = parentName;
        this.parentRank = parentRank; 
    }

    public String getTaxonName() {
        return taxonName;
    }

    public String getParentName() {
        return parentName;
    }

    public String getParentRank() {
        return parentRank;
    } 
}
