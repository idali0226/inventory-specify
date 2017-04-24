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
public final class TaxanData {
     
    private final int taxaId; 
    private final String genus;
    private final String species;
    private final String computedName; 
    private final String guid; 
    private final String author; 
    private final String notes; 
    private final String source;    
    
    
    public TaxanData(int taxaId, String genus, String species, String computedName, 
                        String guid, String author, String notes,  String source) {
        this.taxaId = taxaId;
        this.genus = genus;
        this.species = species;
        this.computedName = computedName;
        this.guid = guid;
        this.author = author;
        this.notes = notes;
        this.source = source; 
    }

    public int getTaxaId() {
        return taxaId;
    }

    public String getGenus() {
        return genus;
    }

    public String getSpecies() {
        return species;
    }

    public String getComputedName() {
        return computedName;
    }

    public String getGuid() {
        return guid;
    }

    public String getAuthor() {
        return author;
    }

    public String getNotes() {
        return notes;
    }

    public String getSource() {
        return source;
    } 
}
