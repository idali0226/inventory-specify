/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.inventory.client.services;
  
import java.util.HashMap; 
import java.util.LinkedHashMap;
import java.util.Map; 
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.ejb.Stateless; 
import javax.ws.rs.client.Entity; 
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response; 
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException; 
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import se.nrm.dina.inventory.client.vo.ExcelData; 

/**
 *
 * @author idali
 */
@Stateless
public class SmtpServiceClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String BASE_SERVICE_URL = "http://localhost:8181/rest/api/01/"; 
    private static ResteasyClient client;
    private static ResteasyWebTarget target; 
 
    public SmtpServiceClient() {

        ResteasyClientBuilder clientBuilder = new ResteasyClientBuilder();
        clientBuilder.connectionPoolSize(32);
        if (client == null) {
            client = clientBuilder.build();
        }  
    }
    
    
    public Map<String, Integer> getAgents() {

        logger.info("getAgents");

        StringBuilder sb = new StringBuilder();
        sb.append(BASE_SERVICE_URL);
        sb.append("agents");

        Map<String, Integer> results = new HashMap();
        try {
            target = client.target(sb.toString());
            Response response = target.request().get();
            JSONArray array = new JSONArray(response.readEntity(String.class));
 
            IntStream.range(0, array.length())
                    .forEach(i -> {
                        try {
                            results.put(array.getJSONObject(i).getString("agentData"),
                                            array.getJSONObject(i).getInt("agentId"));
                        } catch (JSONException ex) {
                            logger.error(ex.getMessage());
                        }
                    }); 
            response.close();
        } catch (JSONException ex) {
            logger.error(ex.getMessage());
        } 
        
        return results.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)); 
    }
     
    public String upload(ExcelData data) { 
        logger.info("upload");
        
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_SERVICE_URL);  
  
        target = client.target(sb.toString()); 
        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(data)); 
       
        String responseString = response.readEntity(String.class); 
        response.close();
        
        return responseString; 
    }  
}
