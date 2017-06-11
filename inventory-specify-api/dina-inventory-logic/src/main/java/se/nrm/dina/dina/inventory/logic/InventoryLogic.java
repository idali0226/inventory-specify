/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dina.inventory.logic;
 
import java.io.Serializable;
import java.sql.Timestamp;   
import java.util.ArrayList;  
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;  
import java.util.function.Predicate;
import java.util.stream.IntStream;
import javax.ejb.EJB;  
import javax.ejb.Stateless;     
import org.apache.commons.lang.StringUtils; 
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;   
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
import se.nrm.dina.dina.inventory.logic.vo.TaxonVO;

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
    private Taxon preferdTaxon;
    private Agent determinedBy; 
    private Preptype preptype;
    private Collectingevent event;
    private Collection collection;
    private Discipline discipline;
    private List<Determination> determinations;
    private List<Preparation> prepartions;
    
    private Collectionobject co;
    private Determination determination;
    private Preparation preparation;

    private String determinedByName;
    private String firstName;
    private String lastName;
    private String determinedDate; 
    private String fullName;
    private String media;
    private String determinationRemarks;
    
    private String storage; 
    private String remark;
    private String loanNumber;
     
    
    @EJB
    private SMTPDao dao;
     
    public InventoryLogic() {
        
    }
    
    private Taxon getTaxonFromDB(String fullName) {
         
        if (fullName.trim().endsWith("sp.")) { 
            fullName = StringUtils.replace(fullName, "sp.", "").trim(); 
        } else if (fullName.equals("Olethreutes lacunana")) {
            fullName = "Olethreutes lacunanum";
        } else if(fullName.equals("Thecophora sp 1")) {
            fullName = "Thecophora";
        } else if(fullName.equals("Omphale sp in aetius-group")) {
            fullName = "Omphale";
        } else if(fullName.equals("Formica sp 1")) {
            fullName = "Formica";
        } else if(fullName.equals("Helina sp 1") || fullName.equals("Helina sp 2")) {
            fullName = "Helina"; 
        } else if(fullName.equals("Phaonia sp 1")) {
            fullName = "Phaonia";
        } else if(fullName.equals("Halictophagus sp 1")) {
            fullName = "Halictophagus";
        } else if(fullName.equals("Allantus sp 1")) {
            fullName = "Allantus";
        }

//        if (fullName.trim().endsWith("sp.")) {
//            fullName = StringUtils.replace(fullName, "sp.", "").trim();
//        } else if (fullName.equals("Barylypa propugnator")) {
//            fullName = "Barylypta propugnator";
//        else if (fullName.equals("Arachnospila sogdiana")) {
//            fullName = "Arachnospila sogdianoides";
//        } else if (fullName.equals("Coelichneumon cyaniventris")) {
//            fullName = "Coelichneumon cyaniventrus";
//        } else if (fullName.equals("Hepiopelmus variegatorius")) { 
//            fullName = "Hepiopelmus variegatorus";
//        } else if (fullName.equals("Homotropus frontorius")) {
//            fullName = "Syrphoctonus frontorius";
//        }
        List<Taxon> taxonList =  dao.getEntitiesByJPQL(QueryStringBuilder
                                                        .getInstance()
                                                        .buildGetTaxon(fullName));
        
        if(taxonList == null || taxonList.isEmpty()) {
            return null;
        } else if(taxonList.size() == 1) {
            return taxonList.get(0);
        } else {
            int count = (int)taxonList.stream() 
                                      .filter(isAccepted())
                                      .count(); 
            if(count >= 1) {
                return taxonList.stream().filter(isAccepted()).findFirst().get();
            } else {
                return taxonList.get(0);
            }
        }
    }
    
    private Predicate<Taxon> isAccepted() {    
        return t -> t.getIsAccepted();
    }
    
    public List<TaxonVO> validateTaxon(String json) { 
        
        List<TaxonVO> taxons = new ArrayList();
        try {
            JSONObject jsonObj = new JSONObject(json);
            String strTaxons = jsonObj.getString("taxonList");
            strTaxons = StringUtils.remove(strTaxons, "[\"");
            strTaxons = StringUtils.remove(strTaxons, "\"]");
            
            List<String> taxonList = Arrays.asList(StringUtils.split(strTaxons, "\",\""));  
            taxonList.stream()
                        .forEach(t -> { 
                            taxon = getTaxonFromDB(t);
                            if (taxon != null) {
                                taxons.add(new TaxonVO(taxon.getTaxonID(), t));
                            } else {
                                taxons.add(new TaxonVO(0, t));
                            }
                        }); 
        } catch (JSONException ex) {
            logger.error(ex.getMessage());
        }
//        return JsonBuilder.getInstance().buildTaxonJson(taxons);
        return taxons;
    }

    public String getSmtpAgentList() {
        logger.info("getSmtpAgentList");
        List<Object[]> results = dao.getSearchResultsByJPQL(
                QueryStringBuilder.getInstance().buildGetSmtpAgentList());
        return JsonBuilder.getInstance().buildAgentList(results);
    }
  
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
//            int preparedById = jsonObj.getInt("preparaedById");
//            String preparedByDate = jsonObj.getString("preparedDate");
            loanNumber = jsonObj.getString("loanNumber");
              
            Map<String, Taxon> taxonMap = new HashMap<>();
            Map<String, Taxon> preferedTaxonMap = new HashMap();
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
                             
                            determinedByName = coObject.getString("determiner"); 
                            firstName = coObject.getString("firstName");
                            lastName = coObject.getString("lastName");
                            if(determinerMap.containsKey(determinedByName)) {
                               determinedBy = determinerMap.get(determinedByName); 
                            } else {
                                determinedBy = getDeterminerFromDB(firstName, lastName);
                                determinerMap.put(determinedByName, determinedBy);
                            }
                            
                            determinedDate = coObject.getString("determinedDate"); 
                            int taxonId = coObject.getInt("taxonId");
                            fullName = coObject.getString("computedName");
                            if(taxonMap.containsKey(fullName)) {
                               taxon = taxonMap.get(fullName); 
                               preferdTaxon = preferedTaxonMap.get(fullName);
                            } else { 
                                taxon = (Taxon) dao.findByReference(taxonId, Taxon.class);
                                taxonMap.put(fullName, taxon);
                                if(taxon.getIsAccepted()) {
                                    preferdTaxon = taxon;
                                    preferedTaxonMap.put(fullName, taxon); 
                                } else {
                                    preferdTaxon = (Taxon) dao.findByReference(taxon.getAcceptedID().getTaxonID(), Taxon.class);
                                    preferedTaxonMap.put(fullName, preferdTaxon);
                                }
                            }
                            determinationRemarks = coObject.getString("determinationRemarks");
                             
                            media = coObject.getString("media");
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
                             
                            storage = coObject.getString("storage"); 
                            remark =  coObject.getString("remark");
                            
                            co = new Collectionobject();
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
                            co.setReservedText2(loanNumber);
                             
                            co.setGuid(Util.getInstance().generateGUID().toString());  
                            List<Collectionobjectattr> coAttrs = addNewCollectionObjectAttrs(co, total, numOfMales, numOfFemales);
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
     
    
    private List<Collectionobjectattr> addNewCollectionObjectAttrs(Collectionobject co, int total, int numOfMales, int numOfFemales) { 
        List<Collectionobjectattr> coAttrs = new ArrayList<>();
        
        int numOfUnknown = getNumOfUnknown(total, numOfMales, numOfFemales);
        if(numOfFemales != 0) { 
            Collectionobjectattr coAttrFemail = addCollectionObjectAtt("Female", numOfFemales);
            coAttrFemail.setCollectionObjectID(co); 
            coAttrs.add(coAttrFemail);  
        }
        
        if(numOfMales != 0) { 
            Collectionobjectattr coAttrMale = addCollectionObjectAtt("Male", numOfMales);
            coAttrMale.setCollectionObjectID(co);
            coAttrs.add(coAttrMale);
        }
        
        if(numOfUnknown != 0) { 
            Collectionobjectattr coAttr = addCollectionObjectAtt("Unknown", numOfUnknown);
            coAttr.setCollectionObjectID(co);
            coAttrs.add(coAttr);
        }   
        return coAttrs;
    }
    
    private Collectionobjectattr addCollectionObjectAtt(String sex, int numOfSpecimens) {
        
        Attributedef attrDef = createNewAttributedef();
        Collectionobjectattr coAttr = new Collectionobjectattr(); 
        coAttr.setCreatedByAgentID(createdBy);
        coAttr.setTimestampCreated(timestamp); 
        coAttr.setCollectionMemberID(Util.getInstance().getCollectionId()); 
        coAttr.setAttributeDefID(attrDef);  
        coAttr.setStrValue(sex);
        coAttr.setDoubleValue((double) numOfSpecimens);
    
        return coAttr;
    }
    
    private Attributedef createNewAttributedef() {
        Attributedef attributDef = new Attributedef();
        attributDef.setCreatedByAgentID(createdBy);
        attributDef.setDisciplineID(discipline);
        attributDef.setTimestampCreated(timestamp);   
        attributDef.setPrepTypeID(preptype);
        return attributDef;
    }
    
    private int buildNextCatalognumberByCollection() {
 
        String lastCatalogNumber = dao.getLastCatalogunumber(
                                        QueryStringBuilder.getInstance()
                                                .buildGetLastCatalogNumber(Util.getInstance().getCollectionId()));
         
        return lastCatalogNumber == null ? 0 : Integer.parseInt(lastCatalogNumber);
    }
    
    private Determination buildDetermination(String determinedDate, Collectionobject co) {
        determination = new Determination();
        determination.setCreatedByAgentID(createdBy);
        determination.setTimestampCreated(timestamp);
        determination.setCollectionMemberID(Util.getInstance().getCollectionId());
        determination.setDeterminedDate(Util.getInstance().convertStringToDate(determinedDate));
        determination.setIsCurrent(true);
        determination.setGuid(Util.getInstance().generateGUID().toString());
        determination.setTaxonID(taxon);
        determination.setPreferredTaxonID(preferdTaxon);
        determination.setDeterminerID(determinedBy);
        determination.setCollectionObjectID(co);
        if(determinationRemarks != null) {
            determination.setRemarks(determinationRemarks);
        }
        return determination;
    }
    
 
    
    private Preparation buildPreparation(int total, String storage, Collectionobject co) {
        preparation = new Preparation();
        preparation.setCreatedByAgentID(createdBy);
        preparation.setTimestampCreated(timestamp);
        preparation.setCollectionMemberID(Util.getInstance().getCollectionId());
        preparation.setCountAmt(total);
        preparation.setPreparedDate(timestamp);
        preparation.setPrepTypeID(preptype);
        preparation.setPreparedByID(createdBy);
        preparation.setPreparedDatePrecision((short) 1);
        preparation.setCollectionObjectID(co);
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


 
    
    private Agent getDeterminerFromDB(String firstName, String lastName) {
        
//        String firstName = agentName.split(" ")[0];
//        String lastName = getLastName(agentName); 
        
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
