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
public class UsedTaxon {
     
    private final String scientificName;
    private final String author;
    
    public UsedTaxon(String scientificName, String author) { 
        this.scientificName = scientificName;
        this.author = author;
    }
 
    public String getScientificName() {
        return scientificName;
    } 

    public String getAuthor() {
        return author;
    }
     
    @Override
    public String toString() {
        return scientificName;
    }
}
