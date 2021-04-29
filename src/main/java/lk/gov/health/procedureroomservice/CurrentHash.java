/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
@Entity
@XmlRootElement
public class CurrentHash implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String currHash;
    private String owner; 
    
    public ArrayList<CurrentHash> getObjectList(JSONArray ja_) {
        ArrayList<CurrentHash> ObjectList = new ArrayList<>();

        for (int i = 0; i < ja_.size(); i++) {
            ObjectList.add(new CurrentHash().getObject((JSONObject) ja_.get(i)));
        }
        return ObjectList;
    }

    public CurrentHash getObject(JSONObject jo_) {
        this.setId(jo_.containsKey("id") ? Long.parseLong(jo_.get("id").toString()) : null);
        this.setCurrHash(jo_.containsKey("currHash") ? jo_.get("currHash").toString() : null);
        this.setOwner(jo_.containsKey("owner") ? jo_.get("owner").toString() : null);        
        return this;
    }

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
        if (!(object instanceof CurrentHash)) {
            return false;
        }
        CurrentHash other = (CurrentHash) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.gov.health.procedureroomservice.CurrentHash[ id=" + id + " ]";
    }

    public String getCurrHash() {
        return currHash;
    }

    public void setCurrHash(String currHash) {
        this.currHash = currHash;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    
}
