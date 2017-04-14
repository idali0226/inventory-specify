/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.datamodel.impl;
 
import se.nrm.dina.datamodel.BaseEntity;
import java.util.Date; 
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient; 

/**
 *
 * @author idali
 */
@Entity
@Table(name = "inforequest")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inforequest.findAll", query = "SELECT i FROM Inforequest i"),
    @NamedQuery(name = "Inforequest.findByInfoRequestID", query = "SELECT i FROM Inforequest i WHERE i.infoRequestID = :infoRequestID"), 
    @NamedQuery(name = "Inforequest.findByCollectionMemberID", query = "SELECT i FROM Inforequest i WHERE i.collectionMemberID = :collectionMemberID"),
    @NamedQuery(name = "Inforequest.findByEmail", query = "SELECT i FROM Inforequest i WHERE i.email = :email"),
    @NamedQuery(name = "Inforequest.findByFirstname", query = "SELECT i FROM Inforequest i WHERE i.firstname = :firstname"),
    @NamedQuery(name = "Inforequest.findByInfoReqNumber", query = "SELECT i FROM Inforequest i WHERE i.infoReqNumber = :infoReqNumber"),
    @NamedQuery(name = "Inforequest.findByInstitution", query = "SELECT i FROM Inforequest i WHERE i.institution = :institution"),
    @NamedQuery(name = "Inforequest.findByLastname", query = "SELECT i FROM Inforequest i WHERE i.lastname = :lastname"),
    @NamedQuery(name = "Inforequest.findByReplyDate", query = "SELECT i FROM Inforequest i WHERE i.replyDate = :replyDate"),
    @NamedQuery(name = "Inforequest.findByRequestDate", query = "SELECT i FROM Inforequest i WHERE i.requestDate = :requestDate")})
public class Inforequest extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "InfoRequestID")
    private Integer infoRequestID;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "CollectionMemberID")
    private int collectionMemberID;
    
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "Email")
    private String email;
    
    @Size(max = 50)
    @Column(name = "Firstname")
    private String firstname;
    
    @Size(max = 32)
    @Column(name = "InfoReqNumber")
    private String infoReqNumber;
    
    @Size(max = 127)
    @Column(name = "Institution")
    private String institution;
    
    @Size(max = 50)
    @Column(name = "Lastname")
    private String lastname;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "Remarks")
    private String remarks;
    
    @Column(name = "ReplyDate")
    @Temporal(TemporalType.DATE)
    private Date replyDate;
    
    @Column(name = "RequestDate")
    @Temporal(TemporalType.DATE)
    private Date requestDate;
    
    @OneToMany(mappedBy = "infoRequestID", fetch = FetchType.LAZY)
    private Set<Recordset> recordsetList;
    
    @JoinColumn(name = "AgentID", referencedColumnName = "AgentID")
    @ManyToOne
    private Agent agentID;
    
    @JoinColumn(name = "ModifiedByAgentID", referencedColumnName = "AgentID")
    @ManyToOne
    private Agent modifiedByAgentID;
    
    @JoinColumn(name = "CreatedByAgentID", referencedColumnName = "AgentID")
    @ManyToOne
    private Agent createdByAgentID;

    public Inforequest() {
    }

    public Inforequest(Integer infoRequestID) {
        this.infoRequestID = infoRequestID;
    }

    @XmlID
    @XmlAttribute(name = "id")
    @Override
    public String getIdentityString() {
        return String.valueOf(infoRequestID);
    }
    
//    @XmlAttribute(name = "uuid") 
//    @Override
//    public String getUUID() {
//        return Util.getInstance().getURLLink(this.getClass().getSimpleName()) + infoRequestID;
//    }
    
    @Override
    public int getEntityId() {
        return infoRequestID;
    }
    
    public Inforequest(Integer infoRequestID, Date timestampCreated, int collectionMemberID) {
        this.infoRequestID = infoRequestID;
        this.timestampCreated = timestampCreated;
        this.collectionMemberID = collectionMemberID;
    }

    public Integer getInfoRequestID() {
        return infoRequestID;
    }

    public void setInfoRequestID(Integer infoRequestID) {
        this.infoRequestID = infoRequestID;
    }

   

    public int getCollectionMemberID() {
        return collectionMemberID;
    }

    public void setCollectionMemberID(int collectionMemberID) {
        this.collectionMemberID = collectionMemberID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getInfoReqNumber() {
        return infoReqNumber;
    }

    public void setInfoReqNumber(String infoReqNumber) {
        this.infoReqNumber = infoReqNumber;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    @XmlTransient
    public Set<Recordset> getRecordsetList() {
        return recordsetList;
    }

    public void setRecordsetList(Set<Recordset> recordsetList) {
        this.recordsetList = recordsetList;
    }

    @XmlIDREF
    public Agent getAgentID() {
        return agentID;
    }

    public void setAgentID(Agent agentID) {
        this.agentID = agentID;
    }

    @XmlIDREF
    public Agent getModifiedByAgentID() {
        return modifiedByAgentID;
    }

    public void setModifiedByAgentID(Agent modifiedByAgentID) {
        this.modifiedByAgentID = modifiedByAgentID;
    }

    @XmlIDREF
    public Agent getCreatedByAgentID() {
        return createdByAgentID;
    }

    public void setCreatedByAgentID(Agent createdByAgentID) {
        this.createdByAgentID = createdByAgentID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (infoRequestID != null ? infoRequestID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inforequest)) {
            return false;
        }
        Inforequest other = (Inforequest) object;
        return !((this.infoRequestID == null && other.infoRequestID != null) || (this.infoRequestID != null && !this.infoRequestID.equals(other.infoRequestID)));
    }

    @Override
    public String toString() {
        return "se.nrm.dina.datamodel.Inforequest[ infoRequestID=" + infoRequestID + " ]";
    }  
}
