/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.bean;

import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lk.gov.health.procedureroomservice.ProcedurePerClient;
import lk.gov.health.procedureservice.serviceutils.ProcedurePerClientFacade;

/**
 *
 * @author user
 */
@Named("procedurePerClientCtrl")
@SessionScoped
public class ProcedurePerClientCtrl implements Serializable {

    private ProcedurePerClient selected = new ProcedurePerClient();
    private ArrayList<ProcedurePerClient> items = new ArrayList<>();

    @EJB
    private ProcedurePerClientFacade procClientFacade = new ProcedurePerClientFacade();

    public ProcedurePerClient getSelected() {
        return selected;
    }

    public void setSelected(ProcedurePerClient selected) {
        this.selected = selected;
    }

    public ArrayList<ProcedurePerClient> getItems() {
        return items;
    }

    public void setItems(ArrayList<ProcedurePerClient> items) {
        this.items = items;
    }    

    public ProcedurePerClientFacade getProcClientFacade() {
        return procClientFacade;
    }

    public void setProcClientFacade(ProcedurePerClientFacade procClientFacade) {
        this.procClientFacade = procClientFacade;
    }

}
