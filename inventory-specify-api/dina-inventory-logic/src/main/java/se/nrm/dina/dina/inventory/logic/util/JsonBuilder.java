/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dina.inventory.logic.util;

import java.util.List;
import java.util.stream.Collectors;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author idali
 */
public class JsonBuilder {
    
    private JSONObject jsonObject;
    private static JsonBuilder instance = null;

    public static synchronized JsonBuilder getInstance() {
        if (instance == null) {
            instance = new JsonBuilder();
        }
        return instance;
    }
    
    public String buildTaxonAndEventCountJson(int collectingEventCount, List<Object[]> results) {
        
        int total = 0; 
        jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();    
        try {  
            if(results != null) { 
                for (Object[] obj : results) {  
                    jsonObject = new JSONObject();  
                    jsonObject.put("taxon", obj[0]);     
                    jsonObject.put("total", obj[1]); 
                    jsonArray.put(jsonObject);  
                    total += ((Number)obj[1]).intValue(); 
                } 
                jsonObject = new JSONObject();  
                jsonObject.put("totalvial", total); 
                jsonObject.put("totalevent", collectingEventCount); 
                jsonObject.put("determination", jsonArray); 
                jsonObject.put("uniquecount", jsonArray.length());
            } else {
                jsonObject.put("noresult", 0);
            } 
        } catch (JSONException ex) { 
        } 
        return jsonObject.toString();
    }
    
//    public String buildTaxonCountJson(List<Object[]> results) {
//        int total = 0;
//        jsonObject = new JSONObject();
//        JSONArray jsonArray = new JSONArray();    
//        try {  
//            if(results != null) {
//                for (Object[] obj : results) {  
//                    jsonObject = new JSONObject();  
//                    jsonObject.put("taxon", obj[0]);   
//                    jsonObject.put("total", obj[1]); 
//                    jsonArray.put(jsonObject);  
//                    total += ((Number)obj[1]).intValue(); 
//                } 
//                jsonObject = new JSONObject();  
//                jsonObject.put("totalvial", total); 
//                jsonObject.put("determination", jsonArray); 
//                jsonObject.put("uniquecount", jsonArray.length());
//            } else {
//                jsonObject.put("noresult", 0);
//            } 
//        } catch (JSONException ex) { 
//        } 
//        return jsonObject.toString();
//    }
    
    public String buildCollectingEventListJson(List<Object[]> data) { 
        List<JSONObject> list = data.stream()
                .map(this::buildEventJsonObject)
                .collect(Collectors.toList());
        return list.toString(); 
    }
    
    private JSONObject buildEventJsonObject(Object[] obj) {  
        jsonObject = new JSONObject();
        try {
            jsonObject.put("sfn", obj[0]);
            jsonObject.put("startdate", obj[1]);
            jsonObject.put("enddate", obj[2]); 
        } catch (JSONException ex) { 
        } 
        return jsonObject;
    }
    
    
    
    public String buildAgentList(List<Object[]> results) {
        
        List<JSONObject> list =  results.stream()
                        .map(this::buildAgentJsonObject)
                        .collect(Collectors.toList());
        return list.toString();
    }
     
    private JSONObject buildAgentJsonObject(Object[] obj) {
        jsonObject = new JSONObject();
        
        StringBuilder sb = new StringBuilder();
        sb.append(obj[1]);
        sb.append(" ");
        sb.append(obj[2]);
        sb.append(" (");
        sb.append(obj[3]);
        sb.append(")");
        try { 
            jsonObject.put("agentId", obj[0]);
            jsonObject.put("agentData", sb.toString()); 
        } catch (JSONException ex) { 
        }
        return jsonObject;
    }
    
    public String buildLocalityJson(Object... data) {
            
        jsonObject = new JSONObject();
        try {
            if(data != null) {
                jsonObject.put("localityId", data[0]);
                jsonObject.put("locality", data[1]);
                jsonObject.put("latitude", data[2]);
                jsonObject.put("longitude", data[3]);
                jsonObject.put("shortName", data[4]);
            } else {
                jsonObject.put("localityId", 0);
            } 
        } catch (JSONException ex) { 
            return null;
        }
        return jsonObject.toString();
    }

    public String buildCollectingEventJson(Object... data) {

        jsonObject = new JSONObject();
        try {
            if (data != null) {
                jsonObject.put("sfn", data[0]);
                jsonObject.put("startDate", data[1]);
                jsonObject.put("endDate", data[2]);
                jsonObject.put("ceId", data[3]);
                jsonObject.put("locality", data[4]);
                jsonObject.put("latitude", data[5]);
                jsonObject.put("longitude", data[6]);  
                jsonObject.put("shortName", data[7]);  
            } else {
                jsonObject.put("ceId", 0);
            }
        } catch (JSONException ex) {
            return null;
        }
        return jsonObject.toString();
    }
}
