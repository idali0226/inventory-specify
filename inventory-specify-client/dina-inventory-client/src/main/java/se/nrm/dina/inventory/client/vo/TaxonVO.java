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
public class TaxonVO {
     
    private final List<String> taxonList;
    
    public TaxonVO(List<String> taxonList) {
        this.taxonList = taxonList;
    } 
    
    public List<String> getTaxonList() {
        return taxonList;
    }
}
