/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dina.inventory.logic;
 
import java.io.Serializable;
import java.sql.Timestamp;   
import java.util.ArrayList;  
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import javax.ejb.EJB;  
import javax.ejb.Stateless;     
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  
import se.nrm.dina.data.jpa.SMTPDao;
import se.nrm.dina.datamodel.impl.Agent;
import se.nrm.dina.datamodel.impl.Collectingevent;
import se.nrm.dina.datamodel.impl.Collection;
import se.nrm.dina.datamodel.impl.Collectionobject;
import se.nrm.dina.datamodel.impl.Determination;
import se.nrm.dina.datamodel.impl.Preparation;
import se.nrm.dina.datamodel.impl.Preptype;
import se.nrm.dina.datamodel.impl.Taxon;
import se.nrm.dina.datamodel.impl.Taxontreedef;
import se.nrm.dina.dina.inventory.logic.util.JsonBuilder;
import se.nrm.dina.dina.inventory.logic.util.QueryStringBuilder;
import se.nrm.dina.dina.inventory.logic.util.Util;

/**
 *
 * @author idali
 */
@Stateless
public class InventoryLogic implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private static final String NAMED_QUERY_GET_AGENT_BY_NAME = "Agent.findByAgentName";
    
    private Timestamp timestamp;
    private Agent createdBy;
    private Taxon taxon;
    private Agent determinedBy;
    private Taxontreedef taxonTreeDef;
    private Preptype preptype;
    private Collectingevent event;
    private Collection collection;
    List<Determination> determinations;
    List<Preparation> prepartions;
    
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
     
 
    public String upload(String json) {
        logger.info("upload ");

        createdBy = (Agent) dao.findByReference(Util.getInstance().getAgentId(), Agent.class);
        timestamp = new Timestamp(System.currentTimeMillis());
        taxonTreeDef = (Taxontreedef) dao.findByReference(Util.getInstance().getTaxonTreeDefId(), Taxontreedef.class);
        collection = (Collection) dao.findByReference(Util.getInstance().getCollectionId(), Collection.class);
         
        int lastCatalogNumber = buildNextCatalognumberByCollection();

        try {
            JSONObject jsonObj = new JSONObject(json);
            String fileName = jsonObj.getString("excelfilename");
            int preparedById = jsonObj.getInt("preparaedById");
            String preparedByDate = jsonObj.getString("preparedDate");
            
            logger.info("prepared data : {} --- {}", preparedById, preparedByDate);

            Map<String, Taxon> taxonMap = new HashMap<>();
            Map<String, Agent> determinerMap = new HashMap<>();
            Map<Integer, Collectingevent> cenMap = new HashMap<>(); 
            Map<String, Preptype> preptypeMep = new HashMap<>();
            
            
            
            Accumulator accumulator = new Accumulator();
            accumulator.setTotal(lastCatalogNumber);
            
            JSONArray coArray = jsonObj.getJSONArray("coDataList"); 
            IntStream.range(0, coArray.length())  
                    .forEach(i -> { 
                        determinations = new ArrayList<>();
                        prepartions = new ArrayList<>();
                        preptype = null;
                        try {
                            accumulator.increment();
                            String catalognumber = String.format("%08d", accumulator.total);
                             
                            JSONObject coObject = coArray.getJSONObject(i);
                            int eventId = coObject.getInt("eventId");  
                            if(cenMap.containsKey(eventId)) {
                                event = cenMap.get(eventId);
                            } else {
                                event = getCollectingevent(eventId);
                                cenMap.put(eventId, event);
                            }
                             
                            String determinedByName = coObject.getString("determiner"); 
                            if(determinerMap.containsKey(determinedByName)) {
                               determinedBy = determinerMap.get(determinedByName); 
                            } else {
                                determinedBy = getDeterminerFromDB(determinedByName);
                                determinerMap.put(determinedByName, determinedBy);
                            }
                            
                            int determinedDate = coObject.getInt("determinedDate");
                            String fullName = coObject.getString("computedName");
                            if(taxonMap.containsKey(fullName)) {
                               taxon = taxonMap.get(fullName); 
                            } else {
                                taxon = getTaxonFromDB(fullName);
                                taxonMap.put(fullName, taxon);
                            }
                             
                            String media = coObject.getString("media");
                            if(preptypeMep.containsKey(media)) {
                                preptype = preptypeMep.get(media);
                            } else {
                                preptype = getPreptype(media);
                                if (preptype == null) {
                                    preptype = createPreptype(media);
                                }  
                                preptypeMep.put(media, preptype); 
                            }
                            
                            
                            
                             
                            int numOfMales = coObject.getInt("numOfMales");
                            int numOfFeaales = coObject.getInt("numOfFemales");
                            int total = coObject.getInt("total"); 
                            int numOfUnknown = getNumOfUnknown(total, numOfMales, numOfFeaales);
                            
                            String storage = coObject.getString("storage"); 
                            String remark =  coObject.getString("remark");
                            
                            Collectionobject co = new Collectionobject();
                            co.setCreatedByAgentID(createdBy);
                            co.setCatalogNumber(catalognumber);
                            co.setTimestampCreated(timestamp);
                            co.setCatalogNumber(fullName);
 
                            determinations.add(buildDetermination(determinedDate));
                            prepartions.add(buildPreparation(total, storage));
                            
                            

                            co.setDeterminationList(new HashSet(determinations));


                        } catch (JSONException ex) {
                            logger.error(ex.getMessage());
                        } catch (Exception ex) {
                            logger.error(ex.getMessage());
                        }
                    }); 
            
        } catch (JSONException ex) { 
        }
        return null;
    }
    
    private int buildNextCatalognumberByCollection() {
 
        String lastCatalogNumber = dao.getLastCatalogunumber(
                                        QueryStringBuilder.getInstance()
                                                .buildGetLastCatalogNumber(Util.getInstance().getCollectionId()));
        return lastCatalogNumber == null ? 0 : Integer.parseInt(lastCatalogNumber);
    }
    
    private Determination buildDetermination(int determinedDate) {
        Determination determination = new Determination();
        determination.setCreatedByAgentID(createdBy);
        determination.setTimestampCreated(timestamp);
        determination.setCollectionMemberID(Util.getInstance().getCollectionId());
        determination.setDeterminedDate(Util.getInstance().convertStringToDate(determinedDate));
        determination.setIsCurrent(true);
        determination.setGuid(Util.getInstance().generateGUID().toString());
        determination.setTaxonID(taxon);
        determination.setPreferredTaxonID(taxon);
        determination.setDeterminerID(determinedBy);
        return determination;
    }
    
 
    
    private Preparation buildPreparation(int total, String storage) {
        Preparation preparation = new Preparation();
        preparation.setCreatedByAgentID(createdBy);
        preparation.setTimestampCreated(timestamp);
        preparation.setCollectionMemberID(Util.getInstance().getCollectionId());
        preparation.setCountAmt(total);
        preparation.setPreparedDate(timestamp);
        preparation.setPrepTypeID(preptype);
        preparation.setPreparedByID(createdBy);
        preparation.setPreparedDatePrecision((short) 1);
        preparation.setStorageLocation(storage); 
   
        return preparation;
    }
    
    
    private Preptype createPreptype(String media) { 
        preptype = new Preptype();
        preptype.setCollectionID(collection);
        preptype.setCreatedByAgentID(createdBy);
        preptype.setName(media);
        preptype.setTimestampCreated(timestamp);
        
        return (Preptype) dao.create(preptype);
    }
    
    private Preptype getPreptype(String media) { 
        return (Preptype)dao.getEntityByJPQL(QueryStringBuilder.getInstance()
                                    .buildGetPrepType(media, Util.getInstance().getCollectionId()));  
    }
    
    
    private int getNumOfUnknown(int total, int numOfMale, int numOfFemale) { 
        int numOfUnknown = total - (numOfMale + numOfFemale);
        return numOfUnknown < 0 ? 0 : numOfUnknown;
    }
    
    private Taxon getTaxonFromDB(String fullName) {
        return (Taxon) dao.getEntityByJPQL(
                                QueryStringBuilder.getInstance()
                                    .buildGetTaxon(fullName));
    }
    
    private Agent getDeterminerFromDB(String agentName) {
        
        String firstName = agentName.split(" ")[0];
        String lastName = getLastName(agentName);
        if(agentName.equals("M Forshage")) {
            firstName = "Mattias";
            lastName = "Forshage";
        }
         
        Map<String, String> map = new HashMap();
        map.put("firstName", firstName);
        map.put("lastName", lastName);

        return (Agent) dao.getEntityByNamedQuery(NAMED_QUERY_GET_AGENT_BY_NAME, map);
    }
    
    private String getLastName(String name) {
        if (name.contains(" ")) {
            int index = name.indexOf(" ");
            return name.substring(index + 1);
        }
        return name;
    }
    
    
    private Collectingevent getCollectingevent(int eventId) {
        return (Collectingevent) dao.getEntityByJPQL(
                                QueryStringBuilder.getInstance()
                                    .buildGetCollectingEventByEventIdAndDisciplineId(eventId, Util.getInstance().getDisciplineId()));
    }
     
    /**
     * Accumulator class to help accumulator long in foreach statement
     */
    public static class Accumulator {

        private int total = 0;
          
        public void increment() {
            total++;
        } 
        
        public void setTotal(int count) {
            total = count;
        }
    } 
    
}
