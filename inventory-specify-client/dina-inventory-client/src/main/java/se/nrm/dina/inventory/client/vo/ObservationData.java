/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.inventory.client.vo;
 
import java.util.Date;


/**
 *
 * @author idali
 */ 
public class ObservationData {
 
    private final int eventId;   
    private final String genus; 
    private final String species;    
    private final String determiner;
    private final int determinedDate;
    private final String storage; 
    private final String media;  
    private final String remark; 
    private final int numOfMales; 
    private final int numOfFemales;   
    private final int total; 
    
    public ObservationData(int eventId, String genus, String species, String determiner, int determinedDate, 
                            String storage, String media, String remark, int numOfMales, int numOfFemales, int total) {
        this.eventId = eventId;  
        this.genus = genus;
        this.species = species;  
        this.determinedDate = determinedDate;
        this.determiner = determiner;
        this.storage = storage;
        this.media = media; 
        this.remark = remark;
        this.numOfMales = numOfMales;
        this.numOfFemales = numOfFemales;  
        this.total = total; 
    }
     
    public int getEventId() {
        return eventId;
    }
 
    public String getGenus() {
        return genus;
    }

    public String getSpecies() {
        return species;
    }

    public String getComputedName() {
        return genus + " " + species;
    }
 
    public String getStorage() {
        return storage;
    }

    public String getMedia() {
        return media;
    } 
    
    public String getRemark() {
        return remark;
    }

    public int getNumOfMales() {
        return numOfMales;
    }

    public int getNumOfFemales() {
        return numOfFemales;
    } 

    public int getTotal() {
        return total;
    }  

    public String getDeterminer() {
        return determiner;
    }

    public int getDeterminedDate() {
        return determinedDate;
    } 
}
