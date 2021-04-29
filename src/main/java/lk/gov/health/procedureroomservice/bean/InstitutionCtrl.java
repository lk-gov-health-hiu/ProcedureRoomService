/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.bean;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lk.gov.health.procedureroomservice.Institute;
import lk.gov.health.procedureservice.serviceutils.CurrentHashFacade;
import lk.gov.health.procedureservice.serviceutils.InstitutionFacade;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author user
 */
@Named("institutionCtrl")
@SessionScoped
public class InstitutionCtrl implements Serializable {
    
    private Institute selected = new Institute();  
    @EJB
    private CurrentHashFacade currHash = new CurrentHashFacade(); 
    
    @EJB
    private InstitutionFacade insFacede = new InstitutionFacade();
    
    
    public void managedInstitutions() {
        ArrayList<Institute> items;
        String mainAppUrl = "http://localhost:8080/chims/data?name=";
        try {
            Client client = Client.create();
            WebResource webResource1 = client.resource(mainAppUrl + "get_module_institutes_list");
            ClientResponse cr = webResource1.accept("application/json").get(ClientResponse.class);
            String outpt = cr.getEntity(String.class);
            JSONObject jo_ = (JSONObject) new JSONParser().parse(outpt);
            items = selected.getObjectList((JSONArray) jo_.get("data"));
            
            for (Institute inst : items) {
//                inst.setId(null);
//                super.create(inst);

                System.out.println("77777777777 -->" + inst.getCode());
            }
        } catch (org.json.simple.parser.ParseException ex) {
            Logger.getLogger(InstitutionCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    public String getLocalHash(){
        return currHash.Get_Current_Hash();
    }   
    
    public Institute getSelected() {
        return selected;
    }
    
    public void setSelected(Institute selected) {
        this.selected = selected;
    }    

    public CurrentHashFacade getCurrHash() {
        return currHash;
    }

    public void setCurrHash(CurrentHashFacade currHash) {
        this.currHash = currHash;
    }

    public InstitutionFacade getInsFacede() {
        return insFacede;
    }

    public void setInsFacede(InstitutionFacade insFacede) {
        this.insFacede = insFacede;
    }
    
}
