/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.bean;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lk.gov.health.procedureroomservice.CurrentHash;

/**
 *
 * @author user
 */
@Named("currentHashCtrl")
@SessionScoped
public class CurrentHashCtrl implements Serializable{
    
    private CurrentHash selected = new CurrentHash();

    public CurrentHash getSelected() {
        return selected;
    }

    public void setSelected(CurrentHash selected) {
        this.selected = selected;
    }   
}
