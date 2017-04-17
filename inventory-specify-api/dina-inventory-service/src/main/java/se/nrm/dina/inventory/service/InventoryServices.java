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


//    @GET
//    @Produces("text/plain")
//    public Response doGet() {
//        logger.info("logic : {}", logic);
//        return Response.ok("Hello from WildFly Swarm!").build();
//    }

    @GET
    @Path("/agents")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAgents() {
        logger.info("getAgents");
         
        return Response.ok(logic.getSmtpAgentList()).build();
    }
 
    
    
    
//    @GET
//    @Path("/")
//    public Response getChildren(@QueryParam("parent") String parent) {
////        logger.info("getTaxonChildrenByName : {} -- {}", name, tree);
//
//        logic.getTaxonTree(parent);
//
//        StringBuilder jpqlSB = new StringBuilder();
//        jpqlSB.append("SELECT new se.nrm.dina.data.jpa.vo.CommonVO(t.taxonID, t.fullName, t.guid) FROM ");
//        jpqlSB.append(tree);
//        jpqlSB.append(" AS t WHERE t.parent.fullName = '");
//        jpqlSB.append(name);
//        jpqlSB.append("' AND t.definition.");
//        jpqlSB.append(tree.toLowerCase());
//        jpqlSB.append("TreeDefId = ");
//        jpqlSB.append(treedefid);
//        jpqlSB.append(" AND t.isAccepted = true");
//        jpqlSB.append(" ORDER BY t.fullName");
//
//        List<CommonVO> results = dao.getListByJPQL(jpqlSB.toString());
//        List<CommonVO> commonVos = new ArrayList();
//        
//        
//        
//        results.stream().forEach(r -> {
//            List<String> list = dao.getStringListByJPQL(SYN_SQL + r.getId()); 
//            commonVos.add(new CommonVO(r.getName(), r.getGuid(), list));
//        });
//
//        GenericEntity ge = new GenericEntity<List<CommonVO>>(commonVos) {
//        };
//
//        Response.ResponseBuilder rb = Response.ok(ge);
//        return rb.build();
//    }
    
    
    
    
    
    
    
    
 
    
    @POST 
    @Path("/")  
    @Consumes("application/json")
    public Response uploadData(String json) { 
        logger.info("upload " );
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
