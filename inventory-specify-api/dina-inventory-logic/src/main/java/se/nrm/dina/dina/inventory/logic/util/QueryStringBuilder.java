/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dina.inventory.logic.util;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author idali
 */
public class QueryStringBuilder {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
     
    private StringBuilder sb;
    private static QueryStringBuilder instance = null;

    public static synchronized QueryStringBuilder getInstance() {
        if (instance == null) {
            instance = new QueryStringBuilder();
        }
        return instance;
    }
    
    
    
    public String buildGetCollectingEventByEventIdAndDisciplineId(int eventId, int disciplineId) {
        
        sb = new StringBuilder();  
        
        sb.append("SELECT ce "); 
        sb.append("FROM Collectingevent ce "); 
        sb.append("WHERE ce.stationFieldNumber = 'Event ID "); 
        sb.append(eventId);
        sb.append("' AND ce.disciplineID.userGroupScopeId= ");
        sb.append(disciplineId);    
        return sb.toString();
    }
    
     
    
    public String buildGetSmtpAgentList() { 
        return "SELECT a.agentID, a.firstName, a.lastName, a.remarks FROM Agent AS a WHERE a.agentType = 1 AND a.remarks like '%smtp%'"; 
    } 
    
    public String buildGetTaxonTreeDefItem(int taxonTreeDefId, int rankId) {
        
        sb = new StringBuilder();
        sb.append("SELECT tdi FROM Taxontreedefitem AS tdi ");
        sb.append("WHERE tdi.taxonTreeDefID.taxonTreeDefID = ");
        sb.append(taxonTreeDefId);
        sb.append(" AND tdi.rankID = ");
        sb.append(rankId);
         
        return sb.toString();
    }
    
    public String buildGetTaxonParent(String fullName, int rankId, int treeDefId) {
        
        sb = new StringBuilder();
        sb.append("SELECT t FROM Taxon AS t ");
        sb.append("WHERE t.taxonTreeDefID.taxonTreeDefID = ");
        sb.append(treeDefId);
        sb.append(" AND t.fullName = '");
        sb.append(fullName);
        sb.append("' AND t.rankID = ");
        sb.append(rankId); 
        sb.append(" AND t.isAccepted = true");
         
        return sb.toString();
    }
    
    public String buildGetTaxon(String fullName, int treeDefId) {
        
        logger.info("buildGetTaxon");
        
        sb = new StringBuilder();
        sb.append("SELECT t FROM Taxon AS t ");
        sb.append("WHERE t.taxonTreeDefID.taxonTreeDefID = ");
        sb.append(treeDefId);
        sb.append(" AND t.fullName = '");
        sb.append(fullName); 
        sb.append("' AND t.isAccepted = true");
          
        return sb.toString();
    }
    
    public String buildGetTaxon(String fullName) {
        
        logger.info("buildGetTaxon");
        
        sb = new StringBuilder();
        sb.append("SELECT t FROM Taxon AS t ");
        sb.append("WHERE t.taxonTreeDefID.taxonTreeDefID = 11"); 
        sb.append(" AND t.fullName = '");
        sb.append(fullName);
        sb.append("'");
       
        return sb.toString();
    }
    
 
    
    
//    private String buildGetTaxonWithGuid(String fullName, String guid) {
//        
//        logger.info("buildGetTaxon");
//        
//        sb = new StringBuilder();
//        sb.append("SELECT t FROM Taxon AS t ");
//        sb.append("WHERE t.taxonTreeDefID.taxonTreeDefID = 11"); 
//        sb.append(" AND t.fullName = '");
//        sb.append(fullName);
//        sb.append("' AND t.source = '");
//        sb.append(guid);
//        sb.append("' AND t.guid is not null");
//       
//        return sb.toString();
//    }

    public String buildGetPrepType(String media, int collectionId) {
        sb = new StringBuilder();
        sb.append("SELECT p ");
        sb.append("FROM Preptype p ");
        sb.append("WHERE p.name = '");
        sb.append(media);
        sb.append("' AND p.collectionID.userGroupScopeId = ");
        sb.append(collectionId);
        return sb.toString();
    }

    public String buildGetLastCatalogNumber(int collectionId) {
        sb = new StringBuilder();
        sb.append("SELECT c.catalogNumber FROM Collectionobject AS c where c.collectionID.userGroupScopeId = ");
        sb.append(collectionId);
        sb.append(" ORDER BY c.collectionObjectID desc");
        return sb.toString();
    }
    
    public String buildTaxonTree(String parent) {
        sb = new StringBuilder();
        sb.append("SELECT new se.nrm.dina.data.jpa.vo.TaxonVO(t.taxonID, t.fullName) ");
        sb.append("From taxon AS t WHERE t.parent.fullName = '"); 
        sb.append(parent); 
        sb.append("TreeDefId = ");
        sb.append(Util.getInstance().getTaxonTreeDefId());
        sb.append(" AND t.isAccepted = true");
        sb.append(" ORDER BY t.fullName");
        
        return sb.toString();
    }
            
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//    public String buildTaxonCountQuery(String taxon, int taxonTreeDefId) {
//        sb = new StringBuilder();
//        sb.append("SELECT COUNT(t) ");
//        sb.append("FROM Taxon t ");  
//        sb.append("WHERE t.fullName = '");
//        sb.append(taxon); 
//        sb.append("' AND t.taxonTreeDefID.taxonTreeDefID = ");  
//        sb.append(taxonTreeDefId); 
//        return sb.toString();
//    }
//    
//    public String buildGetSmtpAgentByNameQuery(String firstname, String lastname) {
//        sb = new StringBuilder();
//        sb.append("SELECT a ");
//        sb.append("FROM Agent AS a ");
//        sb.append("WHERE a.firstName = '");
//        sb.append(firstname);
//        sb.append("' AND a.lastName = '");
//        sb.append(lastname);
//        sb.append("' AND a.agentType = ");
//        sb.append(1);  
//        return sb.toString();
//    }
//    
//        
//    public String buildNumOfDeterminationsQuery(String taxon, int collectingeventId, int collectionId) {
//        sb = new StringBuilder();
//        sb.append("SELECT COUNT(d) ");
//        sb.append("FROM Determination d ");
//        sb.append("WHERE d.taxonID.fullName = '");
//        sb.append(taxon);
//        sb.append("' AND d.collectionObjectID.collectingEventID.collectingEventID = ");
//        sb.append(collectingeventId);
//        sb.append(" AND d.collectionMemberID = ");
//        sb.append(collectionId);
//        sb.append(" AND d.isCurrent = ");
//        sb.append(true); 
//        return sb.toString();
//    }
//    
//    public String buildCollectingEventCountByLocalityId(int localityId) {
//        sb = new StringBuilder();   
//        sb.append("SELECT COUNT(ce) ");
//        sb.append("FROM Collectingevent ce ");
//        sb.append("WHERE ce.localityID.localityID ="); 
//        sb.append(localityId);
//        sb.append(" AND ce.stationFieldNumber like 'Event ID %'");
//        return sb.toString();
//    }
//    
//    public String buildGetCollectingEventsByLocalityId(int localityId) {
//        sb = new StringBuilder();   
//        sb.append("SELECT ce.stationFieldNumber, ce.startDate, ce.endDate ");
//        sb.append("FROM Collectingevent ce ");
//        sb.append("WHERE ce.localityID.localityID = "); 
//        sb.append(localityId);    
//        sb.append(" AND ce.stationFieldNumber like 'Event ID %'");
//        return sb.toString();    
//    }
//    
//    
//    
//    
//    public String buildTaxonCountByLocalityId(int localityId) {
//        sb = new StringBuilder();
//        sb.append("SELECT t.fullName, COUNT(t) ");
//        sb.append("FROM Determination d ");  
//        sb.append("JOIN d.taxonID t ");   
//        sb.append("WHERE d.taxonID = t ");  
//        sb.append("AND d.collectionObjectID.collectingEventID.localityID.localityID = "); 
//        sb.append(localityId);   
//        sb.append(" AND d.isCurrent = true "); 
//        sb.append("GROUP BY t.fullName ORDER BY t.fullName ASC");
//        
//        return sb.toString();
//    }
//    
//    
// 
//    
//    public String buildTaxonCountByCollectingeventId(int eventId) {
//        sb = new StringBuilder();   
//        sb.append("SELECT t.fullName, COUNT(t) ");
//        sb.append("FROM Determination d ");  
//        sb.append("JOIN d.taxonID t ");   
//        sb.append("WHERE d.taxonID = t ");
//        sb.append("AND d.collectionObjectID.collectingEventID.collectingEventID = "); 
//        sb.append(eventId);   
//        sb.append(" AND d.isCurrent = true "); 
//        sb.append("GROUP BY t.fullName ORDER BY t.fullName ASC");
//        return sb.toString();
//    }
// 
//    
//    public String buildGetLocalityByTrapIdAndDisciplineId(int trapId, int disciplineId) {
//        sb = new StringBuilder();   
//        sb.append("SELECT lc.localityID, lc.localityName, lc.lat1Text, lc.long1Text, lc.shortName ");
//        sb.append("FROM Locality lc "); 
//        sb.append("WHERE lc.shortName = 'Trap ID ");  
//        sb.append(trapId);
//        sb.append("' ");
//        sb.append("AND lc.disciplineID.userGroupScopeId = ");
//        sb.append(disciplineId);  
//        return sb.toString(); 
//    }
//    
//
//    
//    public String buildGetTaxonListQuery(String searchValue, int treeDefId) {
//        sb = new StringBuilder();
//        sb.append("SELECT t.fullName ");
//        sb.append("FROM Taxon AS t where t.fullName like '");
//        sb.append(searchValue);
//        sb.append("%' AND t.taxonTreeDefID.taxonTreeDefID = "); 
//        sb.append(treeDefId);
//         
//        return sb.toString();
//    }
}
