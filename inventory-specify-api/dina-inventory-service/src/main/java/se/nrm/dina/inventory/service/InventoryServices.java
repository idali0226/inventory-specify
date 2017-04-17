/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.inventory.service;

import java.io.Serializable; 
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes; 
import javax.ws.rs.GET; 
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces; 
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.dina.inventory.logic.InventoryLogic;
import se.nrm.dina.dina.inventory.logic.dyntaxa.DyntaxaDumpLogic;
import se.nrm.dina.dina.inventory.logic.dyntaxa.ExcelLogic; 

/**
 *
 * @author idali
 */
@Path("/rest/api/01")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN}) 
@Stateless
public class InventoryServices implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private InventoryLogic logic;
    
    @Inject 
    private DyntaxaDumpLogic dyntaxaDumpLogic;
    
    @Inject 
    private ExcelLogic excelLogic;


    @GET
    @Produces("text/plain")
    public Response doGet() {
        logger.info("logic : {}", logic);
        return Response.ok("Hello from WildFly Swarm!").build();
    }

    @GET
    @Path("/agents")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAgents() {
        logger.info("getAgents : {}", logic);
         
        return Response.ok(logic.getSmtpAgentList()).build();
    }
 
    
    @POST 
    @Path("/")  
    @Consumes("application/json")
    public Response uploadData(String json) { 
        logger.info("upload : {}", json);
        logic.upload(json);
        return Response.ok().build();
    }
 
    
    
    
    
    
    
    
    
    
    
    @POST 
    @Path("{rank}")  
    public Response readExcel(@PathParam("rank") String rank) { 
        logger.info("readExcel : {}", rank);
        
        
        switch (rank) {
            
            case "subphylum":
                dyntaxaDumpLogic.uploadSubphylum();
                break;
            case "order":
                dyntaxaDumpLogic.uploadOrders();
                break;
            case "superfamily":
                dyntaxaDumpLogic.uploadSuperfamily();
                break;
            case "family":
                dyntaxaDumpLogic.uploadFamily();
                break;
            case "subfamily":
                dyntaxaDumpLogic.uploadSubfamily();
                break;
            case "tribe":
                dyntaxaDumpLogic.uploadTribe();
                break;
            case "subtribe":
                dyntaxaDumpLogic.uploadSubtribe();
                break; 
            case "genus": 
                dyntaxaDumpLogic.uploadGenus();
                break;
            case "subgenus":
                dyntaxaDumpLogic.uploadSubgenus();
                break;
            case "species_genus":
                dyntaxaDumpLogic.uploadSpeciesOnGenus();
                break; 
            case "species_subgenus":
                dyntaxaDumpLogic.uploadSpeciesOnSubgenus(); 
                break;
            case "subspecies":
                dyntaxaDumpLogic.uploadSubspecies();
                break;
            case "synonmy":
                dyntaxaDumpLogic.uploadSynonmys();
                break;
            case "synonmy_species_1":
                dyntaxaDumpLogic.uploadSpeciesSynonmys1();
                break;
            case "synonmy_species_2":
                dyntaxaDumpLogic.uploadSpeciesSynonmys();
                break;
            case "synonmy_subspecies":
                dyntaxaDumpLogic.uploadSubspeciesSynonmys();
                break;
            case "highTaxa": 
                excelLogic.readInExcelFile(rank);
                break;
            case "newTaxa": 
                excelLogic.readInExcelFile(rank);
                break;
            case "newSynonyms": 
                excelLogic.readInExcelFile(rank);
                break;
            default: 
                break;
        }
        return Response.ok().build();
    }
    
    
//    @DELETE
//    public Response deleteOldSMTPData() {
//        
//        logger.info("delete");
//        logic.cleanUp();
//        return Response.ok().build();
//    }

}
