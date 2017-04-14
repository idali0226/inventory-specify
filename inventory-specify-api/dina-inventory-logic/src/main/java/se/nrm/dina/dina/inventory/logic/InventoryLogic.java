/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dina.inventory.logic;
 
import java.io.Serializable;
import java.sql.Timestamp; 
import java.util.List;
import javax.ejb.EJB; 
import javax.ejb.Stateless;    
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  
import se.nrm.dina.data.jpa.SMTPDao;
import se.nrm.dina.dina.inventory.logic.util.JsonBuilder;
import se.nrm.dina.dina.inventory.logic.util.QueryStringBuilder;

/**
 *
 * @author idali
 */
@Stateless
public class InventoryLogic implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private Timestamp timestamp;
 
    @EJB
    private SMTPDao dao;
     
    public InventoryLogic() {

    }

    public String getSmtpAgentList() {
        logger.info("getSmtpAgentList");
        List<Object[]> results = dao.getSearchResultsByJPQL(
                QueryStringBuilder.getInstance().buildGetSmtpAgentList());
        return JsonBuilder.getInstance().buildAgentList(results);
    }

    public void cleanUp() {
        dao.deleteOldSMTPData();
    }


    
    
    
    
    
    
    
    
    
    
    

//    
//    public int getSmtpAgent(String firstName, String lastName) {
//        logger.info("getSmtpAgent");
//
//        String namedQuery = "Agent.findByName";
//        Map<String, Object> map = new HashMap<>();
//        map.put("firstName", firstName);
//        map.put("lastName", lastName);
//        map.put("agentType", (short)1); 
//
//        List<Agent> agents = dao.getEntitiesByNamedQuery(namedQuery, map);
//        if (agents == null || agents.isEmpty()) {
//            return 0;
//        }
//        return agents.get(0).getAgentID();
//    }
//    
//    public int validateTaxon(String taxonName, int taxonTreeDefId) {
//        logger.info("validateTaxon"); 
//        return dao.getCountByQuery(QueryStringBuilder.getInstance().buildTaxonCountQuery(taxonName, taxonTreeDefId)); 
//    }
//    
//    
//    
//    public String getEventsByLocalityId(int localityId) { 
//        List<Object[]> results = dao.getSearchResultsByJPQL(QueryStringBuilder.getInstance()
//                                        .buildGetCollectingEventsByLocalityId(localityId));    
//        return JsonBuilder.getInstance().buildCollectingEventListJson(results); 
//    }
//    
//    public String getEventByEventId(int eventId, int disciplineId) {
//        logger.info("getEventByEventId : {} -- {}", eventId, disciplineId);
//         
//        Object[] results = dao.getListOfDataByJPQL(QueryStringBuilder.getInstance()
//                                    .buildGetCollectingEventByEventIdAndDisciplineId(eventId, disciplineId));   
//        return JsonBuilder.getInstance().buildCollectingEventJson(results);
//    }
//    
//    public String getTrapByTrapId(int trapId, int disciplineId) {
//        logger.info("getTrapByTrapId : {} -- {}", trapId, disciplineId); 
//        return JsonBuilder.getInstance().buildLocalityJson(
//                            dao.getListOfDataByJPQL(QueryStringBuilder.getInstance()
//                                    .buildGetLocalityByTrapIdAndDisciplineId(trapId, disciplineId)));
//    }
//    
//    public int getDeterminationCount(String taxonName, int collectingEventId, int CollectionId) {
//        return dao.getCountByQuery(QueryStringBuilder.getInstance()
//                                                .buildNumOfDeterminationsQuery(taxonName, collectingEventId, CollectionId));
//    }
//    
//    
//    public String getTaxonAndCollectingeventCount(int localityId) {
//        int ceCount = dao.getCountByQuery(QueryStringBuilder.getInstance()
//                                        .buildCollectingEventCountByLocalityId(localityId)); 
//         
//        List<Object[]> results = dao.getSearchResultsByJPQL(
//                                    QueryStringBuilder.getInstance()
//                                            .buildTaxonCountByLocalityId(localityId));   
//        
//        return JsonBuilder.getInstance().buildTaxonAndEventCountJson(ceCount, results); 
//    }
//    
//    public List<String> getTaxonNameList(String searchValue, int treeDefId) { 
//        List<String> results = dao.getStringListByJPQL(
//                                    QueryStringBuilder.getInstance()
//                                            .buildGetTaxonListQuery(searchValue, treeDefId));
//        return results;
//    }
//             
//    public String getTaxonCount(int eventId) {
//        logger.info("getDataByEventId : {}", eventId);
//   
//        List<Object[]> results = dao.getSearchResultsByJPQL(
//                                    QueryStringBuilder.getInstance()
//                                        .buildTaxonCountByCollectingeventId(eventId));   
//        
//        return JsonBuilder.getInstance().buildTaxonAndEventCountJson(1, results);
//    }
//     
//    public List<EntityBean> findAll(String className) {
//        return dao.findAll(Collection.class);
//    }
//    
//    public String upload(String json) {
//        try {
//            JSONObject jsonObj = new JSONObject(json);
//            int ceId = jsonObj.getInt("ceId");
//            int collectionId = jsonObj.getInt("collectionId");
//            int disciplineId = jsonObj.getInt("disciplineId");
//            int smtpAgentId = jsonObj.getInt("smtpAgentId");
//            String sortingDate = jsonObj.getString("sortingDate");
//            int numOfDeaccessioned = jsonObj.getInt("numOfDeaccesioned");
//            String group = jsonObj.getString("group");
//            String loggedInUser = jsonObj.getString("loggedInUser");
//            
//            Collectingevent ce = (Collectingevent) dao.findByReference(ceId, Collectingevent.class);
//            Collection collection = (Collection) dao.findByReference(collectionId, Collection.class);
//            Discipline discipline = (Discipline) dao.findByReference(disciplineId, Discipline.class);
//            Agent smtpAgent = (Agent) dao.findByReference(smtpAgentId, Agent.class);
//            Agent loggedInAgent = getAgentByUserName(loggedInUser);
//            
//            
//            
//            JSONArray jsonEventArray = jsonObj.getJSONArray("samples");
//            
//        } catch (JSONException ex) { 
//        }
//        return null;
//    }
//
//    private Agent getAgentByUserName(String userName) {
//         
//        if(userName.equals("idali")) {
//            return (Agent) dao.findByReference(5945, Agent.class);
//        } else {
//            Map<String, Object> map = new HashMap<>();
//            map.put("name", userName);
//
//            String namedQuery = "Agent.findBySpecifyUserName";
//            return (Agent) dao.getEntityByNamedQuery(namedQuery, map);  
//        } 
//        
//    }
//
//    public boolean login(String json) {
//        logger.info("login");
//        
//        try { 
//            JSONObject data  = new JSONObject(json);
//            String username = data.getString("username"); 
//            boolean login = data.getBoolean("login");
//            
//            timestamp = new Timestamp(System.currentTimeMillis());
//            Map<String, Object> map = new HashMap<>();
//            map.put("name", username);
//            map.put("isLoggedIn", login);
//            map.put("loginOutTime", timestamp);
//            map.put("timestampModified", timestamp);
//             
//            String namedQuery = "Specifyuser.updateUser";
//            return dao.updateByNamedquery(namedQuery, map); 
//        } catch (JSONException ex) {
//             throw new DinaException(ex.getMessage(), ErrorMsg.getInstance().getBadRequestCode());
//        }
//    }
    
}
