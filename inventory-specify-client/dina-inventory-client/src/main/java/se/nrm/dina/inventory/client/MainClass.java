/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.inventory.client;
 
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response; 
import org.codehaus.jettison.json.JSONException;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import org.codehaus.jettison.json.JSONObject; 

/**
 *
 * @author idali
 */
public class MainClass {
    
    private static ResteasyClient client;
    private static ResteasyWebTarget target;
    private static final String LOCAL_URL = "http://localhost:8080/inventoryservice/service/taxon/Insecta/11";
    
    
    public static void main(String[] args) {
        client = new ResteasyClientBuilder().build();
        target = client.target(LOCAL_URL);
 
        testget();
        testpost();
    }

    private static void testget() {
        target = client.target("http://localhost:8080/vegadare/result/28");
        Response response = target.request().get();
        System.out.println("status : " + response.getStatus());
        
        response.close();
    }
    
    private static void testpost() {
        
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("favorite", "Apple");
            
            StringBuilder sb = new StringBuilder();
            sb.append("<user id=\"0\">");
            sb.append("   <username>Test User</username>");
            sb.append("   <email>test.user@test.com</email>");
            sb.append("</user>");
            
            target = client.target("http://localhost:8080/vegadare/users");
            
            Response response = target.request().post(Entity.json(jsonObj.toString()));
            System.out.println("status : " + response.getStatus());
            
            response.close();
        } catch (JSONException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
    
    
    

}
