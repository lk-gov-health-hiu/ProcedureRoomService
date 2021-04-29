/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
@Entity
@XmlRootElement
public class Institute implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String code;
    private String intituteTypeDb;
    private String intituteType;
    private String name;
    private String hin;
    private String address;
    private String provinceId;
    private String districtId;
    private String childInstitutes;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date editedAt;

    public ArrayList<Institute> getObjectList(JSONArray ja_) {
        ArrayList<Institute> ObjectList = new ArrayList<>();

        for (int i = 0; i < ja_.size(); i++) {
            ObjectList.add(new Institute().getObject((JSONObject) ja_.get(i)));
        }
        return ObjectList;
    }

    public Institute getObject(JSONObject jo_) {
        this.setId(jo_.containsKey("id") ? Long.parseLong(jo_.get("id").toString()) : null);
        this.setCode(jo_.containsKey("institute_id") ? jo_.get("institute_id").toString() : null);
        this.setIntituteTypeDb(jo_.containsKey("institute_type_db") ? jo_.get("institute_type_db").toString() : null);
        this.setIntituteType(jo_.containsKey("institute_type") ? jo_.get("institute_type").toString() : null);
        this.setName(jo_.containsKey("name") ? jo_.get("name").toString() : null);
        this.setHin(jo_.containsKey("hin") ? jo_.get("hin").toString() : null);
        this.setAddress(jo_.containsKey("address") ? jo_.get("address").toString() : null);
        this.setProvinceId(jo_.containsKey("provinceId") ? jo_.get("provinceId").toString() : null);
        this.setDistrictId(jo_.containsKey("districtId") ? jo_.get("districtId").toString() : null);
        this.setChildInstitutes(jo_.containsKey("child_ins_list") ? jo_.get("child_ins_list").toString() : null);
        this.setEditedAt(jo_.containsKey("edited_at") ? jo_.get("edited_at").toString() : new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(new Timestamp(System.currentTimeMillis())));

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
        if (!(object instanceof Institute)) {
            return false;
        }
        Institute other = (Institute) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.gov.health.procedureroomservice.Institute[ id=" + id + " ]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHin() {
        return hin;
    }

    public void setHin(String hin) {
        this.hin = hin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getIntituteTypeDb() {
        return intituteTypeDb;
    }

    public void setIntituteTypeDb(String intituteTypeDb) {
        this.intituteTypeDb = intituteTypeDb;
    }

    public String getIntituteType() {
        return intituteType;
    }

    public void setIntituteType(String intituteType) {
        this.intituteType = intituteType;
    }

    public String getChildInstitutes() {
        return childInstitutes;
    }

    public void setChildInstitutes(String childInstitutes) {
        this.childInstitutes = childInstitutes;
    }

    public Date getEditedAt() {
        return editedAt;
    }

//    public void setEditedAt(Date editedAt) {
//        this.editedAt = editedAt;
//    }

    public void setEditedAt(String editedAt) {
        DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date date;
        try {
            date = formatter.parse(editedAt);
            Timestamp timeStampDate = new Timestamp(date.getTime());
            this.editedAt = timeStampDate;
        } catch (ParseException ex) {
            Logger.getLogger(Institute.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
