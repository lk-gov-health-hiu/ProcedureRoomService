/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.bean;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lk.gov.health.procedureroomservice.DataSync;
import lk.gov.health.procedureservice.serviceutils.DataSyncFacade;

/**
 *
 * @author user
 */
@Named("dataSyncCtrl")
@SessionScoped
public class DataSyncCtrl implements Serializable {

    @EJB
    private DataSyncFacade dataSyncHash;
    private DataSync selected = new DataSync();

    public boolean isInstHashExist() {
        String jpql_ = "SELECT ds FROM DataSync ds WHERE ds.hashOwner = 'INSTITUTION'";
        return !dataSyncHash.findByJpql(jpql_).isEmpty();
    }

    public String Get_Inst_Sync_Hash() {
        String jpql_ = "SELECT ds FROM DataSync ds WHERE ds.hashOwner = 'INSTITUTION'";
        
        if(!dataSyncHash.findByJpql(jpql_).isEmpty()){
            return dataSyncHash.findByJpql(jpql_).get(0).getHashValue();
        }        
        return null;
    }

    public DataSync Get_Data_Sync() {
        String jpql_ = "SELECT ds FROM DataSync ds WHERE ds.hashOwner = 'INSTITUTION'";
        return dataSyncHash.findByJpql(jpql_).get(0);
    }

    public void manageHash(String hashOwner, String hashValue) {
        if (isInstHashExist()) {
            selected = Get_Data_Sync();
            selected.setHashValue(hashValue);

            dataSyncHash.edit(selected);
        } else {
            selected.setHashOwner(hashOwner);
            selected.setHashValue(hashValue);

            dataSyncHash.create(selected);
        }
    }

    public DataSyncFacade getDataSyncHash() {
        return dataSyncHash;
    }

    public void setDataSyncHash(DataSyncFacade dataSyncHash) {
        this.dataSyncHash = dataSyncHash;
    }

    public DataSync getSelected() {
        return selected;
    }

    public void setSelected(DataSync selected) {
        this.selected = selected;
    }

}
