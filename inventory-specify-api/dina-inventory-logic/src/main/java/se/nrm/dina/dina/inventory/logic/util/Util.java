/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dina.inventory.logic.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID; 
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author idali
 */
public class Util {
     
    private final int RANK_SUBPHYLUM_ID = 40;
    private final int RANK_CLASS_ID = 60; 
    private final int RANK_ORDER_ID = 100;
    private final int RANK_SUBORDER_ID = 110;
    private final int RANK_INFRAORDER_ID = 120;
    private final int RANK_SUPERFAMILY_ID = 130;
    private final int RANK_FAMILY_ID = 140;
    private final int RANK_SUBFAMILY_ID = 150;
    private final int RANK_TRIBE_ID = 160;
    private final int RANK_SUBTRIBE_ID = 170;
    private final int RANK_GENUS_ID = 180;
    private final int RANK_SUBGENUS_ID = 190;
    private final int RANK_SPECIES_ID = 220;
    private final int RANK_SUBSPECIES_ID = 230;
    
    private final int DISCIPLIN_ID = 655360;
    private final static int COLLECTION_ID = 655361;  
    private final static int SPECIS_LIST_TAXON_TREE_DEF_ID = 11;
    private final int AGENT_ID = 5945;
    
    private static final DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    
    private static Util instance = null;

    public static synchronized Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }
    
    public UUID generateGUID() { 
        return UUID.randomUUID(); 
    }
         
    public int getRankId(String rank) {
        switch (rank) {
            case "Subphylum":
                return RANK_SUBPHYLUM_ID; 
            case "Class":
                return RANK_CLASS_ID;  
            case "Order":
                return RANK_ORDER_ID; 
            case "Suborder":
                return RANK_SUBORDER_ID; 
            case "Infraorder":
                return RANK_INFRAORDER_ID; 
            case "Superfamily":
                return RANK_SUPERFAMILY_ID; 
            case "Family":
                return RANK_FAMILY_ID; 
            case "Subfamily":
                return RANK_SUBFAMILY_ID; 
            case "Tribe":
                return RANK_TRIBE_ID; 
            case "Subtribe":
                return RANK_SUBTRIBE_ID; 
            case "Genus":
                return RANK_GENUS_ID; 
            case "Subgenus":
                return RANK_SUBGENUS_ID; 
            case "Species":
                return RANK_SPECIES_ID; 
            case "Subspecies":
                return RANK_SUBSPECIES_ID; 
            default:
                return 0;
        }
    }
    
    public int getDisciplineId() {
        return DISCIPLIN_ID;
    }
    
    public int getCollectionId() {
        return COLLECTION_ID;
    }
    
    public int getTaxonTreeDefId() {
        return SPECIS_LIST_TAXON_TREE_DEF_ID;
    }
    
    public int getAgentId() {
        return AGENT_ID;
    }
    
    public Date convertStringToDate(String strDate) {
        
        if(strDate == null) {
            return null;
        } else {
            try {
                return DATA_FORMAT.parse(strDate);
            } catch (ParseException ex) {
                return null;
            }
        } 
    }
        
    public Date convertStringToDate(int year) {
        try { 
            if(year != 0) {
                String strYear = String.valueOf(year); 
                if(strYear.length() == 8) {
                    strYear = StringUtils.substring(strYear, 0, 3);
                } 
                return DATA_FORMAT.parse(strYear + "-01-01");
            } else {
                return null;
            } 
        } catch (ParseException ex) {
            return null;
        }
    }
}
