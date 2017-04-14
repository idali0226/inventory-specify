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
    
    private final String taxonName;
    private final String commonName;
    private final String parentName;
    private final String parentRank;
    private final String author;
    private final String source;
    private final String guid;
    
    
    public TaxonVO(final String taxonName, final String commonName, final String parentName, 
                   final String parentRank, final String author, final String source, final String guid) {
        this.taxonName = taxonName;
        this.commonName = commonName;
        this.parentName = parentName;
        this.parentRank = parentRank;
        this.author = author;
        this.source = source;
        this.guid = guid;
    }

    public String getTaxonName() {
        return taxonName;
    }

    public String getCommonName() {
        return commonName;
    }

    
    public String getParentName() {
        return parentName;
    }

    public String getParentRank() {
        return parentRank;
    }

    public String getAuthor() {
        return author;
    }

    public String getSource() {
        return source;
    }

    public String getGuid() {
        return guid;
    }
    
    
}
