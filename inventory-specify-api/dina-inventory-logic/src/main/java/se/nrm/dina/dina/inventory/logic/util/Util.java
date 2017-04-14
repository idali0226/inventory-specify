/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dina.inventory.logic.util;

import java.util.UUID;

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
}
