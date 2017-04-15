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
public final class TaxonCounter {
    
    private final String taxonName;
    private final int taxonCount;
    
    public TaxonCounter(String taxonName, int taxonCount) {
        this.taxonName = taxonName;
        this.taxonCount = taxonCount;
    }

    public String getTaxonName() {
        return taxonName;
    }

    public int getTaxonCount() {
        return taxonCount;
    } 
}
