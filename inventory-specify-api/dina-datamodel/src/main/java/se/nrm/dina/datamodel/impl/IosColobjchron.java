/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.datamodel.impl;
 
import se.nrm.dina.datamodel.EntityBean;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement; 

/**
 *
 * @author idali
 */
@Entity
@Table(name = "ios_colobjchron")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IosColobjchron.findAll", query = "SELECT i FROM IosColobjchron i"),
    @NamedQuery(name = "IosColobjchron.findByOldID", query = "SELECT i FROM IosColobjchron i WHERE i.oldID = :oldID"),
    @NamedQuery(name = "IosColobjchron.findByNewID", query = "SELECT i FROM IosColobjchron i WHERE i.newID = :newID")})
public class IosColobjchron implements EntityBean, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "OldID")
    private Integer oldID;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "NewID")
    private int newID;

    public IosColobjchron() {
    }
    
    @XmlID
    @XmlAttribute(name = "id")
    @Override
    public String getIdentityString() {
        return String.valueOf(oldID);
    }
    
    
//    @XmlAttribute(name = "uuid") 
//    @Override
//    public String getUUID() {
//        return Util.getInstance().getURLLink(this.getClass().getSimpleName()) + oldID;
//    }
    
    @Override
    public int getEntityId() {
        return oldID;
    }

    public IosColobjchron(Integer oldID) {
        this.oldID = oldID;
    }

    public IosColobjchron(Integer oldID, int newID) {
        this.oldID = oldID;
        this.newID = newID;
    }

    public Integer getOldID() {
        return oldID;
    }

    public void setOldID(Integer oldID) {
        this.oldID = oldID;
    }

    public int getNewID() {
        return newID;
    }

    public void setNewID(int newID) {
        this.newID = newID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (oldID != null ? oldID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IosColobjchron)) {
            return false;
        }
        IosColobjchron other = (IosColobjchron) object;
        return !((this.oldID == null && other.oldID != null) || (this.oldID != null && !this.oldID.equals(other.oldID)));
    }

    @Override
    public String toString() {
        return "se.nrm.dina.datamodel.IosColobjchron[ oldID=" + oldID + " ]";
    }
    
}
