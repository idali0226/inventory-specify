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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute; 
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
//import org.codehaus.jackson.annotate.JsonIgnore; 

/**
 *
 * @author idali
 */
@Entity
@Table(name = "agent")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agent.findAll", query = "SELECT a FROM Agent a"),
    @NamedQuery(name = "Agent.findByAgentID", query = "SELECT a FROM Agent a WHERE a.agentID = :agentID"),  
    @NamedQuery(name = "Agent.findByAgentType", query = "SELECT a FROM Agent a WHERE a.agentType = :agentType"),   
    @NamedQuery(name = "Agent.findByEmail", query = "SELECT a FROM Agent a WHERE a.email = :email"),
    @NamedQuery(name = "Agent.findByFirstName", query = "SELECT a FROM Agent a WHERE a.firstName = :firstName"),
    @NamedQuery(name = "Agent.findByGuid", query = "SELECT a FROM Agent a WHERE a.guid = :guid"),       
    @NamedQuery(name = "Agent.findBySpecifyUserName", query = "SELECT a FROM Agent a WHERE a.specifyUserID.name = :name"),       
    @NamedQuery(name = "Agent.findByName", query = "SELECT a FROM Agent a WHERE a.firstName = :firstName AND a.lastName = :lastName AND a.agentType = :agentType"),  
    @NamedQuery(name = "Agent.findByAgentName", query = "SELECT a FROM Agent a WHERE a.firstName = :firstName AND a.lastName = :lastName"),
    @NamedQuery(name = "Agent.findByLastName", query = "SELECT a FROM Agent a WHERE a.lastName = :lastName"), 
    @NamedQuery(name = "Agent.findByDateType", query = "SELECT a FROM Agent a WHERE a.dateType = :dateType") })
public class Agent extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "AgentID")
    private Integer agentID;
    
    @Size(max = 50)
    @Column(name = "Abbreviation")
    private String abbreviation;
     
    @Basic(optional = false)
    @NotNull
    @Column(name = "AgentType")
    private short agentType;
    
    @Column(name = "DateOfBirth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    
    @Column(name = "DateOfBirthPrecision")
    private Short dateOfBirthPrecision;
    
    @Column(name = "DateOfDeath")
    @Temporal(TemporalType.DATE)
    private Date dateOfDeath;
    
    @Column(name = "DateOfDeathPrecision")
    private Short dateOfDeathPrecision;
    
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "Email")
    private String email;
    
    @Size(max = 50)
    @Column(name = "FirstName")
    private String firstName;
    
    @Size(max = 128)
    @Column(name = "GUID")
    private String guid;
    
    @Size(max = 8)
    @Column(name = "Initials")
    private String initials;
    
    @Size(max = 255)
    @Column(name = "Interests")
    private String interests;
    
    @Size(max = 50)
    @Column(name = "JobTitle")
    private String jobTitle;
    
    @Size(max = 128)
    @Column(name = "LastName")
    private String lastName;
    
    @Size(max = 50)
    @Column(name = "MiddleInitial")
    private String middleInitial;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "Remarks")
    private String remarks;
    
    @Size(max = 50)
    @Column(name = "Title")
    private String title;
    
    @Column(name = "DateType")
    private Short dateType;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "URL")
    private String url;
    
    @Size(max = 50)
    @Column(name = "Suffix")
    private String suffix;
    
         
    @OneToMany(mappedBy = "modifiedByAgentID")
    private Set<Treatmenteventattachment> treatmenteventattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID")
    private Set<Treatmenteventattachment> treatmenteventattachmentList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Container> containerList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Container> containerList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Gift> giftList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Gift> giftList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Geologictimeperiodtreedef> geologictimeperiodtreedefList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Geologictimeperiodtreedef> geologictimeperiodtreedefList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Collectingeventattachment> collectingeventattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Collectingeventattachment> collectingeventattachmentList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Taxontreedef> taxontreedefList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Taxontreedef> taxontreedefList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Preparationattachment> preparationattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Preparationattachment> preparationattachmentList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Determination> determinationList;
    
    @OneToMany(mappedBy = "createdByAgentID",fetch = FetchType.LAZY)
    private Set<Determination> determinationList1;
    
    @OneToMany(mappedBy = "determinerID", fetch = FetchType.LAZY)
    private Set<Determination> determinationList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Discipline> disciplineList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Discipline> disciplineList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Spfieldvaluedefault> spfieldvaluedefaultList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Spfieldvaluedefault> spfieldvaluedefaultList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Exsiccataitem> exsiccataitemList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Exsiccataitem> exsiccataitemList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Preparation> preparationList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Preparation> preparationList1;
    
    @OneToMany(mappedBy = "preparedByID", fetch = FetchType.LAZY)
    private Set<Preparation> preparationList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Division> divisionList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Division> divisionList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Sptasksemaphore> sptasksemaphoreList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Sptasksemaphore> sptasksemaphoreList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Repositoryagreementattachment> repositoryagreementattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Repositoryagreementattachment> repositoryagreementattachmentList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Geography> geographyList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Geography> geographyList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID", fetch = FetchType.LAZY)
    private Set<Agentattachment> agentattachmentList;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Agentattachment> agentattachmentList1;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Agentattachment> agentattachmentList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Picklistitem> picklistitemList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Picklistitem> picklistitemList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Permitattachment> permitattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Permitattachment> permitattachmentList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Collectingtrip> collectingtripList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Collectingtrip> collectingtripList1;
    
    @OneToMany(mappedBy = "agentID", fetch = FetchType.LAZY)
    private Set<Geocoorddetail> geocoorddetailList;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Geocoorddetail> geocoorddetailList1;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Geocoorddetail> geocoorddetailList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Dnasequencingrun> dnasequencingrunList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Dnasequencingrun> dnasequencingrunList1;
    
    @OneToMany(mappedBy = "runByAgentID", fetch = FetchType.LAZY)
    private Set<Dnasequencingrun> dnasequencingrunList2;
    
    @OneToMany(mappedBy = "preparedByAgentID", fetch = FetchType.LAZY)
    private Set<Dnasequencingrun> dnasequencingrunList3;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Preptype> preptypeList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Preptype> preptypeList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Conservdescription> conservdescriptionList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Conservdescription> conservdescriptionList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Locality> localityList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Locality> localityList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Accessionattachment> accessionattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Accessionattachment> accessionattachmentList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalogedByID", fetch = FetchType.LAZY)
    private Set<Exchangeout> exchangeoutList;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Exchangeout> exchangeoutList1;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Exchangeout> exchangeoutList2;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sentToOrganizationID", fetch = FetchType.LAZY)
    private Set<Exchangeout> exchangeoutList3;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Borrowmaterial> borrowmaterialList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Borrowmaterial> borrowmaterialList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Dnasequenceattachment> dnasequenceattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Dnasequenceattachment> dnasequenceattachmentList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID",fetch = FetchType.LAZY)
    private Set<Agentvariant> agentvariantList;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Agentvariant> agentvariantList1;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Agentvariant> agentvariantList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Localitycitation> localitycitationList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Localitycitation> localitycitationList1;
    
    @OneToMany(mappedBy = "agentID", fetch = FetchType.LAZY)
    private Set<Fieldnotebookpageset> fieldnotebookpagesetList;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Fieldnotebookpageset> fieldnotebookpagesetList1;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Fieldnotebookpageset> fieldnotebookpagesetList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Paleocontext> paleocontextList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Paleocontext> paleocontextList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Collectionobjectattribute> collectionobjectattributeList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Collectionobjectattribute> collectionobjectattributeList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Collectionobjectattachment> collectionobjectattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Collectionobjectattachment> collectionobjectattachmentList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY )
    private Set<Storageattachment> storageattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Storageattachment> storageattachmentList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Exsiccata> exsiccataList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Exsiccata> exsiccataList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Attachmentmetadata> attachmentmetadataList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Attachmentmetadata> attachmentmetadataList1;
    
    @OneToMany(mappedBy = "receivedByID", fetch = FetchType.LAZY)
    private Set<Loanreturnpreparation> loanreturnpreparationList;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Loanreturnpreparation> loanreturnpreparationList1;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Loanreturnpreparation> loanreturnpreparationList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Localitynamealias> localitynamealiasList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Localitynamealias> localitynamealiasList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Recordset> recordsetList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Recordset> recordsetList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Spappresource> spappresourceList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Spappresource> spappresourceList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Taxonattachment> taxonattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Taxonattachment> taxonattachmentList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Storagetreedefitem> storagetreedefitemList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Storagetreedefitem> storagetreedefitemList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Determinationcitation> determinationcitationList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Determinationcitation> determinationcitationList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Conserveventattachment> conserveventattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Conserveventattachment> conserveventattachmentList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Lithostrattreedefitem> lithostrattreedefitemList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Lithostrattreedefitem> lithostrattreedefitemList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Journal> journalList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Journal> journalList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Datatype> datatypeList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Datatype> datatypeList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Collectionreltype> collectionreltypeList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Collectionreltype> collectionreltypeList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Taxon> taxonList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Taxon> taxonList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Localityattachment> localityattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Localityattachment> localityattachmentList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Treatmentevent> treatmenteventList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Treatmentevent> treatmenteventList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Storagetreedef> storagetreedefList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Storagetreedef> storagetreedefList1;
    
    @OneToMany(mappedBy = "agentID", fetch = FetchType.LAZY)
    private Set<Address> addressList;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Address> addressList1;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Address> addressList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Spexportschemaitem> spexportschemaitemList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Spexportschemaitem> spexportschemaitemList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Splocaleitemstr> splocaleitemstrList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Splocaleitemstr> splocaleitemstrList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Workbenchtemplatemappingitem> workbenchtemplatemappingitemList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Workbenchtemplatemappingitem> workbenchtemplatemappingitemList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Lithostrattreedef> lithostrattreedefList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Lithostrattreedef> lithostrattreedefList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Autonumberingscheme> autonumberingschemeList;
 
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Autonumberingscheme> autonumberingschemeList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Conservdescriptionattachment> conservdescriptionattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Conservdescriptionattachment> conservdescriptionattachmentList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Collectingeventattribute> collectingeventattributeList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Collectingeventattribute> collectingeventattributeList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Spquery> spqueryList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Spquery> spqueryList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Geographytreedef> geographytreedefList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Geographytreedef> geographytreedefList1;
    
    @OneToMany(mappedBy = "agentID", fetch = FetchType.LAZY)
    private Set<Addressofrecord> addressofrecordList;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Addressofrecord> addressofrecordList1;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Addressofrecord> addressofrecordList2;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID", fetch = FetchType.LAZY)
    private Set<Deaccessionagent> deaccessionagentList;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Deaccessionagent> deaccessionagentList1;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Deaccessionagent> deaccessionagentList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Borrow> borrowList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Borrow> borrowList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Localitydetail> localitydetailList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Localitydetail> localitydetailList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Commonnametxcitation> commonnametxcitationList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Commonnametxcitation> commonnametxcitationList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Spqueryfield> spqueryfieldList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Spqueryfield> spqueryfieldList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "memberID", fetch = FetchType.LAZY)
    private Set<Groupperson> grouppersonList;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Groupperson> grouppersonList1;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Groupperson> grouppersonList2;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupID", fetch = FetchType.LAZY)
    private Set<Groupperson> grouppersonList3;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Attributedef> attributedefList;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Attributedef> attributedefList1;
    
    @JoinColumn(name = "CollectionCCID", referencedColumnName = "UserGroupScopeId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Collection collectionCCID;
    
    @JoinColumn(name = "CollectionTCID", referencedColumnName = "UserGroupScopeId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Collection collectionTCID;
    
    @JoinColumn(name = "SpecifyUserID", referencedColumnName = "SpecifyUserID")
    @ManyToOne
    private Specifyuser specifyUserID;
    
    @OneToMany(mappedBy = "modifiedByAgentID", fetch = FetchType.LAZY)
    private Set<Agent> agentList;
    
    @JoinColumn(name = "ModifiedByAgentID", referencedColumnName = "AgentID")
    @ManyToOne
    private Agent modifiedByAgentID;
    
    @OneToMany(mappedBy = "createdByAgentID", fetch = FetchType.LAZY)
    private Set<Agent> agentList1;
    
    @JoinColumn(name = "CreatedByAgentID", referencedColumnName = "AgentID")
    @ManyToOne
    private Agent createdByAgentID;
    
    @OneToMany(mappedBy = "parentOrganizationID", fetch = FetchType.LAZY)
    private Set<Agent> agentList2;
    
    @JoinColumn(name = "ParentOrganizationID", referencedColumnName = "AgentID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Agent parentOrganizationID;
    
    @JoinColumn(name = "InstitutionCCID", referencedColumnName = "UserGroupScopeId")
    @ManyToOne(  fetch = FetchType.LAZY)
    private Institution institutionCCID;
    
    @JoinColumn(name = "InstitutionTCID", referencedColumnName = "UserGroupScopeId")
    @ManyToOne(  fetch = FetchType.LAZY)
    private Institution institutionTCID;
    
    @JoinColumn(name = "DivisionID", referencedColumnName = "UserGroupScopeId")
    @ManyToOne(  fetch = FetchType.EAGER)
    private Division divisionID;
    
    @OneToMany(mappedBy = "catalogerID",  fetch = FetchType.LAZY )
    private Set<Collectionobject> collectionobjectList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Collectionobject> collectionobjectList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Collectionobject> collectionobjectList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Project> projectList;
    
    @OneToMany(mappedBy = "projectAgentID",  fetch = FetchType.LAZY )
    private Set<Project> projectList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Project> projectList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Splocalecontaineritem> splocalecontaineritemList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Splocalecontaineritem> splocalecontaineritemList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Institution> institutionList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Institution> institutionList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Collectingevent> collectingeventList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Collectingevent> collectingeventList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID",  fetch = FetchType.LAZY )
    private Set<Fieldnotebook> fieldnotebookList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Fieldnotebook> fieldnotebookList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Fieldnotebook> fieldnotebookList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Geologictimeperiod> geologictimeperiodList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Geologictimeperiod> geologictimeperiodList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Lithostrat> lithostratList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Lithostrat> lithostratList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Taxoncitation> taxoncitationList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Taxoncitation> taxoncitationList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Dnasequencingruncitation> dnasequencingruncitationList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Dnasequencingruncitation> dnasequencingruncitationList1;
    
    @OneToMany(mappedBy = "agentID",  fetch = FetchType.LAZY )
    private Set<Dnasequence> dnasequenceList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Dnasequence> dnasequenceList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Dnasequence> dnasequenceList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Preparationattr> preparationattrList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Preparationattr> preparationattrList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Borrowreturnmaterial> borrowreturnmaterialList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Borrowreturnmaterial> borrowreturnmaterialList1;
    
    @OneToMany(mappedBy = "returnedByID",  fetch = FetchType.LAZY )
    private Set<Borrowreturnmaterial> borrowreturnmaterialList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Workbench> workbenchList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Workbench> workbenchList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Spauditlogfield> spauditlogfieldList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Spauditlogfield> spauditlogfieldList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Referencework> referenceworkList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Referencework> referenceworkList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Shipment> shipmentList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Shipment> shipmentList1;
    
    @OneToMany(mappedBy = "shippedByID",  fetch = FetchType.LAZY )
    private Set<Shipment> shipmentList2;
    
    @OneToMany(mappedBy = "shippedToID",  fetch = FetchType.LAZY )
    private Set<Shipment> shipmentList3;
    
    @OneToMany(mappedBy = "shipperID",  fetch = FetchType.LAZY )
    private Set<Shipment> shipmentList4;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID",  fetch = FetchType.LAZY )
    private Set<Repositoryagreement> repositoryagreementList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Repositoryagreement> repositoryagreementList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Repositoryagreement> repositoryagreementList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Splocalecontainer> splocalecontainerList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Splocalecontainer> splocalecontainerList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID",  fetch = FetchType.LAZY )
    private Set<Author> authorList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Author> authorList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Author> authorList2;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID",  fetch = FetchType.LAZY )
    private Set<Accessionagent> accessionagentList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Accessionagent> accessionagentList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Accessionagent> accessionagentList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Workbenchtemplate> workbenchtemplateList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Workbenchtemplate> workbenchtemplateList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Taxontreedefitem> taxontreedefitemList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Taxontreedefitem> taxontreedefitemList1;
    
    @OneToMany(mappedBy = "examinedByAgentID",  fetch = FetchType.LAZY )
    private Set<Conservevent> conserveventList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Conservevent> conserveventList1;
    
    @OneToMany(mappedBy = "treatedByAgentID",  fetch = FetchType.LAZY )
    private Set<Conservevent> conserveventList2;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Conservevent> conserveventList3;
    
    @OneToMany(mappedBy = "curatorID",  fetch = FetchType.LAZY )
    private Set<Conservevent> conserveventList4;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Collection> collectionList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Collection> collectionList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Fieldnotebookpageattachment> fieldnotebookpageattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Fieldnotebookpageattachment> fieldnotebookpageattachmentList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Spprincipal> spprincipalList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Spprincipal> spprincipalList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Fieldnotebookattachment> fieldnotebookattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Fieldnotebookattachment> fieldnotebookattachmentList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Preparationattribute> preparationattributeList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Preparationattribute> preparationattributeList1;
    
    @OneToMany(mappedBy = "agentID",  fetch = FetchType.LAZY )
    private Set<Inforequest> inforequestList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Inforequest> inforequestList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Inforequest> inforequestList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY ) 
    private Set<Collectingeventattr> collectingeventattrList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Collectingeventattr> collectingeventattrList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Spappresourcedir> spappresourcedirList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Spappresourcedir> spappresourcedirList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Spexportschema> spexportschemaList;
 
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Spexportschema> spexportschemaList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Collectionrelationship> collectionrelationshipList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Collectionrelationship> collectionrelationshipList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Attachmenttag> attachmenttagList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Attachmenttag> attachmenttagList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Geographytreedefitem> geographytreedefitemList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Geographytreedefitem> geographytreedefitemList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID",  fetch = FetchType.LAZY )
    private Set<Borrowagent> borrowagentList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Borrowagent> borrowagentList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Borrowagent> borrowagentList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Geologictimeperiodtreedefitem> geologictimeperiodtreedefitemList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Geologictimeperiodtreedefitem> geologictimeperiodtreedefitemList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Loan> loanList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Loan> loanList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Fieldnotebookpage> fieldnotebookpageList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Fieldnotebookpage> fieldnotebookpageList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID",  fetch = FetchType.LAZY )
    private Set<Agentspecialty> agentspecialtyList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Agentspecialty> agentspecialtyList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Agentspecialty> agentspecialtyList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Picklist> picklistList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Picklist> picklistList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Accession> accessionList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Accession> accessionList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Storage> storageList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Storage> storageList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Dnasequencerunattachment> dnasequencerunattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Dnasequencerunattachment> dnasequencerunattachmentList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID",  fetch = FetchType.LAZY )
    private Set<Giftagent> giftagentList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Giftagent> giftagentList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Giftagent> giftagentList2;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID",  fetch = FetchType.LAZY )
    private Set<Collector> collectorList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Collector> collectorList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Collector> collectorList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Attachment> attachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Attachment> attachmentList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID",  fetch = FetchType.LAZY )
    private Set<Appraisal> appraisalList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Appraisal> appraisalList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Appraisal> appraisalList2;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID",  fetch = FetchType.LAZY )
    private Set<Agentgeography> agentgeographyList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Agentgeography> agentgeographyList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Agentgeography> agentgeographyList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Collectionobjectcitation> collectionobjectcitationList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Collectionobjectcitation> collectionobjectcitationList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Spappresourcedata> spappresourcedataList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Spappresourcedata> spappresourcedataList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Spexportschemaitemmapping> spexportschemaitemmappingList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Spexportschemaitemmapping> spexportschemaitemmappingList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Specifyuser> specifyuserList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Specifyuser> specifyuserList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalogedByID",  fetch = FetchType.LAZY )
    private Set<Exchangein> exchangeinList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Exchangein> exchangeinList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Exchangein> exchangeinList2;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receivedFromOrganizationID",  fetch = FetchType.LAZY )
    private Set<Exchangein> exchangeinList3;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Fieldnotebookpagesetattachment> fieldnotebookpagesetattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Fieldnotebookpagesetattachment> fieldnotebookpagesetattachmentList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Commonnametx> commonnametxList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Commonnametx> commonnametxList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Loanpreparation> loanpreparationList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Loanpreparation> loanpreparationList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Otheridentifier> otheridentifierList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Otheridentifier> otheridentifierList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Spexportschemamapping> spexportschemamappingList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Spexportschemamapping> spexportschemamappingList1;
 
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Giftpreparation> giftpreparationList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Giftpreparation> giftpreparationList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Spversion> spversionList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Spversion> spversionList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID",  fetch = FetchType.LAZY )
    private Set<Loanagent> loanagentList;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Loanagent> loanagentList1;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Loanagent> loanagentList2;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Spviewsetobj> spviewsetobjList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Spviewsetobj> spviewsetobjList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Deaccessionpreparation> deaccessionpreparationList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Deaccessionpreparation> deaccessionpreparationList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Accessionauthorization> accessionauthorizationList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Accessionauthorization> accessionauthorizationList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Permit> permitList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Permit> permitList1;
    
    @OneToMany(mappedBy = "issuedByID",  fetch = FetchType.LAZY )
    private Set<Permit> permitList2;
    
    @OneToMany(mappedBy = "issuedToID",  fetch = FetchType.LAZY )
    private Set<Permit> permitList3;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Collectionobjectattr> collectionobjectattrList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Collectionobjectattr> collectionobjectattrList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Spauditlog> spauditlogList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Spauditlog> spauditlogList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Spreport> spreportList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Spreport> spreportList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Deaccession> deaccessionList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Deaccession> deaccessionList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Loanattachment> loanattachmentList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Loanattachment> loanattachmentList1;
    
    @OneToMany(mappedBy = "modifiedByAgentID",  fetch = FetchType.LAZY )
    private Set<Spsymbiotainstance> spsymbiotainstanceList;
    
    @OneToMany(mappedBy = "createdByAgentID",  fetch = FetchType.LAZY )
    private Set<Spsymbiotainstance> spsymbiotainstanceList1;

    public Agent() {
    }

    public Agent(Integer agentID) {
        this.agentID = agentID;
    }

    public Agent(Integer agentID, Date timestampCreated, short agentType) {
        this.agentID = agentID;
        this.timestampCreated = timestampCreated;
        this.agentType = agentType;
    }
 
    @XmlID
    @XmlAttribute(name = "id") 
    @Override
    public String getIdentityString() {
        return String.valueOf(agentID);
    }
    
//    @XmlAttribute(name = "uuid") 
//    @Override
//    public String getUUID() {
//        return Util.getInstance().getURLLink(this.getClass().getSimpleName()) + agentID;
//    }

    @XmlTransient 
    @Override
    public int getEntityId() {
        return agentID;
    }
     
    public Integer getAgentID() {
        return agentID;
    }

    public void setAgentID(Integer agentID) {
        this.agentID = agentID;
    }

    
    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public short getAgentType() {
        return agentType;
    }

    public void setAgentType(short agentType) {
        this.agentType = agentType;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Short getDateOfBirthPrecision() {
        return dateOfBirthPrecision;
    }

    public void setDateOfBirthPrecision(Short dateOfBirthPrecision) {
        this.dateOfBirthPrecision = dateOfBirthPrecision;
    }

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public Short getDateOfDeathPrecision() {
        return dateOfDeathPrecision;
    }

    public void setDateOfDeathPrecision(Short dateOfDeathPrecision) {
        this.dateOfDeathPrecision = dateOfDeathPrecision;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Short getDateType() {
        return dateType;
    }

    public void setDateType(Short dateType) {
        this.dateType = dateType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @XmlTransient
    public Set<Container> getContainerList() {
        return containerList;
    }

    public void setContainerList(Set<Container> containerList) {
        this.containerList = containerList;
    }

    @XmlTransient
    public Set<Container> getContainerList1() {
        return containerList1;
    }

    public void setContainerList1(Set<Container> containerList1) {
        this.containerList1 = containerList1;
    }

    @XmlTransient
    public Set<Gift> getGiftList() {
        return giftList;
    }

    public void setGiftList(Set<Gift> giftList) {
        this.giftList = giftList;
    }

    @XmlTransient
    public Set<Gift> getGiftList1() {
        return giftList1;
    }

    public void setGiftList1(Set<Gift> giftList1) {
        this.giftList1 = giftList1;
    }

    @XmlTransient
    public Set<Geologictimeperiodtreedef> getGeologictimeperiodtreedefList() {
        return geologictimeperiodtreedefList;
    }

    public void setGeologictimeperiodtreedefList(Set<Geologictimeperiodtreedef> geologictimeperiodtreedefList) {
        this.geologictimeperiodtreedefList = geologictimeperiodtreedefList;
    }

    @XmlTransient
    public Set<Geologictimeperiodtreedef> getGeologictimeperiodtreedefList1() {
        return geologictimeperiodtreedefList1;
    }

    public void setGeologictimeperiodtreedefList1(Set<Geologictimeperiodtreedef> geologictimeperiodtreedefList1) {
        this.geologictimeperiodtreedefList1 = geologictimeperiodtreedefList1;
    }

    @XmlTransient
    public Set<Collectingeventattachment> getCollectingeventattachmentList() {
        return collectingeventattachmentList;
    }

    public void setCollectingeventattachmentList(Set<Collectingeventattachment> collectingeventattachmentList) {
        this.collectingeventattachmentList = collectingeventattachmentList;
    }

    @XmlTransient
    public Set<Collectingeventattachment> getCollectingeventattachmentList1() {
        return collectingeventattachmentList1;
    }

    public void setCollectingeventattachmentList1(Set<Collectingeventattachment> collectingeventattachmentList1) {
        this.collectingeventattachmentList1 = collectingeventattachmentList1;
    }

    @XmlTransient
    public Set<Taxontreedef> getTaxontreedefList() {
        return taxontreedefList;
    }

    public void setTaxontreedefList(Set<Taxontreedef> taxontreedefList) {
        this.taxontreedefList = taxontreedefList;
    }

    @XmlTransient
    public Set<Taxontreedef> getTaxontreedefList1() {
        return taxontreedefList1;
    }

    public void setTaxontreedefList1(Set<Taxontreedef> taxontreedefList1) {
        this.taxontreedefList1 = taxontreedefList1;
    }

    @XmlTransient
    public Set<Preparationattachment> getPreparationattachmentList() {
        return preparationattachmentList;
    }

    public void setPreparationattachmentList(Set<Preparationattachment> preparationattachmentList) {
        this.preparationattachmentList = preparationattachmentList;
    }

    @XmlTransient
    public Set<Preparationattachment> getPreparationattachmentList1() {
        return preparationattachmentList1;
    }

    public void setPreparationattachmentList1(Set<Preparationattachment> preparationattachmentList1) {
        this.preparationattachmentList1 = preparationattachmentList1;
    }

    @XmlTransient
    public Set<Determination> getDeterminationList() {
        return determinationList;
    }

    public void setDeterminationList(Set<Determination> determinationList) {
        this.determinationList = determinationList;
    }

    @XmlTransient
    public Set<Determination> getDeterminationList1() {
        return determinationList1;
    }

    public void setDeterminationList1(Set<Determination> determinationList1) {
        this.determinationList1 = determinationList1;
    }

    @XmlTransient
    public Set<Determination> getDeterminationList2() {
        return determinationList2;
    }

    public void setDeterminationList2(Set<Determination> determinationList2) {
        this.determinationList2 = determinationList2;
    }

    @XmlTransient
    public Set<Discipline> getDisciplineList() {
        return disciplineList;
    }

    public void setDisciplineList(Set<Discipline> disciplineList) {
        this.disciplineList = disciplineList;
    }

    @XmlTransient
    public Set<Discipline> getDisciplineList1() {
        return disciplineList1;
    }

    public void setDisciplineList1(Set<Discipline> disciplineList1) {
        this.disciplineList1 = disciplineList1;
    }

    @XmlTransient
    public Set<Spfieldvaluedefault> getSpfieldvaluedefaultList() {
        return spfieldvaluedefaultList;
    }

    public void setSpfieldvaluedefaultList(Set<Spfieldvaluedefault> spfieldvaluedefaultList) {
        this.spfieldvaluedefaultList = spfieldvaluedefaultList;
    }

    @XmlTransient
    public Set<Spfieldvaluedefault> getSpfieldvaluedefaultList1() {
        return spfieldvaluedefaultList1;
    }

    public void setSpfieldvaluedefaultList1(Set<Spfieldvaluedefault> spfieldvaluedefaultList1) {
        this.spfieldvaluedefaultList1 = spfieldvaluedefaultList1;
    }

    @XmlTransient
    public Set<Exsiccataitem> getExsiccataitemList() {
        return exsiccataitemList;
    }

    public void setExsiccataitemList(Set<Exsiccataitem> exsiccataitemList) {
        this.exsiccataitemList = exsiccataitemList;
    }

    @XmlTransient
    public Set<Exsiccataitem> getExsiccataitemList1() {
        return exsiccataitemList1;
    }

    public void setExsiccataitemList1(Set<Exsiccataitem> exsiccataitemList1) {
        this.exsiccataitemList1 = exsiccataitemList1;
    }

    @XmlTransient
    public Set<Preparation> getPreparationList() {
        return preparationList;
    }

    public void setPreparationList(Set<Preparation> preparationList) {
        this.preparationList = preparationList;
    }

    @XmlTransient
    public Set<Preparation> getPreparationList1() {
        return preparationList1;
    }

    public void setPreparationList1(Set<Preparation> preparationList1) {
        this.preparationList1 = preparationList1;
    }

    @XmlTransient
    public Set<Preparation> getPreparationList2() {
        return preparationList2;
    }

    public void setPreparationList2(Set<Preparation> preparationList2) {
        this.preparationList2 = preparationList2;
    }

    @XmlTransient
    public Set<Division> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(Set<Division> divisionList) {
        this.divisionList = divisionList;
    }

    @XmlTransient
    public Set<Division> getDivisionList1() {
        return divisionList1;
    }

    public void setDivisionList1(Set<Division> divisionList1) {
        this.divisionList1 = divisionList1;
    }

    @XmlTransient
    public Set<Sptasksemaphore> getSptasksemaphoreList() {
        return sptasksemaphoreList;
    }

    public void setSptasksemaphoreList(Set<Sptasksemaphore> sptasksemaphoreList) {
        this.sptasksemaphoreList = sptasksemaphoreList;
    }

    @XmlTransient
    public Set<Sptasksemaphore> getSptasksemaphoreList1() {
        return sptasksemaphoreList1;
    }

    public void setSptasksemaphoreList1(Set<Sptasksemaphore> sptasksemaphoreList1) {
        this.sptasksemaphoreList1 = sptasksemaphoreList1;
    }

    @XmlTransient
    public Set<Repositoryagreementattachment> getRepositoryagreementattachmentList() {
        return repositoryagreementattachmentList;
    }

    public void setRepositoryagreementattachmentList(Set<Repositoryagreementattachment> repositoryagreementattachmentList) {
        this.repositoryagreementattachmentList = repositoryagreementattachmentList;
    }

    @XmlTransient
    public Set<Repositoryagreementattachment> getRepositoryagreementattachmentList1() {
        return repositoryagreementattachmentList1;
    }

    public void setRepositoryagreementattachmentList1(Set<Repositoryagreementattachment> repositoryagreementattachmentList1) {
        this.repositoryagreementattachmentList1 = repositoryagreementattachmentList1;
    }

    @XmlTransient
    public Set<Geography> getGeographyList() {
        return geographyList;
    }

    public void setGeographyList(Set<Geography> geographyList) {
        this.geographyList = geographyList;
    }

    @XmlTransient
    public Set<Geography> getGeographyList1() {
        return geographyList1;
    }

    public void setGeographyList1(Set<Geography> geographyList1) {
        this.geographyList1 = geographyList1;
    }

    @XmlTransient
    public Set<Agentattachment> getAgentattachmentList() {
        return agentattachmentList;
    }

    public void setAgentattachmentList(Set<Agentattachment> agentattachmentList) {
        this.agentattachmentList = agentattachmentList;
    }

    @XmlTransient
    public Set<Agentattachment> getAgentattachmentList1() {
        return agentattachmentList1;
    }

    public void setAgentattachmentList1(Set<Agentattachment> agentattachmentList1) {
        this.agentattachmentList1 = agentattachmentList1;
    }

    @XmlTransient
    public Set<Agentattachment> getAgentattachmentList2() {
        return agentattachmentList2;
    }

    public void setAgentattachmentList2(Set<Agentattachment> agentattachmentList2) {
        this.agentattachmentList2 = agentattachmentList2;
    }

    @XmlTransient
    public Set<Picklistitem> getPicklistitemList() {
        return picklistitemList;
    }

    public void setPicklistitemList(Set<Picklistitem> picklistitemList) {
        this.picklistitemList = picklistitemList;
    }

    @XmlTransient
    public Set<Picklistitem> getPicklistitemList1() {
        return picklistitemList1;
    }

    public void setPicklistitemList1(Set<Picklistitem> picklistitemList1) {
        this.picklistitemList1 = picklistitemList1;
    }

    @XmlTransient
    public Set<Permitattachment> getPermitattachmentList() {
        return permitattachmentList;
    }

    public void setPermitattachmentList(Set<Permitattachment> permitattachmentList) {
        this.permitattachmentList = permitattachmentList;
    }

    @XmlTransient
    public Set<Permitattachment> getPermitattachmentList1() {
        return permitattachmentList1;
    }

    public void setPermitattachmentList1(Set<Permitattachment> permitattachmentList1) {
        this.permitattachmentList1 = permitattachmentList1;
    }

    @XmlTransient
    public Set<Collectingtrip> getCollectingtripList() {
        return collectingtripList;
    }

    public void setCollectingtripList(Set<Collectingtrip> collectingtripList) {
        this.collectingtripList = collectingtripList;
    }

    @XmlTransient
    public Set<Collectingtrip> getCollectingtripList1() {
        return collectingtripList1;
    }

    public void setCollectingtripList1(Set<Collectingtrip> collectingtripList1) {
        this.collectingtripList1 = collectingtripList1;
    }

    @XmlTransient
    public Set<Geocoorddetail> getGeocoorddetailList() {
        return geocoorddetailList;
    }

    public void setGeocoorddetailList(Set<Geocoorddetail> geocoorddetailList) {
        this.geocoorddetailList = geocoorddetailList;
    }

    @XmlTransient
    public Set<Geocoorddetail> getGeocoorddetailList1() {
        return geocoorddetailList1;
    }

    public void setGeocoorddetailList1(Set<Geocoorddetail> geocoorddetailList1) {
        this.geocoorddetailList1 = geocoorddetailList1;
    }

    @XmlTransient
    public Set<Geocoorddetail> getGeocoorddetailList2() {
        return geocoorddetailList2;
    }

    public void setGeocoorddetailList2(Set<Geocoorddetail> geocoorddetailList2) {
        this.geocoorddetailList2 = geocoorddetailList2;
    }

    @XmlTransient
    public Set<Dnasequencingrun> getDnasequencingrunList() {
        return dnasequencingrunList;
    }

    public void setDnasequencingrunList(Set<Dnasequencingrun> dnasequencingrunList) {
        this.dnasequencingrunList = dnasequencingrunList;
    }

    @XmlTransient
    public Set<Dnasequencingrun> getDnasequencingrunList1() {
        return dnasequencingrunList1;
    }

    public void setDnasequencingrunList1(Set<Dnasequencingrun> dnasequencingrunList1) {
        this.dnasequencingrunList1 = dnasequencingrunList1;
    }

    @XmlTransient
    public Set<Dnasequencingrun> getDnasequencingrunList2() {
        return dnasequencingrunList2;
    }

    public void setDnasequencingrunList2(Set<Dnasequencingrun> dnasequencingrunList2) {
        this.dnasequencingrunList2 = dnasequencingrunList2;
    }

    @XmlTransient
    public Set<Dnasequencingrun> getDnasequencingrunList3() {
        return dnasequencingrunList3;
    }

    public void setDnasequencingrunList3(Set<Dnasequencingrun> dnasequencingrunList3) {
        this.dnasequencingrunList3 = dnasequencingrunList3;
    }

    @XmlTransient
    public Set<Preptype> getPreptypeList() {
        return preptypeList;
    }

    public void setPreptypeList(Set<Preptype> preptypeList) {
        this.preptypeList = preptypeList;
    }

    @XmlTransient
    public Set<Preptype> getPreptypeList1() {
        return preptypeList1;
    }

    public void setPreptypeList1(Set<Preptype> preptypeList1) {
        this.preptypeList1 = preptypeList1;
    }

    @XmlTransient
    public Set<Conservdescription> getConservdescriptionList() {
        return conservdescriptionList;
    }

    public void setConservdescriptionList(Set<Conservdescription> conservdescriptionList) {
        this.conservdescriptionList = conservdescriptionList;
    }

    @XmlTransient
    public Set<Conservdescription> getConservdescriptionList1() {
        return conservdescriptionList1;
    }

    public void setConservdescriptionList1(Set<Conservdescription> conservdescriptionList1) {
        this.conservdescriptionList1 = conservdescriptionList1;
    }

    @XmlTransient
    public Set<Locality> getLocalityList() {
        return localityList;
    }

    public void setLocalityList(Set<Locality> localityList) {
        this.localityList = localityList;
    }

    @XmlTransient
    public Set<Locality> getLocalityList1() {
        return localityList1;
    }

    public void setLocalityList1(Set<Locality> localityList1) {
        this.localityList1 = localityList1;
    }

    @XmlTransient
    public Set<Accessionattachment> getAccessionattachmentList() {
        return accessionattachmentList;
    }

    public void setAccessionattachmentList(Set<Accessionattachment> accessionattachmentList) {
        this.accessionattachmentList = accessionattachmentList;
    }

    @XmlTransient
    public Set<Accessionattachment> getAccessionattachmentList1() {
        return accessionattachmentList1;
    }

    public void setAccessionattachmentList1(Set<Accessionattachment> accessionattachmentList1) {
        this.accessionattachmentList1 = accessionattachmentList1;
    }

    @XmlTransient
    public Set<Exchangeout> getExchangeoutList() {
        return exchangeoutList;
    }

    public void setExchangeoutList(Set<Exchangeout> exchangeoutList) {
        this.exchangeoutList = exchangeoutList;
    }

    @XmlTransient
    public Set<Exchangeout> getExchangeoutList1() {
        return exchangeoutList1;
    }

    public void setExchangeoutList1(Set<Exchangeout> exchangeoutList1) {
        this.exchangeoutList1 = exchangeoutList1;
    }

    @XmlTransient
    public Set<Exchangeout> getExchangeoutList2() {
        return exchangeoutList2;
    }

    public void setExchangeoutList2(Set<Exchangeout> exchangeoutList2) {
        this.exchangeoutList2 = exchangeoutList2;
    }

    @XmlTransient
    public Set<Exchangeout> getExchangeoutList3() {
        return exchangeoutList3;
    }

    public void setExchangeoutList3(Set<Exchangeout> exchangeoutList3) {
        this.exchangeoutList3 = exchangeoutList3;
    }

    @XmlTransient
    public Set<Borrowmaterial> getBorrowmaterialList() {
        return borrowmaterialList;
    }

    public void setBorrowmaterialList(Set<Borrowmaterial> borrowmaterialList) {
        this.borrowmaterialList = borrowmaterialList;
    }

    @XmlTransient
    public Set<Borrowmaterial> getBorrowmaterialList1() {
        return borrowmaterialList1;
    }

    public void setBorrowmaterialList1(Set<Borrowmaterial> borrowmaterialList1) {
        this.borrowmaterialList1 = borrowmaterialList1;
    }

    @XmlTransient
    public Set<Dnasequenceattachment> getDnasequenceattachmentList() {
        return dnasequenceattachmentList;
    }

    public void setDnasequenceattachmentList(Set<Dnasequenceattachment> dnasequenceattachmentList) {
        this.dnasequenceattachmentList = dnasequenceattachmentList;
    }

    @XmlTransient
    public Set<Dnasequenceattachment> getDnasequenceattachmentList1() {
        return dnasequenceattachmentList1;
    }

    public void setDnasequenceattachmentList1(Set<Dnasequenceattachment> dnasequenceattachmentList1) {
        this.dnasequenceattachmentList1 = dnasequenceattachmentList1;
    }

    @XmlTransient
    public Set<Agentvariant> getAgentvariantList() {
        return agentvariantList;
    }

    public void setAgentvariantList(Set<Agentvariant> agentvariantList) {
        this.agentvariantList = agentvariantList;
    }

    @XmlTransient
    public Set<Agentvariant> getAgentvariantList1() {
        return agentvariantList1;
    }

    public void setAgentvariantList1(Set<Agentvariant> agentvariantList1) {
        this.agentvariantList1 = agentvariantList1;
    }

    @XmlTransient
    public Set<Agentvariant> getAgentvariantList2() {
        return agentvariantList2;
    }

    public void setAgentvariantList2(Set<Agentvariant> agentvariantList2) {
        this.agentvariantList2 = agentvariantList2;
    }

    @XmlTransient
    public Set<Localitycitation> getLocalitycitationList() {
        return localitycitationList;
    }

    public void setLocalitycitationList(Set<Localitycitation> localitycitationList) {
        this.localitycitationList = localitycitationList;
    }

    @XmlTransient
    public Set<Localitycitation> getLocalitycitationList1() {
        return localitycitationList1;
    }

    public void setLocalitycitationList1(Set<Localitycitation> localitycitationList1) {
        this.localitycitationList1 = localitycitationList1;
    }

    @XmlTransient
    public Set<Fieldnotebookpageset> getFieldnotebookpagesetList() {
        return fieldnotebookpagesetList;
    }

    public void setFieldnotebookpagesetList(Set<Fieldnotebookpageset> fieldnotebookpagesetList) {
        this.fieldnotebookpagesetList = fieldnotebookpagesetList;
    }

    @XmlTransient
    public Set<Fieldnotebookpageset> getFieldnotebookpagesetList1() {
        return fieldnotebookpagesetList1;
    }

    public void setFieldnotebookpagesetList1(Set<Fieldnotebookpageset> fieldnotebookpagesetList1) {
        this.fieldnotebookpagesetList1 = fieldnotebookpagesetList1;
    }

    @XmlTransient
    public Set<Fieldnotebookpageset> getFieldnotebookpagesetList2() {
        return fieldnotebookpagesetList2;
    }

    public void setFieldnotebookpagesetList2(Set<Fieldnotebookpageset> fieldnotebookpagesetList2) {
        this.fieldnotebookpagesetList2 = fieldnotebookpagesetList2;
    }

    @XmlTransient
    public Set<Paleocontext> getPaleocontextList() {
        return paleocontextList;
    }

    public void setPaleocontextList(Set<Paleocontext> paleocontextList) {
        this.paleocontextList = paleocontextList;
    }

    @XmlTransient
    public Set<Paleocontext> getPaleocontextList1() {
        return paleocontextList1;
    }

    public void setPaleocontextList1(Set<Paleocontext> paleocontextList1) {
        this.paleocontextList1 = paleocontextList1;
    }

    @XmlTransient
    public Set<Collectionobjectattribute> getCollectionobjectattributeList() {
        return collectionobjectattributeList;
    }

    public void setCollectionobjectattributeList(Set<Collectionobjectattribute> collectionobjectattributeList) {
        this.collectionobjectattributeList = collectionobjectattributeList;
    }

    @XmlTransient
    public Set<Collectionobjectattribute> getCollectionobjectattributeList1() {
        return collectionobjectattributeList1;
    }

    public void setCollectionobjectattributeList1(Set<Collectionobjectattribute> collectionobjectattributeList1) {
        this.collectionobjectattributeList1 = collectionobjectattributeList1;
    }

    @XmlTransient
    public Set<Collectionobjectattachment> getCollectionobjectattachmentList() {
        return collectionobjectattachmentList;
    }

    public void setCollectionobjectattachmentList(Set<Collectionobjectattachment> collectionobjectattachmentList) {
        this.collectionobjectattachmentList = collectionobjectattachmentList;
    }

    @XmlTransient
    public Set<Collectionobjectattachment> getCollectionobjectattachmentList1() {
        return collectionobjectattachmentList1;
    }

    public void setCollectionobjectattachmentList1(Set<Collectionobjectattachment> collectionobjectattachmentList1) {
        this.collectionobjectattachmentList1 = collectionobjectattachmentList1;
    }

    @XmlTransient
    public Set<Storageattachment> getStorageattachmentList() {
        return storageattachmentList;
    }

    public void setStorageattachmentList(Set<Storageattachment> storageattachmentList) {
        this.storageattachmentList = storageattachmentList;
    }

    @XmlTransient
    public Set<Storageattachment> getStorageattachmentList1() {
        return storageattachmentList1;
    }

    public void setStorageattachmentList1(Set<Storageattachment> storageattachmentList1) {
        this.storageattachmentList1 = storageattachmentList1;
    }

    @XmlTransient
    public Set<Exsiccata> getExsiccataList() {
        return exsiccataList;
    }

    public void setExsiccataList(Set<Exsiccata> exsiccataList) {
        this.exsiccataList = exsiccataList;
    }

    @XmlTransient
    public Set<Exsiccata> getExsiccataList1() {
        return exsiccataList1;
    }

    public void setExsiccataList1(Set<Exsiccata> exsiccataList1) {
        this.exsiccataList1 = exsiccataList1;
    }

    @XmlTransient
    public Set<Attachmentmetadata> getAttachmentmetadataList() {
        return attachmentmetadataList;
    }

    public void setAttachmentmetadataList(Set<Attachmentmetadata> attachmentmetadataList) {
        this.attachmentmetadataList = attachmentmetadataList;
    }

    @XmlTransient
    public Set<Attachmentmetadata> getAttachmentmetadataList1() {
        return attachmentmetadataList1;
    }

    public void setAttachmentmetadataList1(Set<Attachmentmetadata> attachmentmetadataList1) {
        this.attachmentmetadataList1 = attachmentmetadataList1;
    }

    @XmlTransient
    public Set<Loanreturnpreparation> getLoanreturnpreparationList() {
        return loanreturnpreparationList;
    }

    public void setLoanreturnpreparationList(Set<Loanreturnpreparation> loanreturnpreparationList) {
        this.loanreturnpreparationList = loanreturnpreparationList;
    }

    @XmlTransient
    public Set<Loanreturnpreparation> getLoanreturnpreparationList1() {
        return loanreturnpreparationList1;
    }

    public void setLoanreturnpreparationList1(Set<Loanreturnpreparation> loanreturnpreparationList1) {
        this.loanreturnpreparationList1 = loanreturnpreparationList1;
    }

    @XmlTransient
    public Set<Loanreturnpreparation> getLoanreturnpreparationList2() {
        return loanreturnpreparationList2;
    }

    public void setLoanreturnpreparationList2(Set<Loanreturnpreparation> loanreturnpreparationList2) {
        this.loanreturnpreparationList2 = loanreturnpreparationList2;
    }

    @XmlTransient
    public Set<Localitynamealias> getLocalitynamealiasList() {
        return localitynamealiasList;
    }

    public void setLocalitynamealiasList(Set<Localitynamealias> localitynamealiasList) {
        this.localitynamealiasList = localitynamealiasList;
    }

    @XmlTransient
    public Set<Localitynamealias> getLocalitynamealiasList1() {
        return localitynamealiasList1;
    }

    public void setLocalitynamealiasList1(Set<Localitynamealias> localitynamealiasList1) {
        this.localitynamealiasList1 = localitynamealiasList1;
    }

    @XmlTransient
    public Set<Recordset> getRecordsetList() {
        return recordsetList;
    }

    public void setRecordsetList(Set<Recordset> recordsetList) {
        this.recordsetList = recordsetList;
    }

    @XmlTransient
    public Set<Recordset> getRecordsetList1() {
        return recordsetList1;
    }

    public void setRecordsetList1(Set<Recordset> recordsetList1) {
        this.recordsetList1 = recordsetList1;
    }

    @XmlTransient
    public Set<Spappresource> getSpappresourceList() {
        return spappresourceList;
    }

    public void setSpappresourceList(Set<Spappresource> spappresourceList) {
        this.spappresourceList = spappresourceList;
    }

    @XmlTransient
    public Set<Spappresource> getSpappresourceList1() {
        return spappresourceList1;
    }

    public void setSpappresourceList1(Set<Spappresource> spappresourceList1) {
        this.spappresourceList1 = spappresourceList1;
    }

    @XmlTransient
    public Set<Taxonattachment> getTaxonattachmentList() {
        return taxonattachmentList;
    }

    public void setTaxonattachmentList(Set<Taxonattachment> taxonattachmentList) {
        this.taxonattachmentList = taxonattachmentList;
    }

    @XmlTransient
    public Set<Taxonattachment> getTaxonattachmentList1() {
        return taxonattachmentList1;
    }

    public void setTaxonattachmentList1(Set<Taxonattachment> taxonattachmentList1) {
        this.taxonattachmentList1 = taxonattachmentList1;
    }

    @XmlTransient
    public Set<Storagetreedefitem> getStoragetreedefitemList() {
        return storagetreedefitemList;
    }

    public void setStoragetreedefitemList(Set<Storagetreedefitem> storagetreedefitemList) {
        this.storagetreedefitemList = storagetreedefitemList;
    }

    @XmlTransient
    public Set<Storagetreedefitem> getStoragetreedefitemList1() {
        return storagetreedefitemList1;
    }

    public void setStoragetreedefitemList1(Set<Storagetreedefitem> storagetreedefitemList1) {
        this.storagetreedefitemList1 = storagetreedefitemList1;
    }

    @XmlTransient
    public Set<Determinationcitation> getDeterminationcitationList() {
        return determinationcitationList;
    }

    public void setDeterminationcitationList(Set<Determinationcitation> determinationcitationList) {
        this.determinationcitationList = determinationcitationList;
    }

    @XmlTransient
    public Set<Determinationcitation> getDeterminationcitationList1() {
        return determinationcitationList1;
    }

    public void setDeterminationcitationList1(Set<Determinationcitation> determinationcitationList1) {
        this.determinationcitationList1 = determinationcitationList1;
    }

    @XmlTransient
    public Set<Conserveventattachment> getConserveventattachmentList() {
        return conserveventattachmentList;
    }

    public void setConserveventattachmentList(Set<Conserveventattachment> conserveventattachmentList) {
        this.conserveventattachmentList = conserveventattachmentList;
    }

    @XmlTransient
    public Set<Conserveventattachment> getConserveventattachmentList1() {
        return conserveventattachmentList1;
    }

    public void setConserveventattachmentList1(Set<Conserveventattachment> conserveventattachmentList1) {
        this.conserveventattachmentList1 = conserveventattachmentList1;
    }

    @XmlTransient
    public Set<Lithostrattreedefitem> getLithostrattreedefitemList() {
        return lithostrattreedefitemList;
    }

    public void setLithostrattreedefitemList(Set<Lithostrattreedefitem> lithostrattreedefitemList) {
        this.lithostrattreedefitemList = lithostrattreedefitemList;
    }

    @XmlTransient
    public Set<Lithostrattreedefitem> getLithostrattreedefitemList1() {
        return lithostrattreedefitemList1;
    }

    public void setLithostrattreedefitemList1(Set<Lithostrattreedefitem> lithostrattreedefitemList1) {
        this.lithostrattreedefitemList1 = lithostrattreedefitemList1;
    }

    @XmlTransient
    public Set<Journal> getJournalList() {
        return journalList;
    }

    public void setJournalList(Set<Journal> journalList) {
        this.journalList = journalList;
    }

    @XmlTransient
    public Set<Journal> getJournalList1() {
        return journalList1;
    }

    public void setJournalList1(Set<Journal> journalList1) {
        this.journalList1 = journalList1;
    }

    @XmlTransient
    public Set<Datatype> getDatatypeList() {
        return datatypeList;
    }

    public void setDatatypeList(Set<Datatype> datatypeList) {
        this.datatypeList = datatypeList;
    }

    @XmlTransient
    public Set<Datatype> getDatatypeList1() {
        return datatypeList1;
    }

    public void setDatatypeList1(Set<Datatype> datatypeList1) {
        this.datatypeList1 = datatypeList1;
    }

    @XmlTransient
    public Set<Collectionreltype> getCollectionreltypeList() {
        return collectionreltypeList;
    }

    public void setCollectionreltypeList(Set<Collectionreltype> collectionreltypeList) {
        this.collectionreltypeList = collectionreltypeList;
    }

    @XmlTransient
    public Set<Collectionreltype> getCollectionreltypeList1() {
        return collectionreltypeList1;
    }

    public void setCollectionreltypeList1(Set<Collectionreltype> collectionreltypeList1) {
        this.collectionreltypeList1 = collectionreltypeList1;
    }

    @XmlTransient
    public Set<Taxon> getTaxonList() {
        return taxonList;
    }

    public void setTaxonList(Set<Taxon> taxonList) {
        this.taxonList = taxonList;
    }

    @XmlTransient
    public Set<Taxon> getTaxonList1() {
        return taxonList1;
    }

    public void setTaxonList1(Set<Taxon> taxonList1) {
        this.taxonList1 = taxonList1;
    }

    @XmlTransient
    public Set<Localityattachment> getLocalityattachmentList() {
        return localityattachmentList;
    }

    public void setLocalityattachmentList(Set<Localityattachment> localityattachmentList) {
        this.localityattachmentList = localityattachmentList;
    }

    @XmlTransient
    public Set<Localityattachment> getLocalityattachmentList1() {
        return localityattachmentList1;
    }

    public void setLocalityattachmentList1(Set<Localityattachment> localityattachmentList1) {
        this.localityattachmentList1 = localityattachmentList1;
    }

    @XmlTransient
    public Set<Treatmentevent> getTreatmenteventList() {
        return treatmenteventList;
    }

    public void setTreatmenteventList(Set<Treatmentevent> treatmenteventList) {
        this.treatmenteventList = treatmenteventList;
    }

    @XmlTransient
    public Set<Treatmentevent> getTreatmenteventList1() {
        return treatmenteventList1;
    }

    public void setTreatmenteventList1(Set<Treatmentevent> treatmenteventList1) {
        this.treatmenteventList1 = treatmenteventList1;
    }

    @XmlTransient
    public Set<Storagetreedef> getStoragetreedefList() {
        return storagetreedefList;
    }

    public void setStoragetreedefList(Set<Storagetreedef> storagetreedefList) {
        this.storagetreedefList = storagetreedefList;
    }

    @XmlTransient
    public Set<Storagetreedef> getStoragetreedefList1() {
        return storagetreedefList1;
    }

    public void setStoragetreedefList1(Set<Storagetreedef> storagetreedefList1) {
        this.storagetreedefList1 = storagetreedefList1;
    }

    @XmlTransient
    public Set<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(Set<Address> addressList) {
        this.addressList = addressList;
    }

    @XmlTransient
    public Set<Address> getAddressList1() {
        return addressList1;
    }

    public void setAddressList1(Set<Address> addressList1) {
        this.addressList1 = addressList1;
    }

    @XmlTransient
    public Set<Address> getAddressList2() {
        return addressList2;
    }

    public void setAddressList2(Set<Address> addressList2) {
        this.addressList2 = addressList2;
    }

    @XmlTransient
    public Set<Spexportschemaitem> getSpexportschemaitemList() {
        return spexportschemaitemList;
    }

    public void setSpexportschemaitemList(Set<Spexportschemaitem> spexportschemaitemList) {
        this.spexportschemaitemList = spexportschemaitemList;
    }

    @XmlTransient
    public Set<Spexportschemaitem> getSpexportschemaitemList1() {
        return spexportschemaitemList1;
    }

    public void setSpexportschemaitemList1(Set<Spexportschemaitem> spexportschemaitemList1) {
        this.spexportschemaitemList1 = spexportschemaitemList1;
    }

    @XmlTransient
    public Set<Splocaleitemstr> getSplocaleitemstrList() {
        return splocaleitemstrList;
    }

    public void setSplocaleitemstrList(Set<Splocaleitemstr> splocaleitemstrList) {
        this.splocaleitemstrList = splocaleitemstrList;
    }

    @XmlTransient
    public Set<Splocaleitemstr> getSplocaleitemstrList1() {
        return splocaleitemstrList1;
    }

    public void setSplocaleitemstrList1(Set<Splocaleitemstr> splocaleitemstrList1) {
        this.splocaleitemstrList1 = splocaleitemstrList1;
    }

    @XmlTransient
    public Set<Workbenchtemplatemappingitem> getWorkbenchtemplatemappingitemList() {
        return workbenchtemplatemappingitemList;
    }

    public void setWorkbenchtemplatemappingitemList(Set<Workbenchtemplatemappingitem> workbenchtemplatemappingitemList) {
        this.workbenchtemplatemappingitemList = workbenchtemplatemappingitemList;
    }

    @XmlTransient
    public Set<Workbenchtemplatemappingitem> getWorkbenchtemplatemappingitemList1() {
        return workbenchtemplatemappingitemList1;
    }

    public void setWorkbenchtemplatemappingitemList1(Set<Workbenchtemplatemappingitem> workbenchtemplatemappingitemList1) {
        this.workbenchtemplatemappingitemList1 = workbenchtemplatemappingitemList1;
    }

    @XmlTransient
    public Set<Lithostrattreedef> getLithostrattreedefList() {
        return lithostrattreedefList;
    }

    public void setLithostrattreedefList(Set<Lithostrattreedef> lithostrattreedefList) {
        this.lithostrattreedefList = lithostrattreedefList;
    }

    @XmlTransient
    public Set<Lithostrattreedef> getLithostrattreedefList1() {
        return lithostrattreedefList1;
    }

    public void setLithostrattreedefList1(Set<Lithostrattreedef> lithostrattreedefList1) {
        this.lithostrattreedefList1 = lithostrattreedefList1;
    }

    @XmlTransient
    public Set<Autonumberingscheme> getAutonumberingschemeList() {
        return autonumberingschemeList;
    }

    public void setAutonumberingschemeList(Set<Autonumberingscheme> autonumberingschemeList) {
        this.autonumberingschemeList = autonumberingschemeList;
    }

    @XmlTransient
    public Set<Autonumberingscheme> getAutonumberingschemeList1() {
        return autonumberingschemeList1;
    }

    public void setAutonumberingschemeList1(Set<Autonumberingscheme> autonumberingschemeList1) {
        this.autonumberingschemeList1 = autonumberingschemeList1;
    }

    @XmlTransient
    public Set<Conservdescriptionattachment> getConservdescriptionattachmentList() {
        return conservdescriptionattachmentList;
    }

    public void setConservdescriptionattachmentList(Set<Conservdescriptionattachment> conservdescriptionattachmentList) {
        this.conservdescriptionattachmentList = conservdescriptionattachmentList;
    }

    @XmlTransient
    public Set<Conservdescriptionattachment> getConservdescriptionattachmentList1() {
        return conservdescriptionattachmentList1;
    }

    public void setConservdescriptionattachmentList1(Set<Conservdescriptionattachment> conservdescriptionattachmentList1) {
        this.conservdescriptionattachmentList1 = conservdescriptionattachmentList1;
    }

    @XmlTransient
    public Set<Collectingeventattribute> getCollectingeventattributeList() {
        return collectingeventattributeList;
    }

    public void setCollectingeventattributeList(Set<Collectingeventattribute> collectingeventattributeList) {
        this.collectingeventattributeList = collectingeventattributeList;
    }

    @XmlTransient
    public Set<Collectingeventattribute> getCollectingeventattributeList1() {
        return collectingeventattributeList1;
    }

    public void setCollectingeventattributeList1(Set<Collectingeventattribute> collectingeventattributeList1) {
        this.collectingeventattributeList1 = collectingeventattributeList1;
    }

    @XmlTransient
    public Set<Spquery> getSpqueryList() {
        return spqueryList;
    }

    public void setSpqueryList(Set<Spquery> spqueryList) {
        this.spqueryList = spqueryList;
    }

    @XmlTransient
    public Set<Spquery> getSpqueryList1() {
        return spqueryList1;
    }

    public void setSpqueryList1(Set<Spquery> spqueryList1) {
        this.spqueryList1 = spqueryList1;
    }

    @XmlTransient
    public Set<Geographytreedef> getGeographytreedefList() {
        return geographytreedefList;
    }

    public void setGeographytreedefList(Set<Geographytreedef> geographytreedefList) {
        this.geographytreedefList = geographytreedefList;
    }

    @XmlTransient
    public Set<Geographytreedef> getGeographytreedefList1() {
        return geographytreedefList1;
    }

    public void setGeographytreedefList1(Set<Geographytreedef> geographytreedefList1) {
        this.geographytreedefList1 = geographytreedefList1;
    }

    @XmlTransient
    public Set<Addressofrecord> getAddressofrecordList() {
        return addressofrecordList;
    }

    public void setAddressofrecordList(Set<Addressofrecord> addressofrecordList) {
        this.addressofrecordList = addressofrecordList;
    }

    @XmlTransient
    public Set<Addressofrecord> getAddressofrecordList1() {
        return addressofrecordList1;
    }

    public void setAddressofrecordList1(Set<Addressofrecord> addressofrecordList1) {
        this.addressofrecordList1 = addressofrecordList1;
    }

    @XmlTransient
    public Set<Addressofrecord> getAddressofrecordList2() {
        return addressofrecordList2;
    }

    public void setAddressofrecordList2(Set<Addressofrecord> addressofrecordList2) {
        this.addressofrecordList2 = addressofrecordList2;
    }

    @XmlTransient
    public Set<Deaccessionagent> getDeaccessionagentList() {
        return deaccessionagentList;
    }

    public void setDeaccessionagentList(Set<Deaccessionagent> deaccessionagentList) {
        this.deaccessionagentList = deaccessionagentList;
    }

    @XmlTransient
    public Set<Deaccessionagent> getDeaccessionagentList1() {
        return deaccessionagentList1;
    }

    public void setDeaccessionagentList1(Set<Deaccessionagent> deaccessionagentList1) {
        this.deaccessionagentList1 = deaccessionagentList1;
    }

    @XmlTransient
    public Set<Deaccessionagent> getDeaccessionagentList2() {
        return deaccessionagentList2;
    }

    public void setDeaccessionagentList2(Set<Deaccessionagent> deaccessionagentList2) {
        this.deaccessionagentList2 = deaccessionagentList2;
    }

    @XmlTransient
    public Set<Borrow> getBorrowList() {
        return borrowList;
    }

    public void setBorrowList(Set<Borrow> borrowList) {
        this.borrowList = borrowList;
    }

    @XmlTransient
    public Set<Borrow> getBorrowList1() {
        return borrowList1;
    }

    public void setBorrowList1(Set<Borrow> borrowList1) {
        this.borrowList1 = borrowList1;
    }

    @XmlTransient
    public Set<Localitydetail> getLocalitydetailList() {
        return localitydetailList;
    }

    public void setLocalitydetailList(Set<Localitydetail> localitydetailList) {
        this.localitydetailList = localitydetailList;
    }

    @XmlTransient
    public Set<Localitydetail> getLocalitydetailList1() {
        return localitydetailList1;
    }

    public void setLocalitydetailList1(Set<Localitydetail> localitydetailList1) {
        this.localitydetailList1 = localitydetailList1;
    }

    @XmlTransient
    public Set<Commonnametxcitation> getCommonnametxcitationList() {
        return commonnametxcitationList;
    }

    public void setCommonnametxcitationList(Set<Commonnametxcitation> commonnametxcitationList) {
        this.commonnametxcitationList = commonnametxcitationList;
    }

    @XmlTransient
    public Set<Commonnametxcitation> getCommonnametxcitationList1() {
        return commonnametxcitationList1;
    }

    public void setCommonnametxcitationList1(Set<Commonnametxcitation> commonnametxcitationList1) {
        this.commonnametxcitationList1 = commonnametxcitationList1;
    }

    @XmlTransient
    public Set<Spqueryfield> getSpqueryfieldList() {
        return spqueryfieldList;
    }

    public void setSpqueryfieldList(Set<Spqueryfield> spqueryfieldList) {
        this.spqueryfieldList = spqueryfieldList;
    }

    @XmlTransient
    public Set<Spqueryfield> getSpqueryfieldList1() {
        return spqueryfieldList1;
    }

    public void setSpqueryfieldList1(Set<Spqueryfield> spqueryfieldList1) {
        this.spqueryfieldList1 = spqueryfieldList1;
    }

    @XmlTransient
    public Set<Groupperson> getGrouppersonList() {
        return grouppersonList;
    }

    public void setGrouppersonList(Set<Groupperson> grouppersonList) {
        this.grouppersonList = grouppersonList;
    }

    @XmlTransient
    public Set<Groupperson> getGrouppersonList1() {
        return grouppersonList1;
    }

    public void setGrouppersonList1(Set<Groupperson> grouppersonList1) {
        this.grouppersonList1 = grouppersonList1;
    }

    @XmlTransient
    public Set<Groupperson> getGrouppersonList2() {
        return grouppersonList2;
    }

    public void setGrouppersonList2(Set<Groupperson> grouppersonList2) {
        this.grouppersonList2 = grouppersonList2;
    }

    @XmlTransient
    public Set<Groupperson> getGrouppersonList3() {
        return grouppersonList3;
    }

    public void setGrouppersonList3(Set<Groupperson> grouppersonList3) {
        this.grouppersonList3 = grouppersonList3;
    }

    @XmlTransient
    public Set<Attributedef> getAttributedefList() {
        return attributedefList;
    }

    public void setAttributedefList(Set<Attributedef> attributedefList) {
        this.attributedefList = attributedefList;
    }

    @XmlTransient
    public Set<Attributedef> getAttributedefList1() {
        return attributedefList1;
    }

    public void setAttributedefList1(Set<Attributedef> attributedefList1) {
        this.attributedefList1 = attributedefList1;
    }

    @XmlTransient
    public Collection getCollectionCCID() {
        return collectionCCID;
    }

    public void setCollectionCCID(Collection collectionCCID) {
        this.collectionCCID = collectionCCID;
    }

    @XmlTransient
    public Collection getCollectionTCID() {
        return collectionTCID;
    }

    public void setCollectionTCID(Collection collectionTCID) {
        this.collectionTCID = collectionTCID;
    }

    @XmlTransient
    public Specifyuser getSpecifyUserID() {
        return specifyUserID;
    }

    public void setSpecifyUserID(Specifyuser specifyUserID) {
        this.specifyUserID = specifyUserID;
    }

    @XmlTransient
    public Set<Agent> getAgentList() {
        return agentList;
    }

    public void setAgentList(Set<Agent> agentList) {
        this.agentList = agentList;
    }

    @XmlIDREF
    public Agent getModifiedByAgentID() {
        return modifiedByAgentID;
    }

    public void setModifiedByAgentID(Agent modifiedByAgentID) {
        this.modifiedByAgentID = modifiedByAgentID;
    }

    @XmlTransient
    public Set<Agent> getAgentList1() {
        return agentList1;
    }

    public void setAgentList1(Set<Agent> agentList1) {
        this.agentList1 = agentList1;
    }

    @XmlIDREF
    public Agent getCreatedByAgentID() {
        return createdByAgentID;
    }

    public void setCreatedByAgentID(Agent createdByAgentID) {
        this.createdByAgentID = createdByAgentID;
    }

    @XmlTransient
    public Set<Agent> getAgentList2() {
        return agentList2;
    }

    public void setAgentList2(Set<Agent> agentList2) {
        this.agentList2 = agentList2;
    }

    @XmlTransient
//    @JsonIgnore
    public Agent getParentOrganizationID() {
        return parentOrganizationID;
    }

    public void setParentOrganizationID(Agent parentOrganizationID) {
        this.parentOrganizationID = parentOrganizationID;
    }

    public Institution getInstitutionCCID() {
        return institutionCCID;
    }

    public void setInstitutionCCID(Institution institutionCCID) {
        this.institutionCCID = institutionCCID;
    }

    @XmlTransient
    public Institution getInstitutionTCID() {
        return institutionTCID;
    }

    public void setInstitutionTCID(Institution institutionTCID) {
        this.institutionTCID = institutionTCID;
    }
 
    public Division getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(Division divisionID) {
        this.divisionID = divisionID;
    }

    @XmlTransient
    public Set<Collectionobject> getCollectionobjectList() {
        return collectionobjectList;
    }

    public void setCollectionobjectList(Set<Collectionobject> collectionobjectList) {
        this.collectionobjectList = collectionobjectList;
    }

    @XmlTransient
    public Set<Collectionobject> getCollectionobjectList1() {
        return collectionobjectList1;
    }

    public void setCollectionobjectList1(Set<Collectionobject> collectionobjectList1) {
        this.collectionobjectList1 = collectionobjectList1;
    }

    @XmlTransient
    public Set<Collectionobject> getCollectionobjectList2() {
        return collectionobjectList2;
    }

    public void setCollectionobjectList2(Set<Collectionobject> collectionobjectList2) {
        this.collectionobjectList2 = collectionobjectList2;
    }

    @XmlTransient
    public Set<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(Set<Project> projectList) {
        this.projectList = projectList;
    }

    @XmlTransient
    public Set<Project> getProjectList1() {
        return projectList1;
    }

    public void setProjectList1(Set<Project> projectList1) {
        this.projectList1 = projectList1;
    }

    @XmlTransient
    public Set<Project> getProjectList2() {
        return projectList2;
    }

    public void setProjectList2(Set<Project> projectList2) {
        this.projectList2 = projectList2;
    }

    @XmlTransient
    public Set<Splocalecontaineritem> getSplocalecontaineritemList() {
        return splocalecontaineritemList;
    }

    public void setSplocalecontaineritemList(Set<Splocalecontaineritem> splocalecontaineritemList) {
        this.splocalecontaineritemList = splocalecontaineritemList;
    }

    @XmlTransient
    public Set<Splocalecontaineritem> getSplocalecontaineritemList1() {
        return splocalecontaineritemList1;
    }

    public void setSplocalecontaineritemList1(Set<Splocalecontaineritem> splocalecontaineritemList1) {
        this.splocalecontaineritemList1 = splocalecontaineritemList1;
    }

    @XmlTransient
    public Set<Institution> getInstitutionList() {
        return institutionList;
    }

    public void setInstitutionList(Set<Institution> institutionList) {
        this.institutionList = institutionList;
    }

    @XmlTransient
    public Set<Institution> getInstitutionList1() {
        return institutionList1;
    }

    public void setInstitutionList1(Set<Institution> institutionList1) {
        this.institutionList1 = institutionList1;
    }

    @XmlTransient
    public Set<Collectingevent> getCollectingeventList() {
        return collectingeventList;
    }

    public void setCollectingeventList(Set<Collectingevent> collectingeventList) {
        this.collectingeventList = collectingeventList;
    }

    @XmlTransient
    public Set<Collectingevent> getCollectingeventList1() {
        return collectingeventList1;
    }

    public void setCollectingeventList1(Set<Collectingevent> collectingeventList1) {
        this.collectingeventList1 = collectingeventList1;
    }

    @XmlTransient
    public Set<Fieldnotebook> getFieldnotebookList() {
        return fieldnotebookList;
    }

    public void setFieldnotebookList(Set<Fieldnotebook> fieldnotebookList) {
        this.fieldnotebookList = fieldnotebookList;
    }

    @XmlTransient
    public Set<Fieldnotebook> getFieldnotebookList1() {
        return fieldnotebookList1;
    }

    public void setFieldnotebookList1(Set<Fieldnotebook> fieldnotebookList1) {
        this.fieldnotebookList1 = fieldnotebookList1;
    }

    @XmlTransient
    public Set<Fieldnotebook> getFieldnotebookList2() {
        return fieldnotebookList2;
    }

    public void setFieldnotebookList2(Set<Fieldnotebook> fieldnotebookList2) {
        this.fieldnotebookList2 = fieldnotebookList2;
    }

    @XmlTransient
    public Set<Geologictimeperiod> getGeologictimeperiodList() {
        return geologictimeperiodList;
    }

    public void setGeologictimeperiodList(Set<Geologictimeperiod> geologictimeperiodList) {
        this.geologictimeperiodList = geologictimeperiodList;
    }

    @XmlTransient
    public Set<Geologictimeperiod> getGeologictimeperiodList1() {
        return geologictimeperiodList1;
    }

    public void setGeologictimeperiodList1(Set<Geologictimeperiod> geologictimeperiodList1) {
        this.geologictimeperiodList1 = geologictimeperiodList1;
    }

    @XmlTransient
    public Set<Lithostrat> getLithostratList() {
        return lithostratList;
    }

    public void setLithostratList(Set<Lithostrat> lithostratList) {
        this.lithostratList = lithostratList;
    }

    @XmlTransient
    public Set<Lithostrat> getLithostratList1() {
        return lithostratList1;
    }

    public void setLithostratList1(Set<Lithostrat> lithostratList1) {
        this.lithostratList1 = lithostratList1;
    }

    @XmlTransient
    public Set<Taxoncitation> getTaxoncitationList() {
        return taxoncitationList;
    }

    public void setTaxoncitationList(Set<Taxoncitation> taxoncitationList) {
        this.taxoncitationList = taxoncitationList;
    }

    @XmlTransient
    public Set<Taxoncitation> getTaxoncitationList1() {
        return taxoncitationList1;
    }

    public void setTaxoncitationList1(Set<Taxoncitation> taxoncitationList1) {
        this.taxoncitationList1 = taxoncitationList1;
    }

    @XmlTransient
    public Set<Dnasequencingruncitation> getDnasequencingruncitationList() {
        return dnasequencingruncitationList;
    }

    public void setDnasequencingruncitationList(Set<Dnasequencingruncitation> dnasequencingruncitationList) {
        this.dnasequencingruncitationList = dnasequencingruncitationList;
    }

    @XmlTransient
    public Set<Dnasequencingruncitation> getDnasequencingruncitationList1() {
        return dnasequencingruncitationList1;
    }

    public void setDnasequencingruncitationList1(Set<Dnasequencingruncitation> dnasequencingruncitationList1) {
        this.dnasequencingruncitationList1 = dnasequencingruncitationList1;
    }

    @XmlTransient
    public Set<Dnasequence> getDnasequenceList() {
        return dnasequenceList;
    }

    public void setDnasequenceList(Set<Dnasequence> dnasequenceList) {
        this.dnasequenceList = dnasequenceList;
    }

    @XmlTransient
    public Set<Dnasequence> getDnasequenceList1() {
        return dnasequenceList1;
    }

    public void setDnasequenceList1(Set<Dnasequence> dnasequenceList1) {
        this.dnasequenceList1 = dnasequenceList1;
    }

    @XmlTransient
    public Set<Dnasequence> getDnasequenceList2() {
        return dnasequenceList2;
    }

    public void setDnasequenceList2(Set<Dnasequence> dnasequenceList2) {
        this.dnasequenceList2 = dnasequenceList2;
    }

    @XmlTransient
    public Set<Preparationattr> getPreparationattrList() {
        return preparationattrList;
    }

    public void setPreparationattrList(Set<Preparationattr> preparationattrList) {
        this.preparationattrList = preparationattrList;
    }

    @XmlTransient
    public Set<Preparationattr> getPreparationattrList1() {
        return preparationattrList1;
    }

    public void setPreparationattrList1(Set<Preparationattr> preparationattrList1) {
        this.preparationattrList1 = preparationattrList1;
    }

    @XmlTransient
    public Set<Borrowreturnmaterial> getBorrowreturnmaterialList() {
        return borrowreturnmaterialList;
    }

    public void setBorrowreturnmaterialList(Set<Borrowreturnmaterial> borrowreturnmaterialList) {
        this.borrowreturnmaterialList = borrowreturnmaterialList;
    }

    @XmlTransient
    public Set<Borrowreturnmaterial> getBorrowreturnmaterialList1() {
        return borrowreturnmaterialList1;
    }

    public void setBorrowreturnmaterialList1(Set<Borrowreturnmaterial> borrowreturnmaterialList1) {
        this.borrowreturnmaterialList1 = borrowreturnmaterialList1;
    }

    @XmlTransient
    public Set<Borrowreturnmaterial> getBorrowreturnmaterialList2() {
        return borrowreturnmaterialList2;
    }

    public void setBorrowreturnmaterialList2(Set<Borrowreturnmaterial> borrowreturnmaterialList2) {
        this.borrowreturnmaterialList2 = borrowreturnmaterialList2;
    }

    @XmlTransient
    public Set<Workbench> getWorkbenchList() {
        return workbenchList;
    }

    public void setWorkbenchList(Set<Workbench> workbenchList) {
        this.workbenchList = workbenchList;
    }

    @XmlTransient
    public Set<Workbench> getWorkbenchList1() {
        return workbenchList1;
    }

    public void setWorkbenchList1(Set<Workbench> workbenchList1) {
        this.workbenchList1 = workbenchList1;
    }

    @XmlTransient
    public Set<Spauditlogfield> getSpauditlogfieldList() {
        return spauditlogfieldList;
    }

    public void setSpauditlogfieldList(Set<Spauditlogfield> spauditlogfieldList) {
        this.spauditlogfieldList = spauditlogfieldList;
    }

    @XmlTransient
    public Set<Spauditlogfield> getSpauditlogfieldList1() {
        return spauditlogfieldList1;
    }
 
    public void setSpauditlogfieldList1(Set<Spauditlogfield> spauditlogfieldList1) {
        this.spauditlogfieldList1 = spauditlogfieldList1;
    }

    @XmlTransient
    public Set<Referencework> getReferenceworkList() {
        return referenceworkList;
    }

    public void setReferenceworkList(Set<Referencework> referenceworkList) {
        this.referenceworkList = referenceworkList;
    }

    @XmlTransient
    public Set<Referencework> getReferenceworkList1() {
        return referenceworkList1;
    }

    public void setReferenceworkList1(Set<Referencework> referenceworkList1) {
        this.referenceworkList1 = referenceworkList1;
    }

    @XmlTransient
    public Set<Shipment> getShipmentList() {
        return shipmentList;
    }

    public void setShipmentList(Set<Shipment> shipmentList) {
        this.shipmentList = shipmentList;
    }

    @XmlTransient
    public Set<Shipment> getShipmentList1() {
        return shipmentList1;
    }

    public void setShipmentList1(Set<Shipment> shipmentList1) {
        this.shipmentList1 = shipmentList1;
    }

    @XmlTransient
    public Set<Shipment> getShipmentList2() {
        return shipmentList2;
    }

    public void setShipmentList2(Set<Shipment> shipmentList2) {
        this.shipmentList2 = shipmentList2;
    }

    @XmlTransient
    public Set<Shipment> getShipmentList3() {
        return shipmentList3;
    }

    public void setShipmentList3(Set<Shipment> shipmentList3) {
        this.shipmentList3 = shipmentList3;
    }

    @XmlTransient
    public Set<Shipment> getShipmentList4() {
        return shipmentList4;
    }

    public void setShipmentList4(Set<Shipment> shipmentList4) {
        this.shipmentList4 = shipmentList4;
    }

    @XmlTransient
    public Set<Repositoryagreement> getRepositoryagreementList() {
        return repositoryagreementList;
    }

    public void setRepositoryagreementList(Set<Repositoryagreement> repositoryagreementList) {
        this.repositoryagreementList = repositoryagreementList;
    }

    @XmlTransient
    public Set<Repositoryagreement> getRepositoryagreementList1() {
        return repositoryagreementList1;
    }

    public void setRepositoryagreementList1(Set<Repositoryagreement> repositoryagreementList1) {
        this.repositoryagreementList1 = repositoryagreementList1;
    }

    @XmlTransient
    public Set<Repositoryagreement> getRepositoryagreementList2() {
        return repositoryagreementList2;
    }

    public void setRepositoryagreementList2(Set<Repositoryagreement> repositoryagreementList2) {
        this.repositoryagreementList2 = repositoryagreementList2;
    }

    @XmlTransient
    public Set<Splocalecontainer> getSplocalecontainerList() {
        return splocalecontainerList;
    }

    public void setSplocalecontainerList(Set<Splocalecontainer> splocalecontainerList) {
        this.splocalecontainerList = splocalecontainerList;
    }

    @XmlTransient
    public Set<Splocalecontainer> getSplocalecontainerList1() {
        return splocalecontainerList1;
    }

    public void setSplocalecontainerList1(Set<Splocalecontainer> splocalecontainerList1) {
        this.splocalecontainerList1 = splocalecontainerList1;
    }

    @XmlTransient
    public Set<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(Set<Author> authorList) {
        this.authorList = authorList;
    }

    @XmlTransient
    public Set<Author> getAuthorList1() {
        return authorList1;
    }

    public void setAuthorList1(Set<Author> authorList1) {
        this.authorList1 = authorList1;
    }

    @XmlTransient
    public Set<Author> getAuthorList2() {
        return authorList2;
    }

    public void setAuthorList2(Set<Author> authorList2) {
        this.authorList2 = authorList2;
    }

    @XmlTransient
    public Set<Accessionagent> getAccessionagentList() {
        return accessionagentList;
    }

    public void setAccessionagentList(Set<Accessionagent> accessionagentList) {
        this.accessionagentList = accessionagentList;
    }

    @XmlTransient
    public Set<Accessionagent> getAccessionagentList1() {
        return accessionagentList1;
    }

    public void setAccessionagentList1(Set<Accessionagent> accessionagentList1) {
        this.accessionagentList1 = accessionagentList1;
    }

    @XmlTransient
    public Set<Accessionagent> getAccessionagentList2() {
        return accessionagentList2;
    }

    public void setAccessionagentList2(Set<Accessionagent> accessionagentList2) {
        this.accessionagentList2 = accessionagentList2;
    }

    @XmlTransient
    public Set<Workbenchtemplate> getWorkbenchtemplateList() {
        return workbenchtemplateList;
    }

    public void setWorkbenchtemplateList(Set<Workbenchtemplate> workbenchtemplateList) {
        this.workbenchtemplateList = workbenchtemplateList;
    }

    @XmlTransient
    public Set<Workbenchtemplate> getWorkbenchtemplateList1() {
        return workbenchtemplateList1;
    }

    public void setWorkbenchtemplateList1(Set<Workbenchtemplate> workbenchtemplateList1) {
        this.workbenchtemplateList1 = workbenchtemplateList1;
    }

    @XmlTransient
    public Set<Taxontreedefitem> getTaxontreedefitemList() {
        return taxontreedefitemList;
    }

    public void setTaxontreedefitemList(Set<Taxontreedefitem> taxontreedefitemList) {
        this.taxontreedefitemList = taxontreedefitemList;
    }

    @XmlTransient
    public Set<Taxontreedefitem> getTaxontreedefitemList1() {
        return taxontreedefitemList1;
    }

    public void setTaxontreedefitemList1(Set<Taxontreedefitem> taxontreedefitemList1) {
        this.taxontreedefitemList1 = taxontreedefitemList1;
    }

    @XmlTransient
    public Set<Conservevent> getConserveventList() {
        return conserveventList;
    }

    public void setConserveventList(Set<Conservevent> conserveventList) {
        this.conserveventList = conserveventList;
    }

    @XmlTransient
    public Set<Conservevent> getConserveventList1() {
        return conserveventList1;
    }

    public void setConserveventList1(Set<Conservevent> conserveventList1) {
        this.conserveventList1 = conserveventList1;
    }

    @XmlTransient
    public Set<Conservevent> getConserveventList2() {
        return conserveventList2;
    }

    public void setConserveventList2(Set<Conservevent> conserveventList2) {
        this.conserveventList2 = conserveventList2;
    }

    @XmlTransient
    public Set<Conservevent> getConserveventList3() {
        return conserveventList3;
    }

    public void setConserveventList3(Set<Conservevent> conserveventList3) {
        this.conserveventList3 = conserveventList3;
    }

    @XmlTransient
    public Set<Conservevent> getConserveventList4() {
        return conserveventList4;
    }

    public void setConserveventList4(Set<Conservevent> conserveventList4) {
        this.conserveventList4 = conserveventList4;
    }

    @XmlTransient
    public Set<Collection> getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(Set<Collection> collectionList) {
        this.collectionList = collectionList;
    }

    @XmlTransient
    public Set<Collection> getCollectionList1() {
        return collectionList1;
    }

    public void setCollectionList1(Set<Collection> collectionList1) {
        this.collectionList1 = collectionList1;
    }

    @XmlTransient
    public Set<Fieldnotebookpageattachment> getFieldnotebookpageattachmentList() {
        return fieldnotebookpageattachmentList;
    }

    public void setFieldnotebookpageattachmentList(Set<Fieldnotebookpageattachment> fieldnotebookpageattachmentList) {
        this.fieldnotebookpageattachmentList = fieldnotebookpageattachmentList;
    }

    @XmlTransient
    public Set<Fieldnotebookpageattachment> getFieldnotebookpageattachmentList1() {
        return fieldnotebookpageattachmentList1;
    }

    public void setFieldnotebookpageattachmentList1(Set<Fieldnotebookpageattachment> fieldnotebookpageattachmentList1) {
        this.fieldnotebookpageattachmentList1 = fieldnotebookpageattachmentList1;
    }

    @XmlTransient
    public Set<Spprincipal> getSpprincipalList() {
        return spprincipalList;
    }

    public void setSpprincipalList(Set<Spprincipal> spprincipalList) {
        this.spprincipalList = spprincipalList;
    }

    @XmlTransient
    public Set<Spprincipal> getSpprincipalList1() {
        return spprincipalList1;
    }

    public void setSpprincipalList1(Set<Spprincipal> spprincipalList1) {
        this.spprincipalList1 = spprincipalList1;
    }

    @XmlTransient
    public Set<Fieldnotebookattachment> getFieldnotebookattachmentList() {
        return fieldnotebookattachmentList;
    }

    public void setFieldnotebookattachmentList(Set<Fieldnotebookattachment> fieldnotebookattachmentList) {
        this.fieldnotebookattachmentList = fieldnotebookattachmentList;
    }

    @XmlTransient
    public Set<Fieldnotebookattachment> getFieldnotebookattachmentList1() {
        return fieldnotebookattachmentList1;
    }

    public void setFieldnotebookattachmentList1(Set<Fieldnotebookattachment> fieldnotebookattachmentList1) {
        this.fieldnotebookattachmentList1 = fieldnotebookattachmentList1;
    }

    @XmlTransient
    public Set<Preparationattribute> getPreparationattributeList() {
        return preparationattributeList;
    }

    public void setPreparationattributeList(Set<Preparationattribute> preparationattributeList) {
        this.preparationattributeList = preparationattributeList;
    }

    @XmlTransient
    public Set<Preparationattribute> getPreparationattributeList1() {
        return preparationattributeList1;
    }

    public void setPreparationattributeList1(Set<Preparationattribute> preparationattributeList1) {
        this.preparationattributeList1 = preparationattributeList1;
    }

    @XmlTransient
    public Set<Inforequest> getInforequestList() {
        return inforequestList;
    }

    public void setInforequestList(Set<Inforequest> inforequestList) {
        this.inforequestList = inforequestList;
    }

    @XmlTransient
    public Set<Inforequest> getInforequestList1() {
        return inforequestList1;
    }

    public void setInforequestList1(Set<Inforequest> inforequestList1) {
        this.inforequestList1 = inforequestList1;
    }

    @XmlTransient
    public Set<Inforequest> getInforequestList2() {
        return inforequestList2;
    }

    public void setInforequestList2(Set<Inforequest> inforequestList2) {
        this.inforequestList2 = inforequestList2;
    }

    @XmlTransient
    public Set<Collectingeventattr> getCollectingeventattrList() {
        return collectingeventattrList;
    }

    public void setCollectingeventattrList(Set<Collectingeventattr> collectingeventattrList) {
        this.collectingeventattrList = collectingeventattrList;
    }

    @XmlTransient
    public Set<Collectingeventattr> getCollectingeventattrList1() {
        return collectingeventattrList1;
    }

    public void setCollectingeventattrList1(Set<Collectingeventattr> collectingeventattrList1) {
        this.collectingeventattrList1 = collectingeventattrList1;
    }

    @XmlTransient
    public Set<Spappresourcedir> getSpappresourcedirList() {
        return spappresourcedirList;
    }

    public void setSpappresourcedirList(Set<Spappresourcedir> spappresourcedirList) {
        this.spappresourcedirList = spappresourcedirList;
    }

    @XmlTransient
    public Set<Spappresourcedir> getSpappresourcedirList1() {
        return spappresourcedirList1;
    }

    public void setSpappresourcedirList1(Set<Spappresourcedir> spappresourcedirList1) {
        this.spappresourcedirList1 = spappresourcedirList1;
    }

    @XmlTransient
    public Set<Spexportschema> getSpexportschemaList() {
        return spexportschemaList;
    }

    public void setSpexportschemaList(Set<Spexportschema> spexportschemaList) {
        this.spexportschemaList = spexportschemaList;
    }

    @XmlTransient
    public Set<Spexportschema> getSpexportschemaList1() {
        return spexportschemaList1;
    }

    public void setSpexportschemaList1(Set<Spexportschema> spexportschemaList1) {
        this.spexportschemaList1 = spexportschemaList1;
    }

    @XmlTransient
    public Set<Collectionrelationship> getCollectionrelationshipList() {
        return collectionrelationshipList;
    }

    public void setCollectionrelationshipList(Set<Collectionrelationship> collectionrelationshipList) {
        this.collectionrelationshipList = collectionrelationshipList;
    }

    @XmlTransient
    public Set<Collectionrelationship> getCollectionrelationshipList1() {
        return collectionrelationshipList1;
    }

    public void setCollectionrelationshipList1(Set<Collectionrelationship> collectionrelationshipList1) {
        this.collectionrelationshipList1 = collectionrelationshipList1;
    }

    @XmlTransient
    public Set<Attachmenttag> getAttachmenttagList() {
        return attachmenttagList;
    }

    public void setAttachmenttagList(Set<Attachmenttag> attachmenttagList) {
        this.attachmenttagList = attachmenttagList;
    }

    @XmlTransient
    public Set<Attachmenttag> getAttachmenttagList1() {
        return attachmenttagList1;
    }

    public void setAttachmenttagList1(Set<Attachmenttag> attachmenttagList1) {
        this.attachmenttagList1 = attachmenttagList1;
    }

    @XmlTransient
    public Set<Geographytreedefitem> getGeographytreedefitemList() {
        return geographytreedefitemList;
    }

    public void setGeographytreedefitemList(Set<Geographytreedefitem> geographytreedefitemList) {
        this.geographytreedefitemList = geographytreedefitemList;
    }

    @XmlTransient
    public Set<Geographytreedefitem> getGeographytreedefitemList1() {
        return geographytreedefitemList1;
    }

    public void setGeographytreedefitemList1(Set<Geographytreedefitem> geographytreedefitemList1) {
        this.geographytreedefitemList1 = geographytreedefitemList1;
    }

    @XmlTransient
    public Set<Borrowagent> getBorrowagentList() {
        return borrowagentList;
    }

    public void setBorrowagentList(Set<Borrowagent> borrowagentList) {
        this.borrowagentList = borrowagentList;
    }

    @XmlTransient
    public Set<Borrowagent> getBorrowagentList1() {
        return borrowagentList1;
    }

    public void setBorrowagentList1(Set<Borrowagent> borrowagentList1) {
        this.borrowagentList1 = borrowagentList1;
    }

    @XmlTransient
    public Set<Borrowagent> getBorrowagentList2() {
        return borrowagentList2;
    }

    public void setBorrowagentList2(Set<Borrowagent> borrowagentList2) {
        this.borrowagentList2 = borrowagentList2;
    }

    @XmlTransient
    public Set<Geologictimeperiodtreedefitem> getGeologictimeperiodtreedefitemList() {
        return geologictimeperiodtreedefitemList;
    }

    public void setGeologictimeperiodtreedefitemList(Set<Geologictimeperiodtreedefitem> geologictimeperiodtreedefitemList) {
        this.geologictimeperiodtreedefitemList = geologictimeperiodtreedefitemList;
    }

    @XmlTransient
    public Set<Geologictimeperiodtreedefitem> getGeologictimeperiodtreedefitemList1() {
        return geologictimeperiodtreedefitemList1;
    }

    public void setGeologictimeperiodtreedefitemList1(Set<Geologictimeperiodtreedefitem> geologictimeperiodtreedefitemList1) {
        this.geologictimeperiodtreedefitemList1 = geologictimeperiodtreedefitemList1;
    }

    @XmlTransient
    public Set<Loan> getLoanList() {
        return loanList;
    }

    public void setLoanList(Set<Loan> loanList) {
        this.loanList = loanList;
    }

    @XmlTransient
    public Set<Loan> getLoanList1() {
        return loanList1;
    }

    public void setLoanList1(Set<Loan> loanList1) {
        this.loanList1 = loanList1;
    }

    @XmlTransient
    public Set<Fieldnotebookpage> getFieldnotebookpageList() {
        return fieldnotebookpageList;
    }

    public void setFieldnotebookpageList(Set<Fieldnotebookpage> fieldnotebookpageList) {
        this.fieldnotebookpageList = fieldnotebookpageList;
    }

    @XmlTransient
    public Set<Fieldnotebookpage> getFieldnotebookpageList1() {
        return fieldnotebookpageList1;
    }

    public void setFieldnotebookpageList1(Set<Fieldnotebookpage> fieldnotebookpageList1) {
        this.fieldnotebookpageList1 = fieldnotebookpageList1;
    }

    @XmlTransient
    public Set<Agentspecialty> getAgentspecialtyList() {
        return agentspecialtyList;
    }

    public void setAgentspecialtyList(Set<Agentspecialty> agentspecialtyList) {
        this.agentspecialtyList = agentspecialtyList;
    }

    @XmlTransient
    public Set<Agentspecialty> getAgentspecialtyList1() {
        return agentspecialtyList1;
    }

    public void setAgentspecialtyList1(Set<Agentspecialty> agentspecialtyList1) {
        this.agentspecialtyList1 = agentspecialtyList1;
    }

    @XmlTransient
    public Set<Agentspecialty> getAgentspecialtyList2() {
        return agentspecialtyList2;
    }

    public void setAgentspecialtyList2(Set<Agentspecialty> agentspecialtyList2) {
        this.agentspecialtyList2 = agentspecialtyList2;
    }

    @XmlTransient
    public Set<Picklist> getPicklistList() {
        return picklistList;
    }

    public void setPicklistList(Set<Picklist> picklistList) {
        this.picklistList = picklistList;
    }

    @XmlTransient
    public Set<Picklist> getPicklistList1() {
        return picklistList1;
    }

    public void setPicklistList1(Set<Picklist> picklistList1) {
        this.picklistList1 = picklistList1;
    }

    @XmlTransient
    public Set<Accession> getAccessionList() {
        return accessionList;
    }

    public void setAccessionList(Set<Accession> accessionList) {
        this.accessionList = accessionList;
    }

    @XmlTransient
    public Set<Accession> getAccessionList1() {
        return accessionList1;
    }

    public void setAccessionList1(Set<Accession> accessionList1) {
        this.accessionList1 = accessionList1;
    }

    @XmlTransient
    public Set<Storage> getStorageList() {
        return storageList;
    }

    public void setStorageList(Set<Storage> storageList) {
        this.storageList = storageList;
    }

    @XmlTransient
    public Set<Storage> getStorageList1() {
        return storageList1;
    }

    public void setStorageList1(Set<Storage> storageList1) {
        this.storageList1 = storageList1;
    }

    @XmlTransient
    public Set<Dnasequencerunattachment> getDnasequencerunattachmentList() {
        return dnasequencerunattachmentList;
    }

    public void setDnasequencerunattachmentList(Set<Dnasequencerunattachment> dnasequencerunattachmentList) {
        this.dnasequencerunattachmentList = dnasequencerunattachmentList;
    }

    @XmlTransient
    public Set<Dnasequencerunattachment> getDnasequencerunattachmentList1() {
        return dnasequencerunattachmentList1;
    }

    public void setDnasequencerunattachmentList1(Set<Dnasequencerunattachment> dnasequencerunattachmentList1) {
        this.dnasequencerunattachmentList1 = dnasequencerunattachmentList1;
    }

    @XmlTransient
    public Set<Giftagent> getGiftagentList() {
        return giftagentList;
    }

    public void setGiftagentList(Set<Giftagent> giftagentList) {
        this.giftagentList = giftagentList;
    }

    @XmlTransient
    public Set<Giftagent> getGiftagentList1() {
        return giftagentList1;
    }

    public void setGiftagentList1(Set<Giftagent> giftagentList1) {
        this.giftagentList1 = giftagentList1;
    }

    @XmlTransient
    public Set<Giftagent> getGiftagentList2() {
        return giftagentList2;
    }

    public void setGiftagentList2(Set<Giftagent> giftagentList2) {
        this.giftagentList2 = giftagentList2;
    }

    @XmlTransient
    public Set<Collector> getCollectorList() {
        return collectorList;
    }

    public void setCollectorList(Set<Collector> collectorList) {
        this.collectorList = collectorList;
    }

    @XmlTransient
    public Set<Collector> getCollectorList1() {
        return collectorList1;
    }

    public void setCollectorList1(Set<Collector> collectorList1) {
        this.collectorList1 = collectorList1;
    }

    @XmlTransient
    public Set<Collector> getCollectorList2() {
        return collectorList2;
    }

    public void setCollectorList2(Set<Collector> collectorList2) {
        this.collectorList2 = collectorList2;
    }

    @XmlTransient
    public Set<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(Set<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    @XmlTransient
    public Set<Attachment> getAttachmentList1() {
        return attachmentList1;
    }

    public void setAttachmentList1(Set<Attachment> attachmentList1) {
        this.attachmentList1 = attachmentList1;
    }

    @XmlTransient
    public Set<Appraisal> getAppraisalList() {
        return appraisalList;
    }

    public void setAppraisalList(Set<Appraisal> appraisalList) {
        this.appraisalList = appraisalList;
    }

    @XmlTransient
    public Set<Appraisal> getAppraisalList1() {
        return appraisalList1;
    }

    public void setAppraisalList1(Set<Appraisal> appraisalList1) {
        this.appraisalList1 = appraisalList1;
    }

    @XmlTransient
    public Set<Appraisal> getAppraisalList2() {
        return appraisalList2;
    }

    public void setAppraisalList2(Set<Appraisal> appraisalList2) {
        this.appraisalList2 = appraisalList2;
    } 
    
    @XmlTransient
    public Set<Agentgeography> getAgentgeographyList() {
        return agentgeographyList;
    }

    public void setAgentgeographyList(Set<Agentgeography> agentgeographyList) {
        this.agentgeographyList = agentgeographyList;
    }

    @XmlTransient
    public Set<Agentgeography> getAgentgeographyList1() {
        return agentgeographyList1;
    }

    public void setAgentgeographyList1(Set<Agentgeography> agentgeographyList1) {
        this.agentgeographyList1 = agentgeographyList1;
    }

    @XmlTransient
    public Set<Agentgeography> getAgentgeographyList2() {
        return agentgeographyList2;
    }

    public void setAgentgeographyList2(Set<Agentgeography> agentgeographyList2) {
        this.agentgeographyList2 = agentgeographyList2;
    }

    @XmlTransient
    public Set<Collectionobjectcitation> getCollectionobjectcitationList() {
        return collectionobjectcitationList;
    }

    public void setCollectionobjectcitationList(Set<Collectionobjectcitation> collectionobjectcitationList) {
        this.collectionobjectcitationList = collectionobjectcitationList;
    }

    @XmlTransient
    public Set<Collectionobjectcitation> getCollectionobjectcitationList1() {
        return collectionobjectcitationList1;
    }

    public void setCollectionobjectcitationList1(Set<Collectionobjectcitation> collectionobjectcitationList1) {
        this.collectionobjectcitationList1 = collectionobjectcitationList1;
    }

    @XmlTransient
    public Set<Spappresourcedata> getSpappresourcedataList() {
        return spappresourcedataList;
    }

    public void setSpappresourcedataList(Set<Spappresourcedata> spappresourcedataList) {
        this.spappresourcedataList = spappresourcedataList;
    }

    @XmlTransient
    public Set<Spappresourcedata> getSpappresourcedataList1() {
        return spappresourcedataList1;
    }

    public void setSpappresourcedataList1(Set<Spappresourcedata> spappresourcedataList1) {
        this.spappresourcedataList1 = spappresourcedataList1;
    }

    @XmlTransient
    public Set<Spexportschemaitemmapping> getSpexportschemaitemmappingList() {
        return spexportschemaitemmappingList;
    }

    public void setSpexportschemaitemmappingList(Set<Spexportschemaitemmapping> spexportschemaitemmappingList) {
        this.spexportschemaitemmappingList = spexportschemaitemmappingList;
    }

    @XmlTransient
    public Set<Spexportschemaitemmapping> getSpexportschemaitemmappingList1() {
        return spexportschemaitemmappingList1;
    }

    public void setSpexportschemaitemmappingList1(Set<Spexportschemaitemmapping> spexportschemaitemmappingList1) {
        this.spexportschemaitemmappingList1 = spexportschemaitemmappingList1;
    }

    @XmlTransient
    public Set<Specifyuser> getSpecifyuserList() {
        return specifyuserList;
    }

    public void setSpecifyuserList(Set<Specifyuser> specifyuserList) {
        this.specifyuserList = specifyuserList;
    }

    @XmlTransient
    public Set<Specifyuser> getSpecifyuserList1() {
        return specifyuserList1;
    }

    public void setSpecifyuserList1(Set<Specifyuser> specifyuserList1) {
        this.specifyuserList1 = specifyuserList1;
    }

    @XmlTransient
    public Set<Exchangein> getExchangeinList() {
        return exchangeinList;
    }

    public void setExchangeinList(Set<Exchangein> exchangeinList) {
        this.exchangeinList = exchangeinList;
    }

    @XmlTransient
    public Set<Exchangein> getExchangeinList1() {
        return exchangeinList1;
    }

    public void setExchangeinList1(Set<Exchangein> exchangeinList1) {
        this.exchangeinList1 = exchangeinList1;
    }

    @XmlTransient
    public Set<Exchangein> getExchangeinList2() {
        return exchangeinList2;
    }

    public void setExchangeinList2(Set<Exchangein> exchangeinList2) {
        this.exchangeinList2 = exchangeinList2;
    }

    @XmlTransient
    public Set<Exchangein> getExchangeinList3() {
        return exchangeinList3;
    }

    public void setExchangeinList3(Set<Exchangein> exchangeinList3) {
        this.exchangeinList3 = exchangeinList3;
    }

    @XmlTransient
    public Set<Fieldnotebookpagesetattachment> getFieldnotebookpagesetattachmentList() {
        return fieldnotebookpagesetattachmentList;
    }

    public void setFieldnotebookpagesetattachmentList(Set<Fieldnotebookpagesetattachment> fieldnotebookpagesetattachmentList) {
        this.fieldnotebookpagesetattachmentList = fieldnotebookpagesetattachmentList;
    }

    @XmlTransient
    public Set<Fieldnotebookpagesetattachment> getFieldnotebookpagesetattachmentList1() {
        return fieldnotebookpagesetattachmentList1;
    }

    public void setFieldnotebookpagesetattachmentList1(Set<Fieldnotebookpagesetattachment> fieldnotebookpagesetattachmentList1) {
        this.fieldnotebookpagesetattachmentList1 = fieldnotebookpagesetattachmentList1;
    }

    @XmlTransient
    public Set<Commonnametx> getCommonnametxList() {
        return commonnametxList;
    }

    public void setCommonnametxList(Set<Commonnametx> commonnametxList) {
        this.commonnametxList = commonnametxList;
    }

    @XmlTransient
    public Set<Commonnametx> getCommonnametxList1() {
        return commonnametxList1;
    }

    public void setCommonnametxList1(Set<Commonnametx> commonnametxList1) {
        this.commonnametxList1 = commonnametxList1;
    }

    @XmlTransient
    public Set<Loanpreparation> getLoanpreparationList() {
        return loanpreparationList;
    }

    public void setLoanpreparationList(Set<Loanpreparation> loanpreparationList) {
        this.loanpreparationList = loanpreparationList;
    }

    @XmlTransient
    public Set<Loanpreparation> getLoanpreparationList1() {
        return loanpreparationList1;
    }

    public void setLoanpreparationList1(Set<Loanpreparation> loanpreparationList1) {
        this.loanpreparationList1 = loanpreparationList1;
    }

    @XmlTransient
    public Set<Otheridentifier> getOtheridentifierList() {
        return otheridentifierList;
    }

    public void setOtheridentifierList(Set<Otheridentifier> otheridentifierList) {
        this.otheridentifierList = otheridentifierList;
    }

    @XmlTransient
    public Set<Otheridentifier> getOtheridentifierList1() {
        return otheridentifierList1;
    }

    public void setOtheridentifierList1(Set<Otheridentifier> otheridentifierList1) {
        this.otheridentifierList1 = otheridentifierList1;
    }

    @XmlTransient
    public Set<Spexportschemamapping> getSpexportschemamappingList() {
        return spexportschemamappingList;
    }

    public void setSpexportschemamappingList(Set<Spexportschemamapping> spexportschemamappingList) {
        this.spexportschemamappingList = spexportschemamappingList;
    }

    @XmlTransient
    public Set<Spexportschemamapping> getSpexportschemamappingList1() {
        return spexportschemamappingList1;
    }

    public void setSpexportschemamappingList1(Set<Spexportschemamapping> spexportschemamappingList1) {
        this.spexportschemamappingList1 = spexportschemamappingList1;
    }

    @XmlTransient
    public Set<Giftpreparation> getGiftpreparationList() {
        return giftpreparationList;
    }

    public void setGiftpreparationList(Set<Giftpreparation> giftpreparationList) {
        this.giftpreparationList = giftpreparationList;
    }

    @XmlTransient
    public Set<Giftpreparation> getGiftpreparationList1() {
        return giftpreparationList1;
    }

    public void setGiftpreparationList1(Set<Giftpreparation> giftpreparationList1) {
        this.giftpreparationList1 = giftpreparationList1;
    }

    @XmlTransient
    public Set<Spversion> getSpversionList() {
        return spversionList;
    }

    public void setSpversionList(Set<Spversion> spversionList) {
        this.spversionList = spversionList;
    }

    @XmlTransient
    public Set<Spversion> getSpversionList1() {
        return spversionList1;
    }

    public void setSpversionList1(Set<Spversion> spversionList1) {
        this.spversionList1 = spversionList1;
    }

    @XmlTransient
    public Set<Loanagent> getLoanagentList() {
        return loanagentList;
    }

    public void setLoanagentList(Set<Loanagent> loanagentList) {
        this.loanagentList = loanagentList;
    }

    @XmlTransient
    public Set<Loanagent> getLoanagentList1() {
        return loanagentList1;
    }

    public void setLoanagentList1(Set<Loanagent> loanagentList1) {
        this.loanagentList1 = loanagentList1;
    }

    @XmlTransient
    public Set<Loanagent> getLoanagentList2() {
        return loanagentList2;
    }

    public void setLoanagentList2(Set<Loanagent> loanagentList2) {
        this.loanagentList2 = loanagentList2;
    }

    @XmlTransient
    public Set<Spviewsetobj> getSpviewsetobjList() {
        return spviewsetobjList;
    }

    public void setSpviewsetobjList(Set<Spviewsetobj> spviewsetobjList) {
        this.spviewsetobjList = spviewsetobjList;
    }

    @XmlTransient
    public Set<Spviewsetobj> getSpviewsetobjList1() {
        return spviewsetobjList1;
    }

    public void setSpviewsetobjList1(Set<Spviewsetobj> spviewsetobjList1) {
        this.spviewsetobjList1 = spviewsetobjList1;
    }

    @XmlTransient
    public Set<Deaccessionpreparation> getDeaccessionpreparationList() {
        return deaccessionpreparationList;
    }

    public void setDeaccessionpreparationList(Set<Deaccessionpreparation> deaccessionpreparationList) {
        this.deaccessionpreparationList = deaccessionpreparationList;
    }

    @XmlTransient
    public Set<Deaccessionpreparation> getDeaccessionpreparationList1() {
        return deaccessionpreparationList1;
    }

    public void setDeaccessionpreparationList1(Set<Deaccessionpreparation> deaccessionpreparationList1) {
        this.deaccessionpreparationList1 = deaccessionpreparationList1;
    }

    @XmlTransient
    public Set<Accessionauthorization> getAccessionauthorizationList() {
        return accessionauthorizationList;
    }

    public void setAccessionauthorizationList(Set<Accessionauthorization> accessionauthorizationList) {
        this.accessionauthorizationList = accessionauthorizationList;
    }

    @XmlTransient
    public Set<Accessionauthorization> getAccessionauthorizationList1() {
        return accessionauthorizationList1;
    }

    public void setAccessionauthorizationList1(Set<Accessionauthorization> accessionauthorizationList1) {
        this.accessionauthorizationList1 = accessionauthorizationList1;
    }

    @XmlTransient
    public Set<Permit> getPermitList() {
        return permitList;
    }

    public void setPermitList(Set<Permit> permitList) {
        this.permitList = permitList;
    }

    @XmlTransient
    public Set<Permit> getPermitList1() {
        return permitList1;
    }

    public void setPermitList1(Set<Permit> permitList1) {
        this.permitList1 = permitList1;
    }

    @XmlTransient
    public Set<Permit> getPermitList2() {
        return permitList2;
    }

    public void setPermitList2(Set<Permit> permitList2) {
        this.permitList2 = permitList2;
    }

    @XmlTransient
    public Set<Permit> getPermitList3() {
        return permitList3;
    }

    public void setPermitList3(Set<Permit> permitList3) {
        this.permitList3 = permitList3;
    }

    @XmlTransient
    public Set<Collectionobjectattr> getCollectionobjectattrList() {
        return collectionobjectattrList;
    }

    public void setCollectionobjectattrList(Set<Collectionobjectattr> collectionobjectattrList) {
        this.collectionobjectattrList = collectionobjectattrList;
    }

    @XmlTransient
    public Set<Collectionobjectattr> getCollectionobjectattrList1() {
        return collectionobjectattrList1;
    }

    public void setCollectionobjectattrList1(Set<Collectionobjectattr> collectionobjectattrList1) {
        this.collectionobjectattrList1 = collectionobjectattrList1;
    }

    @XmlTransient
    public Set<Spauditlog> getSpauditlogList() {
        return spauditlogList;
    }

    public void setSpauditlogList(Set<Spauditlog> spauditlogList) {
        this.spauditlogList = spauditlogList;
    }

    @XmlTransient
    public Set<Spauditlog> getSpauditlogList1() {
        return spauditlogList1;
    }

    public void setSpauditlogList1(Set<Spauditlog> spauditlogList1) {
        this.spauditlogList1 = spauditlogList1;
    }

    @XmlTransient
    public Set<Spreport> getSpreportList() {
        return spreportList;
    }

    public void setSpreportList(Set<Spreport> spreportList) {
        this.spreportList = spreportList;
    }

    @XmlTransient
    public Set<Spreport> getSpreportList1() {
        return spreportList1;
    }

    public void setSpreportList1(Set<Spreport> spreportList1) {
        this.spreportList1 = spreportList1;
    }

    @XmlTransient
    public Set<Deaccession> getDeaccessionList() {
        return deaccessionList;
    }

    public void setDeaccessionList(Set<Deaccession> deaccessionList) {
        this.deaccessionList = deaccessionList;
    }

    @XmlTransient
    public Set<Deaccession> getDeaccessionList1() {
        return deaccessionList1;
    }

    public void setDeaccessionList1(Set<Deaccession> deaccessionList1) {
        this.deaccessionList1 = deaccessionList1;
    }

    @XmlTransient
    public Set<Loanattachment> getLoanattachmentList() {
        return loanattachmentList;
    }

    public void setLoanattachmentList(Set<Loanattachment> loanattachmentList) {
        this.loanattachmentList = loanattachmentList;
    }

    @XmlTransient
    public Set<Loanattachment> getLoanattachmentList1() {
        return loanattachmentList1;
    }

    public void setLoanattachmentList1(Set<Loanattachment> loanattachmentList1) {
        this.loanattachmentList1 = loanattachmentList1;
    }

    @XmlTransient
    public Set<Spsymbiotainstance> getSpsymbiotainstanceList() {
        return spsymbiotainstanceList;
    }

    public void setSpsymbiotainstanceList(Set<Spsymbiotainstance> spsymbiotainstanceList) {
        this.spsymbiotainstanceList = spsymbiotainstanceList;
    }

    @XmlTransient
    public Set<Spsymbiotainstance> getSpsymbiotainstanceList1() {
        return spsymbiotainstanceList1;
    }

    public void setSpsymbiotainstanceList1(Set<Spsymbiotainstance> spsymbiotainstanceList1) {
        this.spsymbiotainstanceList1 = spsymbiotainstanceList1;
    }
    
    
    @XmlTransient
//    @JsonIgnore
    public Set<Treatmenteventattachment> getTreatmenteventattachmentList() {
        return treatmenteventattachmentList;
    }

    public void setTreatmenteventattachmentList(Set<Treatmenteventattachment> treatmenteventattachmentList) {
        this.treatmenteventattachmentList = treatmenteventattachmentList;
    }

    @XmlTransient
//    @JsonIgnore
    public Set<Treatmenteventattachment> getTreatmenteventattachmentList1() {
        return treatmenteventattachmentList1;
    }

    public void setTreatmenteventattachmentList1(Set<Treatmenteventattachment> treatmenteventattachmentList1) {
        this.treatmenteventattachmentList1 = treatmenteventattachmentList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (agentID != null ? agentID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Agent)) {
            return false;
        }
        Agent other = (Agent) object;
        return !((this.agentID == null && other.agentID != null) || (this.agentID != null && !this.agentID.equals(other.agentID)));
    }

    @Override
    public String toString() {
        return "se.nrm.dina.datamodel.Agent[ agentID=" + agentID + " ]";
    }   
}
