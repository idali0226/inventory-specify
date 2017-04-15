/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.inventory.client.util;
 
import java.text.ParseException;
import java.text.SimpleDateFormat;  
import java.util.Date;  

/**
 *
 * @author idali
 */
public class Util {

    private final static boolean IS_LOCAL = false;
    private final static boolean IS_TEST = false;
     
    private final static int COLLECTION_ID_CFILE = 655361; 
 
    private final static int SPECIS_LIST_DISCIPLINE_ID = 655360;
    private final static int SPECIS_LIST_TAXON_TREE_DEF_ID = 11;

    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static boolean isIS_LOCAL() {
        return IS_LOCAL;
    }

    public static boolean isIS_TEST() {
        return IS_TEST;
    }

    public static int getCollectionIdCFile() {
        return COLLECTION_ID_CFILE;
    }
 
    public static int getSpecisListDisciplineId() {
        return SPECIS_LIST_DISCIPLINE_ID;
    }
    
    public static int getSpecisListTaxonTreeDefId() {
        return SPECIS_LIST_TAXON_TREE_DEF_ID;
    }
 
    public static String getNow() { 
        Date now = new Date(); 
        return FORMAT.format(now);
    }
    
    /**
     * Convert String to Date
     * 
     * @param date - String
     * @return Date
     */
    public static Date stringToDate(String date) {  
        try { 
            return FORMAT.parse(date);
        } catch (ParseException ex) {
            return null;
        }
    } 
    
        
    /**
     * Convert Date to String with "yyyy-MM-dd" format
     * 
     * @param date - Date
     * @return String
     */
    public static String dateToString(Date date) { 
        if(date == null) {
            return null;
        } else {
            return FORMAT.format(date); 
        } 
    } 
     
    public static String replaceChars(String value) {
        return value.replaceAll("[\\[\\](),]", " ");   
    }  
}
