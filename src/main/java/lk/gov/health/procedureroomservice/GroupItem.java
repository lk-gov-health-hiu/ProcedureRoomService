/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@XmlRootElement
public class GroupItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private ProcedureGroup procGroup;
    @ManyToOne
    private MedProcedure procedure;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupItem)) {
            return false;
        }
        GroupItem other = (GroupItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.gov.health.procedureroomservice.GroupItem[ id=" + id + " ]";
    }

    public ProcedureGroup getProcGroup() {
        return procGroup;
    }

    public void setProcGroup(ProcedureGroup procGroup) {
        this.procGroup = procGroup;
    }

    public MedProcedure getProcedure() {
        return procedure;
    }

    public void setProcedure(MedProcedure procedure) {
        this.procedure = procedure;
    }
    
}
