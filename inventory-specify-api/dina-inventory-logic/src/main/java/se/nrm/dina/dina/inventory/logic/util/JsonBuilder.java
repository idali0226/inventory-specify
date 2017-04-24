/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dina.inventory.logic.util;

import java.util.List; 
import java.util.stream.Collectors; 
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.datamodel.impl.Taxon;

/**
 *
 * @author idali
 */
public class JsonBuilder {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private JSONObject jsonObject;
    private static JsonBuilder instance = null;

    public static synchronized JsonBuilder getInstance() {
        if (instance == null) {
            instance = new JsonBuilder();
        }
        return instance;
    }
    
    public String buildTaxonJson(List<Taxon> taxons) { 
  
        List<JSONObject> list = taxons.stream()
                                       .map(this::buildJson)
                                       .collect(Collectors.toList()); 
        return list.toString();
    }
    
    private JSONObject buildJson(Taxon taxon) {
        jsonObject = new JSONObject();
         
        try { 
            jsonObject.put("taxonId", taxon.getTaxonID());
            jsonObject.put("scientificName", taxon.getFullName()); 
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
}
