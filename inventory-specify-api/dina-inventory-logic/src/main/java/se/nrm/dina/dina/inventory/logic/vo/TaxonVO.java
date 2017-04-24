/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dina.inventory.logic.vo;

/**
 *
 * @author idali
 */
public class TaxonVO {
    
    private final int taxonID;
    private final String taxonName; 
    
    
    public TaxonVO(final int taxonId, String taxonName) {
        this.taxonID = taxonId;
        this.taxonName = taxonName; 
    }

    public String getTaxonName() {
        return taxonName;
    } 

    public int getTaxonID() {
        return taxonID;
    } 
}
