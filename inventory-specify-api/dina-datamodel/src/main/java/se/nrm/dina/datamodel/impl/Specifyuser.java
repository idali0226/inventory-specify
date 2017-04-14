/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.datamodel.impl;
 
import se.nrm.dina.datamodel.BaseEntity;
import java.math.BigInteger;
import java.util.Date; 
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
@Table(name = "specifyuser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Specifyuser.findAll", query = "SELECT s FROM Specifyuser s"),
    @NamedQuery(name = "Specifyuser.findBySpecifyUserID", query = "SELECT s FROM Specifyuser s WHERE s.specifyUserID = :specifyUserID"), 
    @NamedQuery(name = "Specifyuser.findByAccumMinLoggedIn", query = "SELECT s FROM Specifyuser s WHERE s.accumMinLoggedIn = :accumMinLoggedIn"),
    @NamedQuery(name = "Specifyuser.findByEMail", query = "SELECT s FROM Specifyuser s WHERE s.eMail = :eMail"),
    @NamedQuery(name = "Specifyuser.findByIsLoggedIn", query = "SELECT s FROM Specifyuser s WHERE s.isLoggedIn = :isLoggedIn"),
    @NamedQuery(name = "Specifyuser.findByIsLoggedInReport", query = "SELECT s FROM Specifyuser s WHERE s.isLoggedInReport = :isLoggedInReport"),
    @NamedQuery(name = "Specifyuser.findByLoginCollectionName", query = "SELECT s FROM Specifyuser s WHERE s.loginCollectionName = :loginCollectionName"),
    @NamedQuery(name = "Specifyuser.findByLoginDisciplineName", query = "SELECT s FROM Specifyuser s WHERE s.loginDisciplineName = :loginDisciplineName"),
    @NamedQuery(name = "Specifyuser.findByLoginOutTime", query = "SELECT s FROM Specifyuser s WHERE s.loginOutTime = :loginOutTime"),
    @NamedQuery(name = "Specifyuser.findByName", query = "SELECT s FROM Specifyuser s WHERE s.name = :name"),
    @NamedQuery(name = "Specifyuser.findByPassword", query = "SELECT s FROM Specifyuser s WHERE s.password = :password"),
    @NamedQuery(name = "Specifyuser.findByUserType", query = "SELECT s FROM Specifyuser s WHERE s.userType = :userType"),
    @NamedQuery(name = "Specifyuser.updateUser",
            query = "UPDATE Specifyuser s SET s.isLoggedIn = :isLoggedIn, s.loginOutTime = :loginOutTime, s.timestampModified = :timestampModified WHERE s.name = :name")})
public class Specifyuser extends BaseEntity {
   
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SpecifyUserID")
    private Integer specifyUserID;
    
    
    @Column(name = "AccumMinLoggedIn")
    private BigInteger accumMinLoggedIn;
    
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 64)
    @Column(name = "EMail")
    private String eMail;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "IsLoggedIn")
    private boolean isLoggedIn;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "IsLoggedInReport")
    private boolean isLoggedInReport;
    
    @Size(max = 64)
    @Column(name = "LoginCollectionName")
    private String loginCollectionName;
    
    @Size(max = 64)
    @Column(name = "LoginDisciplineName")
    private String loginDisciplineName;
    
    @Column(name = "LoginOutTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date loginOutTime;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "Name")
    private String name;
    
    @Size(max = 255)
    @Column(name = "Password")
    private String password;
    
    @Size(max = 32)
    @Column(name = "UserType")
    private String userType;
    
    @JoinTable(name = "specifyuser_spprincipal", joinColumns = {
        @JoinColumn(name = "SpecifyUserID", referencedColumnName = "SpecifyUserID")}, inverseJoinColumns = {
        @JoinColumn(name = "SpPrincipalID", referencedColumnName = "SpPrincipalID")})
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Spprincipal> spprincipalList;
    
    @OneToMany(mappedBy = "ownerID", fetch = FetchType.LAZY)
    private Set<Sptasksemaphore> sptasksemaphoreList;
    
    @OneToMany(mappedBy = "visibilitySetByID", fetch = FetchType.LAZY)
    private Set<Locality> localityList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specifyUserID", fetch = FetchType.LAZY)
    private Set<Recordset> recordsetList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specifyUserID", fetch = FetchType.LAZY)
    private Set<Spappresource> spappresourceList;
    
    @OneToMany(mappedBy = "visibilitySetByID", fetch = FetchType.LAZY)
    private Set<Taxon> taxonList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specifyUserID", fetch = FetchType.LAZY)
    private Set<Spquery> spqueryList;
    
    @OneToMany(mappedBy = "specifyUserID", fetch = FetchType.LAZY)
    private Set<Agent> agentList;
    
    @OneToMany(mappedBy = "visibilitySetByID", fetch = FetchType.LAZY)
    private Set<Collectionobject> collectionobjectList;
    
    @OneToMany(mappedBy = "visibilitySetByID", fetch = FetchType.LAZY)
    private Set<Collectingevent> collectingeventList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specifyUserID", fetch = FetchType.LAZY)
    private Set<Workbench> workbenchList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specifyUserID", fetch = FetchType.LAZY)
    private Set<Workbenchtemplate> workbenchtemplateList;
    
    @OneToMany(mappedBy = "specifyUserID", fetch = FetchType.LAZY)
    private Set<Spappresourcedir> spappresourcedirList;
    
    @OneToMany(mappedBy = "visibilitySetByID")
    private Set<Attachment> attachmentList;
    
    @JoinColumn(name = "ModifiedByAgentID", referencedColumnName = "AgentID" )
    @ManyToOne
    private Agent modifiedByAgentID;
    
    @JoinColumn(name = "CreatedByAgentID", referencedColumnName = "AgentID" )
    @ManyToOne
    private Agent createdByAgentID;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specifyUserID", fetch = FetchType.LAZY)
    private Set<Spreport> spreportList;

    public Specifyuser() {
    }

    public Specifyuser(Integer specifyUserID) {
        this.specifyUserID = specifyUserID;
    }

    public Specifyuser(Integer specifyUserID, Date timestampCreated, boolean isLoggedIn, boolean isLoggedInReport, String name) {
        this.specifyUserID = specifyUserID;
        this.timestampCreated = timestampCreated;
        this.isLoggedIn = isLoggedIn;
        this.isLoggedInReport = isLoggedInReport;
        this.name = name;
    }
    
    @XmlID
    @XmlAttribute(name = "id")
    @Override
    public String getIdentityString() {
        return String.valueOf(specifyUserID);
    }
    
//    @XmlAttribute(name = "uuid") 
//    @Override
//    public String getUUID() {
//        return Util.getInstance().getURLLink(this.getClass().getSimpleName()) + specifyUserID;
//    }

    @Override
    public int getEntityId() {
        return specifyUserID;
    }
    
    public Integer getSpecifyUserID() {
        return specifyUserID;
    }

    public void setSpecifyUserID(Integer specifyUserID) {
        this.specifyUserID = specifyUserID;
    }
 

    public BigInteger getAccumMinLoggedIn() {
        return accumMinLoggedIn;
    }

    public void setAccumMinLoggedIn(BigInteger accumMinLoggedIn) {
        this.accumMinLoggedIn = accumMinLoggedIn;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public boolean getIsLoggedInReport() {
        return isLoggedInReport;
    }

    public void setIsLoggedInReport(boolean isLoggedInReport) {
        this.isLoggedInReport = isLoggedInReport;
    }

    public String getLoginCollectionName() {
        return loginCollectionName;
    }

    public void setLoginCollectionName(String loginCollectionName) {
        this.loginCollectionName = loginCollectionName;
    }

    public String getLoginDisciplineName() {
        return loginDisciplineName;
    }

    public void setLoginDisciplineName(String loginDisciplineName) {
        this.loginDisciplineName = loginDisciplineName;
    }

    public Date getLoginOutTime() {
        return loginOutTime;
    }

    public void setLoginOutTime(Date loginOutTime) {
        this.loginOutTime = loginOutTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @XmlTransient
    public Set<Spprincipal> getSpprincipalList() {
        return spprincipalList;
    }

    public void setSpprincipalList(Set<Spprincipal> spprincipalList) {
        this.spprincipalList = spprincipalList;
    }

    @XmlTransient
    public Set<Sptasksemaphore> getSptasksemaphoreList() {
        return sptasksemaphoreList;
    }

    public void setSptasksemaphoreList(Set<Sptasksemaphore> sptasksemaphoreList) {
        this.sptasksemaphoreList = sptasksemaphoreList;
    }

    @XmlTransient
    public Set<Locality> getLocalityList() {
        return localityList;
    }

    public void setLocalityList(Set<Locality> localityList) {
        this.localityList = localityList;
    }

    @XmlTransient
    public Set<Recordset> getRecordsetList() {
        return recordsetList;
    }

    public void setRecordsetList(Set<Recordset> recordsetList) {
        this.recordsetList = recordsetList;
    }

    @XmlTransient
    public Set<Spappresource> getSpappresourceList() {
        return spappresourceList;
    }

    public void setSpappresourceList(Set<Spappresource> spappresourceList) {
        this.spappresourceList = spappresourceList;
    }

    @XmlTransient
    public Set<Taxon> getTaxonList() {
        return taxonList;
    }

    public void setTaxonList(Set<Taxon> taxonList) {
        this.taxonList = taxonList;
    }

    @XmlTransient
    public Set<Spquery> getSpqueryList() {
        return spqueryList;
    }

    public void setSpqueryList(Set<Spquery> spqueryList) {
        this.spqueryList = spqueryList;
    }

    @XmlTransient
    public Set<Agent> getAgentList() {
        return agentList;
    }

    public void setAgentList(Set<Agent> agentList) {
        this.agentList = agentList;
    }

    @XmlTransient
    public Set<Collectionobject> getCollectionobjectList() {
        return collectionobjectList;
    }

    public void setCollectionobjectList(Set<Collectionobject> collectionobjectList) {
        this.collectionobjectList = collectionobjectList;
    }

    @XmlTransient
    public Set<Collectingevent> getCollectingeventList() {
        return collectingeventList;
    }

    public void setCollectingeventList(Set<Collectingevent> collectingeventList) {
        this.collectingeventList = collectingeventList;
    }

    @XmlTransient
    public Set<Workbench> getWorkbenchList() {
        return workbenchList;
    }

    public void setWorkbenchList(Set<Workbench> workbenchList) {
        this.workbenchList = workbenchList;
    }

    @XmlTransient
    public Set<Workbenchtemplate> getWorkbenchtemplateList() {
        return workbenchtemplateList;
    }

    public void setWorkbenchtemplateList(Set<Workbenchtemplate> workbenchtemplateList) {
        this.workbenchtemplateList = workbenchtemplateList;
    }

    @XmlTransient
    public Set<Spappresourcedir> getSpappresourcedirList() {
        return spappresourcedirList;
    }

    public void setSpappresourcedirList(Set<Spappresourcedir> spappresourcedirList) {
        this.spappresourcedirList = spappresourcedirList;
    }

    @XmlTransient
    public Set<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(Set<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
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

    @XmlTransient
    public Set<Spreport> getSpreportList() {
        return spreportList;
    }

    public void setSpreportList(Set<Spreport> spreportList) {
        this.spreportList = spreportList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (specifyUserID != null ? specifyUserID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Specifyuser)) {
            return false;
        }
        Specifyuser other = (Specifyuser) object;
        return !((this.specifyUserID == null && other.specifyUserID != null) || (this.specifyUserID != null && !this.specifyUserID.equals(other.specifyUserID)));
    }

    @Override
    public String toString() {
        return "se.nrm.dina.datamodel.Specifyuser[ specifyUserID=" + specifyUserID + " ]";
    }   
}
