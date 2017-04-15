/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.data.jpa;

import java.util.List;
import java.util.Map;
import se.nrm.dina.datamodel.EntityBean;

/**
 *
 * @author idali
 * @param <T>
 */
public interface SMTPDao<T extends EntityBean> {
    
    
    public T findByReference(int id, Class<T> clazz);
    
    public T create(T entity);
    
    public void bacthCreate(List<T> entities);
    
    
    public T getEntityByJPQL(String jpql);
    
    public List<Object[]> getSearchResultsByJPQL(String jpql);

    /**
     *
     * @param namedQuery
     * @param map
     * @return
     */
    public T getEntityByNamedQuery(String namedQuery, Map<String, Object> map);


   
    public String getLastCatalogunumber(String jpql);
    
    
    
    
    public void deleteOldSMTPData();
}
