/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.datamodel.impl;
 
import se.nrm.dina.datamodel.BaseEntity;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;   
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement; 

/**
 *
 * @author idali
 */
@Entity
@Table(name = "fieldnotebookattachment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fieldnotebookattachment.findAll", query = "SELECT f FROM Fieldnotebookattachment f"),
    @NamedQuery(name = "Fieldnotebookattachment.findByFieldNotebookAttachmentId", query = "SELECT f FROM Fieldnotebookattachment f WHERE f.fieldNotebookAttachmentId = :fieldNotebookAttachmentId"), 
    @NamedQuery(name = "Fieldnotebookattachment.findByOrdinal", query = "SELECT f FROM Fieldnotebookattachment f WHERE f.ordinal = :ordinal")})
public class Fieldnotebookattachment extends BaseEntity {
   
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "FieldNotebookAttachmentId")
    private Integer fieldNotebookAttachmentId;
    
    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "Ordinal")
    private int ordinal;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "Remarks")
    private String remarks;
    
    @JoinColumn(name = "ModifiedByAgentID", referencedColumnName = "AgentID")
    @ManyToOne
    private Agent modifiedByAgentID;
    
    @JoinColumn(name = "CreatedByAgentID", referencedColumnName = "AgentID")
    @ManyToOne
    private Agent createdByAgentID;
    
    @JoinColumn(name = "FieldNotebookID", referencedColumnName = "FieldNotebookID")
    @ManyToOne(optional = false)
    private Fieldnotebook fieldNotebookID;
    
    @JoinColumn(name = "AttachmentID", referencedColumnName = "AttachmentID")
    @ManyToOne(optional = false)
    private Attachment attachmentID;

    public Fieldnotebookattachment() {
    }

    public Fieldnotebookattachment(Integer fieldNotebookAttachmentId) {
        this.fieldNotebookAttachmentId = fieldNotebookAttachmentId;
    }

    public Fieldnotebookattachment(Integer fieldNotebookAttachmentId, Date timestampCreated, int ordinal) {
        this.fieldNotebookAttachmentId = fieldNotebookAttachmentId;
        this.timestampCreated = timestampCreated;
        this.ordinal = ordinal;
    }
    
    @XmlID
    @XmlAttribute(name = "id")
    @Override
    public String getIdentityString() {
        return String.valueOf(fieldNotebookAttachmentId);
    }
    
//    @XmlAttribute(name = "uuid") 
//    @Override
//    public String getUUID() {
//        return Util.getInstance().getURLLink(this.getClass().getSimpleName()) + fieldNotebookAttachmentId;
//    }
    
    @Override
    public int getEntityId() {
        return fieldNotebookAttachmentId;
    }

    public Integer getFieldNotebookAttachmentId() {
        return fieldNotebookAttachmentId;
    }

    public void setFieldNotebookAttachmentId(Integer fieldNotebookAttachmentId) {
        this.fieldNotebookAttachmentId = fieldNotebookAttachmentId;
    }

  

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    @XmlIDREF
    public Fieldnotebook getFieldNotebookID() {
        return fieldNotebookID;
    }

    public void setFieldNotebookID(Fieldnotebook fieldNotebookID) {
        this.fieldNotebookID = fieldNotebookID;
    }

    @XmlIDREF
    public Attachment getAttachmentID() {
        return attachmentID;
    }

    public void setAttachmentID(Attachment attachmentID) {
        this.attachmentID = attachmentID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fieldNotebookAttachmentId != null ? fieldNotebookAttachmentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fieldnotebookattachment)) {
            return false;
        }
        Fieldnotebookattachment other = (Fieldnotebookattachment) object;
        return !((this.fieldNotebookAttachmentId == null && other.fieldNotebookAttachmentId != null) || (this.fieldNotebookAttachmentId != null && !this.fieldNotebookAttachmentId.equals(other.fieldNotebookAttachmentId)));
    }

    @Override
    public String toString() {
        return "se.nrm.dina.datamodel.Fieldnotebookattachment[ fieldNotebookAttachmentId=" + fieldNotebookAttachmentId + " ]";
    }  
}
