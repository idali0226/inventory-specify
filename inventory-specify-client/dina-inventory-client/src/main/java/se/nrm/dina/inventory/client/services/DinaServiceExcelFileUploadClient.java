/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.inventory.client.services;
    
//import com.thoughtworks.xstream.XStream; 
//import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import java.util.ArrayList;  
import java.util.HashMap; 
import java.util.List; 
import java.util.Map;     
import java.util.stream.IntStream;
import javax.ejb.Stateless; 
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest; 
import javax.ws.rs.core.Response;  
import org.codehaus.jettison.json.JSONArray; 
import org.codehaus.jettison.json.JSONException; 
import org.codehaus.jettison.json.JSONObject; 
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget; 
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;   
import se.nrm.dina.inventory.client.vo.TreeTaxa;

/**
 *
 * @author idali
 */
@Stateless
public class DinaServiceExcelFileUploadClient {
    
    private final Logger logger = LoggerFactory.getLogger(DinaServiceExcelFileUploadClient.class);

    private static ResteasyClient client;
    private static ResteasyWebTarget target;
    private static final String BASE_LOCAL_URL = "http://localhost:8080/inventoryservice/service/";
//    private static final String BASE_REMOTE_URL = "http://dina-loans/inventoryservice/service/";
    private static final String BASE_REMOTE_URL = "https://www.dina-web.net/inventoryservice/service/";
    
    private static String servername;
    private final static String LOCAL_HOST = "localhost";
    private static String BASE_URL;
 

    public DinaServiceExcelFileUploadClient() {  
        ResteasyClientBuilder clientBuilder = new ResteasyClientBuilder();
        clientBuilder.connectionPoolSize(32); 
        
        
        if(client == null) {
//            client = new ResteasyClientBuilder().build(); 
            client = clientBuilder.build(); 
        }
         
        if(BASE_URL == null) {
            servername = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
            logger.info("server name : {}", servername); 
            BASE_URL = servername.contains(LOCAL_HOST) ? BASE_LOCAL_URL : BASE_REMOTE_URL;
        } 
    }
  
    /**
     * Verify that catalognumber is not in database
     * @param catalognumber
     * @param collectionId
     * @return boolean
     */
    public boolean isCatalogNumeExistInDb(String catalognumber, int collectionId) {
        logger.info("isCatalogNumeExistInDb: {} -- {}", catalognumber, collectionId);
        
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append("catalognumber/search/"); 
        sb.append(catalognumber); 
        sb.append("/");
        sb.append(collectionId);
        
        target = client.target(sb.toString());
        Response response = target.request().get();
        Integer count = response.readEntity(Integer.class);
        
        
        response.close(); 
        return count == 0;
    }
    
    
    /**
     * To validate collectingevent by event id
     * @param eventId
     * @param disciplineId
     * @return collectingeventId
     */
    public int validateEventByEventId(int eventId, int disciplineId) {
//        logger.info("validateEventByEventId : {}", eventId);
        
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append("collectingevent/search/"); 
        sb.append(eventId); 
        sb.append("/");
        sb.append(disciplineId);
        
        target = client.target(sb.toString());
          
        Response response = target.request().get(); 
        Integer result = response.readEntity(Integer.class);
        response.close();
        return result;
    }

    public int getDeterminerId(String firstname, String lastname) {
        logger.info("getDeterminerId : {} - {}", firstname, lastname);
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append("agent/search/"); 
        sb.append(firstname);
        sb.append("/");
        sb.append(lastname);

        target = client.target(sb.toString());
        Response response = target.request().get();
 
        Integer result = response.readEntity(Integer.class);
           
        response.close();
        return result;
//        if(result != null && result != 0) {
//            
//        }
//        return -1;
    }
    
    public Map<String, Integer> getAgents() {
        
        logger.info("getAgent");
        
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append("agent/smtp/search/"); 
        
        Map<String, Integer> results = new HashMap();
        try {
            target = client.target(sb.toString());
            Response response = target.request().get();
 
            String json = response.readEntity(String.class);  
            int agentId;
            String agentData;
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                agentId = arr.getJSONObject(i).getInt("agentId");
                agentData = arr.getJSONObject(i).getString("agentData");
                results.put(agentData, agentId);
            }
            response.close();
        } catch (JSONException ex) {
            logger.error(ex.getMessage());
        }
        return results;
    }
    
    
//    public String submitObservations(int agentId, int collectionId, ExcelData excelData) {
//        logger.info("submitObservations : {}", agentId);
//        
//        XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
//        xstream.setMode(XStream.NO_REFERENCES);
//        xstream.alias("data", ExcelData.class); 
//        
//        Entity<String> someEntity = Entity.entity(xstream.toXML(excelData), MediaType.APPLICATION_JSON);
//  
//        target = client.target(buildPath("observation", agentId, collectionId));
//        Invocation.Builder invocationBuilder = target.request("text/plain").header("header", "header");
//        Invocation invocation = invocationBuilder.buildPost(someEntity);
//        Response response = invocation.invoke();
//        if(response.getStatus() == 201) {
//            response.close();
//            return "Error"; 
//        }
//         
//        String result = response.readEntity(String.class);  
//        response.close();
//        return result; 
//    }
    
//    public String submitTaxon(int agentId, ExcelData excelData) {
//        
//        logger.info("submitTaxon");
// 
//        XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
//        
//        xstream.setMode(XStream.NO_REFERENCES);
//        xstream.alias("data", ExcelData.class); 
//         
//        Entity<String> entity = Entity.entity(xstream.toXML(excelData), MediaType.APPLICATION_JSON);
// 
//        target = client.target(buildPath("taxon", agentId, -1));
//        Invocation.Builder invocationBuilder = target.request("text/plain").header("header", "header");
//        Invocation invocation = invocationBuilder.buildPost(entity);
//        Response response = invocation.invoke();
//        if(response.getStatus() == 201) {
//            response.close();
//            return "Error"; 
//        }
//  
//        String result = response.readEntity(String.class);   
//        
//        response.close();
//        return result;
//    }
    
    private String buildPath(String entity, int agentId, int collectionId) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append(entity);
        sb.append("/post/");
        sb.append(agentId);
        if(collectionId != -1) {
            sb.append("/");
            sb.append(collectionId);
        }
        return sb.toString();
    }
    
    public Map<String, Integer> getMatchedTaxon(String query, int treeDefId) { 
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append("taxa/search/");
        sb.append(query);
        sb.append("/");
        sb.append(treeDefId);
         
        Map<String, Integer> map = new HashMap();
        try {
            target = client.target(sb.toString());
            Response response = target.request().get();
 
            String json = response.readEntity(String.class); 
            JSONArray arr = new JSONArray(json); 
            IntStream.range(0, arr.length())   
                    .forEach(i -> {
                        try {
                            JSONObject jsonObj = arr.getJSONObject(i);
                            int taxonId = jsonObj.getInt("taxonId"); 
                            String taxon = jsonObj.getString("taxonName") + " [" + jsonObj.getString("guid") + "] "; 
                            map.put(taxon, taxonId);
                        } catch (JSONException ex) {
                            logger.error(ex.getMessage());
                        } 
                    }); 
            response.close();
        } catch (JSONException ex) {
            logger.error(ex.getMessage());
        } 
        return map;
    }
    
    public Map<String, String> getTaxonDyntaxaId(int dyntaxaId, int treeDefId) {
         
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append(dyntaxaId);
        sb.append("/");
        sb.append(treeDefId);

        target = client.target(sb.toString());
        Response response = target.request().get();
        String jsonString = response.readEntity(String.class);
            
        Map<String, String> map = new HashMap(); 
        try {
            JSONObject json = new JSONObject(jsonString);
            if(!json.isNull("taxonId")) {
                map.put("taxonId", json.getString("taxonId"));
                map.put("taxonName", json.getString("taxonName"));
                map.put("guid", json.getString("guid")); 
            } 
        } catch (JSONException ex) {
            logger.error(ex.getMessage());
        }
        response.close();
        return map; 
    }
 
    
    public Map<String, String> getDyntaxaByName(String name, int treeDefId) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append("dyntaxa/");
        sb.append(name);
        sb.append("/");
        sb.append(treeDefId);

        target = client.target(sb.toString());
        Response response = target.request().get(); 
        
        Map<String, String> map = new HashMap(); 
        if(response.getStatus() == 204) {
            return map;
        } 
        
        String jsonString = response.readEntity(String.class); 
        try {
            JSONObject json = new JSONObject(jsonString);
            if(!json.isNull("taxonId")) {
                map.put("taxonId", json.getString("taxonId")); 
                map.put("guid", json.getString("guid")); 
            } 
        } catch (JSONException ex) {
            logger.error(ex.getMessage());
        }
        response.close();
        return map; 
    }
    
    public int isOrderAndUp(String name, int treeDefId) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL); 
        sb.append("taxon/rank/");
        sb.append(name);
        sb.append("/");
        sb.append(treeDefId);
        
        target = client.target(sb.toString());
        Response response = target.request().get(); 
        
        int i = response.readEntity(Integer.class); 
        response.close();
        return i;
    }



    public List<TreeTaxa> getChildren(String name, String treeName, int treeDefId) {
//        logger.info("getChildren : {}", name);

        List<TreeTaxa> children = new ArrayList();

        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append(treeName);
        sb.append("/");
        sb.append(name);
        sb.append("/");
        sb.append(treeDefId);
        try {
            target = client.target(sb.toString());
            Response response = target.request().get();

            //Read output in string format
            String json = response.readEntity(String.class);

            String taxon;
            List<String> syns;
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                taxon = arr.getJSONObject(i).getString("name") + " [" + arr.getJSONObject(i).getString("guid") + "] ";
                syns = new ArrayList();
                JSONArray jsonArray = arr.getJSONObject(i).getJSONArray("list");
                for (int j = 0; j < jsonArray.length(); j++) {
                    syns.add(jsonArray.getString(j));
                }   
                children.add(new TreeTaxa(taxon, false, syns));
            }
            response.close();
        } catch (JSONException ex) {
            logger.error(ex.getMessage());
        }
        return children;
    }
 
    public Map<Integer, String> getTraps(String excelfinename, int collection) {
        logger.info("getTraps");
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append("trap/");
        sb.append(excelfinename);
        sb.append("/");
        sb.append(collection);

        target = client.target(sb.toString());
        Response response = target.request().get();
        String json = response.readEntity(String.class);
         
        int localityId;
        String locality; 
        Map<Integer, String> results = new HashMap();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) { 
                localityId = arr.getJSONObject(i).getInt("localityId");
                locality = arr.getJSONObject(i).getString("locality"); 
                results.put(localityId, locality);
            }
        } catch (JSONException ex) {
            logger.error(ex.getMessage());
        }  
        response.close();
        return results;

    }
    
    public Map<Integer, String> getEvents(String excelfinename, int locality) {

        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append("event/");
        sb.append(excelfinename);
        sb.append("/");
        sb.append(locality);

        target = client.target(sb.toString());
        Response response = target.request().get();
        String json = response.readEntity(String.class);
        
        int eventID;
        String eventDate; 
        Map<Integer, String> results = new HashMap();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) { 
                eventID = arr.getJSONObject(i).getInt("eventId");
                eventDate = arr.getJSONObject(i).getString("event");
                 
                results.put(eventID, eventDate);
            }
        } catch (JSONException ex) {
            logger.error(ex.getMessage());
        }  
        response.close();
        return results;
    }
    
    public Map<Integer, String> getCollectionObjectsByEventId(String excelfinename, int event, int collection) {

        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append("collectionobject/");
        sb.append(excelfinename);
        sb.append("/");
        sb.append(event);
        sb.append("/");
        sb.append(collection);

        target = client.target(sb.toString());
        Response response = target.request().get();
    
        String json = response.readEntity(String.class);  
        int coId;
        String catalognumber; 
        String taxafullname;
        Map<Integer, String> results = new HashMap();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                sb = new StringBuilder();
                
                coId = arr.getJSONObject(i).getInt("coId");
                catalognumber = arr.getJSONObject(i).getString("catalognum");
                taxafullname = arr.getJSONObject(i).getString("taxon"); 
                
                sb.append("Catalognum: ");
                sb.append(catalognumber);
                sb.append(" [");
                sb.append(taxafullname);
                sb.append("] ");
                 
                results.put(coId, sb.toString());
            }
        } catch (JSONException ex) {
            logger.error(ex.getMessage());
        }  
        response.close();
        return results;
    }
    
    public boolean deleteCos(String coId, String fileName) {
        logger.info("deleteCos : {} -- {}", coId, fileName);
        
        
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append("delete/collectionobject/");
        sb.append(fileName);
        sb.append("/");
        sb.append(coId);
        
        target = client.target(sb.toString());
        Response response = target.request().delete();
        int status = response.getStatus();
        response.close();
        return status == 201;
    }

    public boolean deleteTaxon(String taxonIds) {
//        logger.info("deleteTaxon : {}", taxonIds);
        logger.info("deleteTaxon");
        
        int status = 201;
        if(taxonIds != null && taxonIds.length() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(BASE_URL);
            sb.append("delete/taxon/");
            sb.append(taxonIds);

            target = client.target(sb.toString());
            Response response = target.request().delete();
            status = response.getStatus();
            response.close();
        }
        
        return status == 201;
    }
    
    /**
     * This method is only used for test version
     * @return String -- result
     */
    public boolean deleteData() {
        logger.info("deleteData"); 
 
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append("delete"); 
        
        target = client.target(sb.toString());
        Response response = target.request().delete();
        int status = response.getStatus();
        response.close();
        return status == 201;
    }
}
