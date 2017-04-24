/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.data.jpa.smtp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List; 
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext; 
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import se.nrm.dina.data.exceptions.DinaException; 
import se.nrm.dina.data.jpa.SMTPDao;  
import se.nrm.dina.datamodel.EntityBean;

/**
 *
 * @author idali
 * @param <T>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)  
public class SmtpDaoImpl<T extends EntityBean> implements SMTPDao<T>, Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @PersistenceContext(unitName = "jpaPU")                  //  persistence unit connect to production database  
    private EntityManager entityManager;

    public SmtpDaoImpl() {

    }

    @Override
    public T findByReference(int id, Class<T> clazz) {
        return entityManager.getReference(clazz, id);
    }
    
    @Override
    public T create(T entity) {
        logger.info("create(T) : {}", entity);

        T tmp = entity;
        try {
            entityManager.persist(entity);
            entityManager.flush();    
        } catch(ConstraintViolationException e) {
            logger.error("error: {}", e.getMessage());
//            throw new DinaConstraintViolationException(handleConstraintViolations(e), 400);  
        } catch (Exception e) {  
            throw new DinaException(e.getMessage(), 400);
        }
        return tmp;
    }

    @Override
    public void bacthCreate(List<T> entities) {
           
        entities.stream().forEach(e -> {
            entityManager.persist(e);
        }); 
        entityManager.flush();
        entityManager.clear();   
    }
    
     
    @Override
    public int getCountByJPQL(String jpql) {
        logger.info("getCountByJPQL : {}", jpql);
        Query query = entityManager.createQuery(jpql);
        
        T t;
        try {
            t = (T) query.getSingleResult();
            return 1;
        } catch (javax.persistence.NoResultException ex) {
            logger.error("No result");
            return 0;
        } catch (javax.persistence.NonUniqueResultException ex) {
            return 2;
        }
    }
    
    
    @Override
    public List<T> getEntitiesByJPQL(String jpql) {
        logger.info("getEntityByJpql : {}", jpql);
        Query query = entityManager.createQuery(jpql);
         
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException ex) {
            logger.error("No result");
            return null;
        } 
    }
    


    @Override
    public T getEntityByJPQL(String jpql) {
        logger.info("getEntityByJpql : {}", jpql);
        Query query = entityManager.createQuery(jpql);
        
        T t;
        try {
            t = (T) query.getSingleResult();
            return t;
        } catch (javax.persistence.NoResultException ex) {
            logger.error("No result");
            return null;
        } catch (javax.persistence.NonUniqueResultException ex) {
            return null;
        }
    }

    @Override
    public List<Object[]> getSearchResultsByJPQL(String jpql) {
//        logger.info("jpql : {}", jpql);
        TypedQuery<Object[]> TypedQuery = entityManager.createQuery(jpql, Object[].class);
        return TypedQuery.getResultList();
    }
 
 

    @Override
    public String getLastCatalogunumber(String jpql) {

//        logger.info("getLastRecord: {}", jpql);
        TypedQuery<String> query = entityManager.createQuery(jpql, String.class);
        query.setMaxResults(1);

        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException | javax.persistence.NonUniqueResultException ex) {
            logger.warn(ex.getMessage());
            return null;                        // if no result, return null
        } 
    }
    
    
    @Override
    public T getEntityByNamedQuery(String namedQuery, Map<String, Object> map) {
        
        logger.info("getEntityByNamedQuery : {} -- {}", namedQuery, map);
        
        Query query = entityManager.createNamedQuery(namedQuery); 
        setQueryParameter(map, query);
         
        try {  
            List<T> list = query.getResultList();
            logger.info("list : {}", list);
            
            if(list != null && !list.isEmpty()) {
                return list.get(0);
            } 
//            return (T) query.getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            logger.error("No result");
            return null; 
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        return null;  
    }
 
    
    
 
    @Override
    public void deleteOldSMTPData() {
        logger.info("deleteByJPQL"); 
   
        List<String> list = new ArrayList();
        
        list.add("DELETE FROM Collectionobjectattr attr WHERE attr.collectionMemberID = 655361");                   // c-file
        list.add("DELETE FROM Collectionobjectattr attr WHERE attr.collectionMemberID = 688128");                   // d-file
        list.add("DELETE FROM Attributedef def WHERE def.disciplineID = 655360");
        list.add("DELETE FROM Preparation p WHERE p.collectionMemberID = 688128");                                  // d-file only.  c-file don't have preparation
        list.add("DELETE FROM Determination d WHERE d.collectionMemberID = 655361");                                   // c-file
        list.add("DELETE FROM Determination d WHERE d.collectionMemberID = 688128");                                   // d-file
        list.add("DELETE FROM Collectionobject co WHERE co.collectionMemberID = 655361");
        list.add("DELETE FROM Collectionobject co WHERE co.collectionMemberID = 688128");
//        list.add("DELETE FROM Taxon t WHERE t.taxonTreeDefID=11 AND t.rankID > 10");              // keep the tree root
        list.add("DELETE FROM Preptype p WHERE p.collectionID.userGroupScopeId = 655361");                                // dont need to
        list.add("DELETE FROM Preptype p WHERE p.collectionID.userGroupScopeId = 688128");                                // dont need to
        
        list.stream()
                .map((strQry) -> 
                        entityManager.createQuery(strQry)).forEach((query) -> {
            query.executeUpdate();
        }); 
    }  
    
    private void setQueryParameter(Map<String, Object> map, Query query) {
        map.entrySet()
                    .stream()
                    .forEach(entry -> {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        query.setParameter(key, value);
                    });
    }
}
