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
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;
import javax.ejb.EJB;  
import javax.ejb.Stateless;     
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  
import se.nrm.dina.data.exceptions.DinaException;
import se.nrm.dina.data.jpa.SMTPDao;
import se.nrm.dina.datamodel.impl.Agent;
import se.nrm.dina.datamodel.impl.Attributedef;
import se.nrm.dina.datamodel.impl.Collectingevent;
import se.nrm.dina.datamodel.impl.Collection;
import se.nrm.dina.datamodel.impl.Collectionobject;
import se.nrm.dina.datamodel.impl.Collectionobjectattr;
import se.nrm.dina.datamodel.impl.Determination;
import se.nrm.dina.datamodel.impl.Discipline;
import se.nrm.dina.datamodel.impl.Preparation;
import se.nrm.dina.datamodel.impl.Preptype;
import se.nrm.dina.datamodel.impl.Taxon; 
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
    private Preptype preptype;
    private Collectingevent event;
    private Collection collection;
    private Discipline discipline; 
    private List<Determination> determinations;
    private List<Preparation> prepartions;
   
    
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
     
//    public void getTaxonTree(String parent) {
//        String strQuery = QueryStringBuilder.getInstance().buildTaxonTree(parent),
//
//        List<CommonVO> results = dao.getListByJPQL(strQuery);
//        List<CommonVO> commonVos = new ArrayList();
//    }
// 
    public void upload(String json) {
        logger.info("upload ");

        createdBy = (Agent) dao.findByReference(Util.getInstance().getAgentId(), Agent.class);
        timestamp = new Timestamp(System.currentTimeMillis()); 
        collection = (Collection) dao.findByReference(Util.getInstance().getCollectionId(), Collection.class);
        discipline = (Discipline) dao.findByReference(Util.getInstance().getDisciplineId(), Discipline.class);
         
        int lastCatalogNumber = buildNextCatalognumberByCollection();

        List<Collectionobject> collectionList = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(json);
            String fileName = jsonObj.getString("excelfilename");
            int preparedById = jsonObj.getInt("preparaedById");
            String preparedByDate = jsonObj.getString("preparedDate");
              
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
                            
                            String determinedDate = coObject.getString("determinedDate");
                            
                            String guid = coObject.getString("guid");
                            String fullName = coObject.getString("computedName");
                            if(taxonMap.containsKey(fullName)) {
                               taxon = taxonMap.get(fullName); 
                            } else {
//                                if(guid.contains("dyntaxa")) {
//                                    taxon = getTaxonFromDBWithGuid(fullName, guid);
//                                } else {
//                                    taxon = getTaxonFromDB(fullName);
//                                } 
                                taxon = getTaxonFromDB(fullName);
                                taxonMap.put(fullName, taxon);
                            }
                             
                            String media = coObject.getString("media");
                            if(media != null && !media.trim().isEmpty()) {
                                if(preptypeMep.containsKey(media)) {
                                    preptype = preptypeMep.get(media);
                                } else {
                                    preptype = getPreptype(media);
                                    if (preptype == null) {
                                        preptype = createPreptype(media);
                                    }  
                                    preptypeMep.put(media, preptype); 
                                }
                            }
                              
                            int numOfMales = coObject.getInt("numOfMales");
                            int numOfFemales = coObject.getInt("numOfFemales");
                            int total = coObject.getInt("total"); 
                             
                            String storage = coObject.getString("storage"); 
                            String remark =  coObject.getString("remark");
                            
                            Collectionobject co = new Collectionobject();
                            co.setCreatedByAgentID(createdBy);
                            
                            
                            co.setCatalogNumber(catalognumber);
                            co.setTimestampCreated(timestamp); 
                            co.setRemarks(remark); 
                            co.setCatalogedDate(timestamp);
                            co.setCatalogedDatePrecision(Short.valueOf("1"));
                            co.setCollectingEventID(event);
                            co.setCatalogerID(createdBy);
                            co.setCollectionMemberID(Util.getInstance().getCollectionId());
                            co.setCollectionID(collection);
                            co.setReservedText3(fileName);
                             
                            co.setGuid(Util.getInstance().generateGUID().toString()); 
                            
                            
                            int numOfUnknown = getNumOfUnknown(total, numOfMales, numOfFemales);
                            List<Collectionobjectattr> coAttrs = new ArrayList<>();
                            
                            if(numOfMales > 0) {
                                Collectionobjectattr males = addAttrs(numOfMales, "Male");
                                males.setCollectionObjectID(co);
                                coAttrs.add(males); 
                            }
                            if(numOfFemales > 0) {
                                Collectionobjectattr females = addAttrs(numOfFemales, "Female");
                                females.setCollectionObjectID(co);
                                coAttrs.add(females); 
                            }
                            
                            if(numOfUnknown > 0) {
                                Collectionobjectattr unknown = addAttrs(numOfUnknown, "Unknown");
                                unknown.setCollectionObjectID(co);
                                coAttrs.add(unknown); 
                            } 
                            if(coAttrs.size() > 0) {
                                co.setCollectionobjectattrList(coAttrs);
                            }
  
                            determinations.add(buildDetermination(determinedDate, co));
                            co.setDeterminationList(new HashSet(determinations));
                            
                            if(preptype != null) {
                                prepartions.add(buildPreparation(total, storage, co));
                                co.setPreparationList(new HashSet<>(prepartions));
                            } 
                            collectionList.add(co);

                        } catch (JSONException | NumberFormatException ex) {
                            logger.error(ex.getMessage());
                        }
                    }); 
            
        } catch (JSONException ex) { 
        }
        
        dao.bacthCreate(collectionList); 
    }
    
    private Attributedef createNewAttributedef() {
        Attributedef attributDef = new Attributedef();
        attributDef.setCreatedByAgentID(createdBy);
        attributDef.setDisciplineID(discipline);
        attributDef.setTimestampCreated(timestamp);   
        attributDef.setPrepTypeID(preptype);
        return attributDef;
    }
    
    private Collectionobjectattr addAttrs(int numOfSpecimens, String sex) {
         
        Collectionobjectattr coAttr = new Collectionobjectattr(); 
        coAttr.setCreatedByAgentID(createdBy);
        coAttr.setTimestampCreated(timestamp); 
        coAttr.setCollectionMemberID(Util.getInstance().getCollectionId()); 
        coAttr.setAttributeDefID(createNewAttributedef());  
        coAttr.setStrValue(sex);
        coAttr.setDoubleValue((double) numOfSpecimens); 
        return coAttr;
    }
    
//    private Set<Collectionobjectattr> addNewCollectionObjectAttrs(Collectionobject co, int total, int numOfMales, int numOfFemales) { 
//        Set<Collectionobjectattr> coAttrs = new HashSet();
//        
//          
//        logger.info("mail : {} -- {}", numOfMales, numOfFemales + " --- " + numOfUnknown );
//        if(numOfFemales != 0) { 
//            Collectionobjectattr coAttrFemail = addCollectionObjectAtt("Female", numOfFemales);
//            coAttrFemail.setCollectionObjectID(co); 
//            coAttrs.add(coAttrFemail); 
//            logger.info("whay 3 : {}", coAttrs.size());
//        }
//        
//        if(numOfMales != 0) { 
//            Collectionobjectattr coAttrMale = addCollectionObjectAtt("Male", numOfMales);
//            coAttrMale.setCollectionObjectID(co);
//            coAttrs.add(coAttrMale);
//        }
//        
//        if(numOfUnknown != 0) { 
//            Collectionobjectattr coAttr = addCollectionObjectAtt("Unknown", numOfUnknown);
//            coAttr.setCollectionObjectID(co);
//            coAttrs.add(coAttr);
//        }  
//        
//        logger.info("why 2 {}", coAttrs.size());
//        return coAttrs;
//    }
    
//    private Collectionobjectattr addCollectionObjectAtt(String sex, int numOfSpecimens) {
//        
//        Attributedef attrDef = createNewAttributedef();
//        Collectionobjectattr coAttr = new Collectionobjectattr(); 
//        coAttr.setCreatedByAgentID(createdBy);
//        coAttr.setTimestampCreated(timestamp); 
//        coAttr.setCollectionMemberID(Util.getInstance().getCollectionId()); 
//        coAttr.setAttributeDefID(attrDef);  
//        coAttr.setStrValue(sex);
//        coAttr.setDoubleValue((double) numOfSpecimens);
//    
//        return coAttr;
//    }
    
    private int buildNextCatalognumberByCollection() {
 
        String lastCatalogNumber = dao.getLastCatalogunumber(
                                        QueryStringBuilder.getInstance()
                                                .buildGetLastCatalogNumber(Util.getInstance().getCollectionId()));
         
        return lastCatalogNumber == null ? 0 : Integer.parseInt(lastCatalogNumber);
    }
    
    private Determination buildDetermination(String determinedDate, Collectionobject co) {
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
        determination.setCollectionObjectID(co);
        return determination;
    }
    
 
    
    private Preparation buildPreparation(int total, String storage, Collectionobject co) {
        Preparation preparation = new Preparation();
        preparation.setCreatedByAgentID(createdBy);
        preparation.setTimestampCreated(timestamp);
        preparation.setCollectionMemberID(Util.getInstance().getCollectionId());
        preparation.setCountAmt(total);
        preparation.setPreparedDate(timestamp);
        preparation.setPrepTypeID(preptype);
        preparation.setPreparedByID(createdBy);
        preparation.setPreparedDatePrecision((short) 1);
        preparation.setCollectionObjectID(co);
        
        if(storage.length() > 50) {
            storage = StringUtils.substring(storage, 0, 50);
        }
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
        
         
        if(fullName.contains("sp.") && fullName.endsWith("sp.")) {
            fullName = StringUtils.replace(fullName, "sp.", "").trim();
        }
        int count = dao.getCountByJPQL(QueryStringBuilder.getInstance()
                                    .buildGetTaxon(fullName));
        
        if(count == 1) {
            return (Taxon) dao.getEntityByJPQL(
                                QueryStringBuilder.getInstance()
                                    .buildGetTaxon(fullName));
        } else if(count == 0) {
            throw new DinaException("no result");
        } else if(count > 1) {
            return (Taxon) dao.getEntityByJPQL(QueryStringBuilder.getInstance().buildGetTaxon(fullName, 11));
        } 
        return null;
    }
    
//    private Taxon getTaxonFromDBWithGuid(String fullName, String guid) {
//        return (Taxon) dao.getEntityByJPQL(
//                                QueryStringBuilder.getInstance()
//                                    .buildGetTaxonWithGuid(fullName, guid));
//    }
    
    private Agent getDeterminerFromDB(String agentName) {
        
        String firstName = agentName.split(" ")[0];
        String lastName = getLastName(agentName);
        if(agentName.equals("M Forshage")) {
            firstName = "Mattias";
            lastName = "Forshage";
        }
        
        if(agentName.equals("Mårtem Eriksson")) {
            firstName = "Mårten";
            lastName = "Eriksson";
        }
         
        if(agentName.equals("Jan Willem A van Zuijlen")) {
            firstName = "Jan";
            lastName = "van Zuijlen";
        }
        
        if(agentName.equals("Mareike Kiupel, 2014")) {
            firstName = "Mareike";
            lastName = "Kiupel";
        }
        
        if(agentName.equals("R. Hovmöller")) {
            firstName = "Rasmus";
            lastName = "Hovmöller";
        }
        
        if(agentName.equals("KA Johanson")) {
            firstName = "Kjell Arne";
            lastName = "Johanson";
        }
        
        if(agentName.equals("J. Kjærandsen")) {
            firstName = "Jostein";
            lastName = "Kjærandsen";
        }
        
        if(agentName.equals("S. Martinsson")) {
            firstName = "Svante";
            lastName = "Martinsson";
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
