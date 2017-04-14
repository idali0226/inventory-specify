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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;   
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
@Table(name = "discipline")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Discipline.findAll", query = "SELECT d FROM Discipline d"),
    @NamedQuery(name = "Discipline.findByUserGroupScopeId", query = "SELECT d FROM Discipline d WHERE d.userGroupScopeId = :userGroupScopeId"), 
    @NamedQuery(name = "Discipline.findByDisciplineId", query = "SELECT d FROM Discipline d WHERE d.disciplineId = :disciplineId"),
    @NamedQuery(name = "Discipline.findByName", query = "SELECT d FROM Discipline d WHERE d.name = :name"),
    @NamedQuery(name = "Discipline.findByRegNumber", query = "SELECT d FROM Discipline d WHERE d.regNumber = :regNumber"),
    @NamedQuery(name = "Discipline.findByType", query = "SELECT d FROM Discipline d WHERE d.type = :type"),
    @NamedQuery(name = "Discipline.findByIsPaleoContextEmbedded", query = "SELECT d FROM Discipline d WHERE d.isPaleoContextEmbedded = :isPaleoContextEmbedded"),
    @NamedQuery(name = "Discipline.findByPaleoContextChildTable", query = "SELECT d FROM Discipline d WHERE d.paleoContextChildTable = :paleoContextChildTable")})
public class Discipline extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "UserGroupScopeId")
    private Integer userGroupScopeId;
    
    
    @Column(name = "disciplineId")
    private Integer disciplineId;
    
    @Size(max = 64)
    @Column(name = "Name")
    private String name;
    
    @Size(max = 24)
    @Column(name = "RegNumber")
    private String regNumber;
    
    @Size(max = 64)
    @Column(name = "Type")
    private String type;
    
    @Column(name = "IsPaleoContextEmbedded")
    private Boolean isPaleoContextEmbedded;
    
    @Size(max = 50)
    @Column(name = "PaleoContextChildTable")
    private String paleoContextChildTable;
    
    @JoinTable(name = "autonumsch_dsp", joinColumns = {
        @JoinColumn(name = "DisciplineID", referencedColumnName = "UserGroupScopeId")}, inverseJoinColumns = {
        @JoinColumn(name = "AutoNumberingSchemeID", referencedColumnName = "AutoNumberingSchemeID")})
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Autonumberingscheme> autonumberingschemeList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Gift> giftList;
    
    @JoinColumn(name = "LithoStratTreeDefID", referencedColumnName = "LithoStratTreeDefID")
    @ManyToOne
    private Lithostrattreedef lithoStratTreeDefID;
    
    @JoinColumn(name = "DivisionID", referencedColumnName = "UserGroupScopeId")
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Division divisionID;
    
    @JoinColumn(name = "GeologicTimePeriodTreeDefID", referencedColumnName = "GeologicTimePeriodTreeDefID")
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Geologictimeperiodtreedef geologicTimePeriodTreeDefID;
    
    @JoinColumn(name = "GeographyTreeDefID", referencedColumnName = "GeographyTreeDefID")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Geographytreedef geographyTreeDefID;
    
    @JoinColumn(name = "DataTypeID", referencedColumnName = "DataTypeID")
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Datatype dataTypeID;
    
    @JoinColumn(name = "TaxonTreeDefID", referencedColumnName = "TaxonTreeDefID")
    @ManyToOne
    private Taxontreedef taxonTreeDefID;
    
    @JoinColumn(name = "ModifiedByAgentID", referencedColumnName = "AgentID")
    @ManyToOne
    private Agent modifiedByAgentID;
    
    @JoinColumn(name = "CreatedByAgentID", referencedColumnName = "AgentID")
    @ManyToOne
    private Agent createdByAgentID;
    
    @OneToMany(mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Sptasksemaphore> sptasksemaphoreList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Collectingtrip> collectingtripList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Locality> localityList;
    
    @OneToMany(mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Localitycitation> localitycitationList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Fieldnotebookpageset> fieldnotebookpagesetList;
    
    @OneToMany(mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Paleocontext> paleocontextList;
    
    @OneToMany(mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Loanreturnpreparation> loanreturnpreparationList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Localitynamealias> localitynamealiasList;
    
    @OneToMany(mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Collectingeventattribute> collectingeventattributeList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Attributedef> attributedefList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Collectingevent> collectingeventList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Fieldnotebook> fieldnotebookList;
    
    @OneToMany(mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Shipment> shipmentList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Splocalecontainer> splocalecontainerList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Collection> collectionList;
    
    @OneToMany(mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Spappresourcedir> spappresourcedirList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Spexportschema> spexportschemaList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Loan> loanList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Fieldnotebookpage> fieldnotebookpageList;
    
    @OneToMany(mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Giftagent> giftagentList;
    
    @OneToMany(mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Loanpreparation> loanpreparationList;
    
    @OneToMany(mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Giftpreparation> giftpreparationList;
    
    @OneToMany(mappedBy = "disciplineID", fetch = FetchType.LAZY)
    private Set<Loanagent> loanagentList;

    public Discipline() {
    }

    public Discipline(Integer userGroupScopeId) {
        this.userGroupScopeId = userGroupScopeId;
    }

    public Discipline(Integer userGroupScopeId, Date timestampCreated) {
        this.userGroupScopeId = userGroupScopeId;
        this.timestampCreated = timestampCreated;
    }
    
    @XmlID
    @XmlAttribute(name = "id")
    @Override
    public String getIdentityString() {
        return String.valueOf(userGroupScopeId);
    }
    
//    @XmlAttribute(name = "uuid") 
//    @Override
//    public String getUUID() {
//        return Util.getInstance().getURLLink(this.getClass().getSimpleName()) + userGroupScopeId;
//    }

    @Override
    public int getEntityId() {
        return userGroupScopeId;
    }
    
    public Integer getUserGroupScopeId() {
        return userGroupScopeId;
    }

    public void setUserGroupScopeId(Integer userGroupScopeId) {
        this.userGroupScopeId = userGroupScopeId;
    }
 
    public Integer getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(Integer disciplineId) {
        this.disciplineId = disciplineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsPaleoContextEmbedded() {
        return isPaleoContextEmbedded;
    }

    public void setIsPaleoContextEmbedded(Boolean isPaleoContextEmbedded) {
        this.isPaleoContextEmbedded = isPaleoContextEmbedded;
    }

    public String getPaleoContextChildTable() {
        return paleoContextChildTable;
    }

    public void setPaleoContextChildTable(String paleoContextChildTable) {
        this.paleoContextChildTable = paleoContextChildTable;
    }

    @XmlTransient
    public Set<Autonumberingscheme> getAutonumberingschemeList() {
        return autonumberingschemeList;
    }

    public void setAutonumberingschemeList(Set<Autonumberingscheme> autonumberingschemeList) {
        this.autonumberingschemeList = autonumberingschemeList;
    }

    @XmlTransient
    public Set<Gift> getGiftList() {
        return giftList;
    }

    public void setGiftList(Set<Gift> giftList) {
        this.giftList = giftList;
    }

    @XmlIDREF
    public Lithostrattreedef getLithoStratTreeDefID() {
        return lithoStratTreeDefID;
    }

    public void setLithoStratTreeDefID(Lithostrattreedef lithoStratTreeDefID) {
        this.lithoStratTreeDefID = lithoStratTreeDefID;
    }

    @XmlIDREF
    public Division getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(Division divisionID) {
        this.divisionID = divisionID;
    }

    @XmlIDREF
    public Geologictimeperiodtreedef getGeologicTimePeriodTreeDefID() {
        return geologicTimePeriodTreeDefID;
    }

    public void setGeologicTimePeriodTreeDefID(Geologictimeperiodtreedef geologicTimePeriodTreeDefID) {
        this.geologicTimePeriodTreeDefID = geologicTimePeriodTreeDefID;
    }

    @XmlIDREF
    public Geographytreedef getGeographyTreeDefID() {
        return geographyTreeDefID;
    }

    public void setGeographyTreeDefID(Geographytreedef geographyTreeDefID) {
        this.geographyTreeDefID = geographyTreeDefID;
    }

    @XmlIDREF
    public Datatype getDataTypeID() {
        return dataTypeID;
    }

    public void setDataTypeID(Datatype dataTypeID) {
        this.dataTypeID = dataTypeID;
    }

    @XmlIDREF
    public Taxontreedef getTaxonTreeDefID() {
        return taxonTreeDefID;
    }

    public void setTaxonTreeDefID(Taxontreedef taxonTreeDefID) {
        this.taxonTreeDefID = taxonTreeDefID;
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
    public Set<Sptasksemaphore> getSptasksemaphoreList() {
        return sptasksemaphoreList;
    }

    public void setSptasksemaphoreList(Set<Sptasksemaphore> sptasksemaphoreList) {
        this.sptasksemaphoreList = sptasksemaphoreList;
    }

    @XmlTransient
    public Set<Collectingtrip> getCollectingtripList() {
        return collectingtripList;
    }

    public void setCollectingtripList(Set<Collectingtrip> collectingtripList) {
        this.collectingtripList = collectingtripList;
    }

    @XmlTransient
    public Set<Locality> getLocalityList() {
        return localityList;
    }

    public void setLocalityList(Set<Locality> localityList) {
        this.localityList = localityList;
    }

    @XmlTransient
    public Set<Localitycitation> getLocalitycitationList() {
        return localitycitationList;
    }

    public void setLocalitycitationList(Set<Localitycitation> localitycitationList) {
        this.localitycitationList = localitycitationList;
    }

    @XmlTransient
    public Set<Fieldnotebookpageset> getFieldnotebookpagesetList() {
        return fieldnotebookpagesetList;
    }

    public void setFieldnotebookpagesetList(Set<Fieldnotebookpageset> fieldnotebookpagesetList) {
        this.fieldnotebookpagesetList = fieldnotebookpagesetList;
    }

    @XmlTransient
    public Set<Paleocontext> getPaleocontextList() {
        return paleocontextList;
    }

    public void setPaleocontextList(Set<Paleocontext> paleocontextList) {
        this.paleocontextList = paleocontextList;
    }

    @XmlTransient
    public Set<Loanreturnpreparation> getLoanreturnpreparationList() {
        return loanreturnpreparationList;
    }

    public void setLoanreturnpreparationList(Set<Loanreturnpreparation> loanreturnpreparationList) {
        this.loanreturnpreparationList = loanreturnpreparationList;
    }

    @XmlTransient
    public Set<Localitynamealias> getLocalitynamealiasList() {
        return localitynamealiasList;
    }

    public void setLocalitynamealiasList(Set<Localitynamealias> localitynamealiasList) {
        this.localitynamealiasList = localitynamealiasList;
    }

    @XmlTransient
    public Set<Collectingeventattribute> getCollectingeventattributeList() {
        return collectingeventattributeList;
    }

    public void setCollectingeventattributeList(Set<Collectingeventattribute> collectingeventattributeList) {
        this.collectingeventattributeList = collectingeventattributeList;
    }

    @XmlTransient
    public Set<Attributedef> getAttributedefList() {
        return attributedefList;
    }

    public void setAttributedefList(Set<Attributedef> attributedefList) {
        this.attributedefList = attributedefList;
    }

    @XmlTransient
    public Set<Collectingevent> getCollectingeventList() {
        return collectingeventList;
    }

    public void setCollectingeventList(Set<Collectingevent> collectingeventList) {
        this.collectingeventList = collectingeventList;
    }

    @XmlTransient
    public Set<Fieldnotebook> getFieldnotebookList() {
        return fieldnotebookList;
    }

    public void setFieldnotebookList(Set<Fieldnotebook> fieldnotebookList) {
        this.fieldnotebookList = fieldnotebookList;
    }

    @XmlTransient
    public Set<Shipment> getShipmentList() {
        return shipmentList;
    }

    public void setShipmentList(Set<Shipment> shipmentList) {
        this.shipmentList = shipmentList;
    }

    @XmlTransient
    public Set<Splocalecontainer> getSplocalecontainerList() {
        return splocalecontainerList;
    }

    public void setSplocalecontainerList(Set<Splocalecontainer> splocalecontainerList) {
        this.splocalecontainerList = splocalecontainerList;
    }

    @XmlTransient
    public Set<Collection> getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(Set<Collection> collectionList) {
        this.collectionList = collectionList;
    }

    @XmlTransient
    public Set<Spappresourcedir> getSpappresourcedirList() {
        return spappresourcedirList;
    }

    public void setSpappresourcedirList(Set<Spappresourcedir> spappresourcedirList) {
        this.spappresourcedirList = spappresourcedirList;
    }

    @XmlTransient
    public Set<Spexportschema> getSpexportschemaList() {
        return spexportschemaList;
    }

    public void setSpexportschemaList(Set<Spexportschema> spexportschemaList) {
        this.spexportschemaList = spexportschemaList;
    }

    @XmlTransient
    public Set<Loan> getLoanList() {
        return loanList;
    }

    public void setLoanList(Set<Loan> loanList) {
        this.loanList = loanList;
    }

    @XmlTransient
    public Set<Fieldnotebookpage> getFieldnotebookpageList() {
        return fieldnotebookpageList;
    }

    public void setFieldnotebookpageList(Set<Fieldnotebookpage> fieldnotebookpageList) {
        this.fieldnotebookpageList = fieldnotebookpageList;
    }

    @XmlTransient
    public Set<Giftagent> getGiftagentList() {
        return giftagentList;
    }

    public void setGiftagentList(Set<Giftagent> giftagentList) {
        this.giftagentList = giftagentList;
    }

    @XmlTransient
    public Set<Loanpreparation> getLoanpreparationList() {
        return loanpreparationList;
    }

    public void setLoanpreparationList(Set<Loanpreparation> loanpreparationList) {
        this.loanpreparationList = loanpreparationList;
    }

    @XmlTransient
    public Set<Giftpreparation> getGiftpreparationList() {
        return giftpreparationList;
    }

    public void setGiftpreparationList(Set<Giftpreparation> giftpreparationList) {
        this.giftpreparationList = giftpreparationList;
    }

    @XmlTransient
    public Set<Loanagent> getLoanagentList() {
        return loanagentList;
    }

    public void setLoanagentList(Set<Loanagent> loanagentList) {
        this.loanagentList = loanagentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userGroupScopeId != null ? userGroupScopeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Discipline)) {
            return false;
        }
        Discipline other = (Discipline) object;
        return !((this.userGroupScopeId == null && other.userGroupScopeId != null) || (this.userGroupScopeId != null && !this.userGroupScopeId.equals(other.userGroupScopeId)));
    }

    @Override
    public String toString() {
        return "se.nrm.dina.datamodel.Discipline[ userGroupScopeId=" + userGroupScopeId + " ]";
    }  
}
