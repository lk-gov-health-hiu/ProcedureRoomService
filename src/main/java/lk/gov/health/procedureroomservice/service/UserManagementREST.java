/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lk.gov.health.procedureroomservice.Institute;
import lk.gov.health.procedureroomservice.bean.DataSyncCtrl;
import lk.gov.health.procedureroomservice.bean.InstitutionCtrl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author user
 */
@Stateless
@Path("redirect")
public class UserManagementREST {
    
    String mainAppUrl = "http://localhost:8080/chimsd/data?name=";
    @Inject
    InstitutionCtrl institutionCtrl;
    @Inject
    DataSyncCtrl dataSyncCtrl;

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    public Response userRedirection(
            @QueryParam("API_KEY") String apiKey,
            @QueryParam("UserId") String userId,
            @QueryParam("UserName") String userName,
            @QueryParam("UserRole") String userRole,
            @QueryParam("insList") String insList,
            @QueryParam("userInstitution") String institution) {

        if (apiKey != null && !apiKey.trim().equals("") && apiKey.trim().equals("EF16A5D4EF8AA6AA0580AF1390CF0600")) {
//            if ((userRole.equals("System_Administrator") || userRole.equals("Institution_Administrator"))) {
//                return Response.status(Response.Status.ACCEPTED).entity("http://localhost:8080/ProcedureRoom_K/app/index.xhtml?UserId=" + userId + "&UserName=" + userName +"&UserRole=" + userRole + "&Proc_Rooms=" + insList + "&userInstitution=" + institution + "&API_KEY=" + apiKey).build();
//            }else if(getProcedureRoomCount(insList)==0){
//                return Response.status(Response.Status.ACCEPTED).entity("http://localhost:8080/ProcedureRoom_K/app/pages/single_institute_procedures.xhtml?UserId=" + userId + "&UserName=" + userName +"&UserRole=" + userRole + "&Proc_Rooms=" + insList + "&userInstitution=" + institution + "&API_KEY=" + apiKey).build();
//            }else{
//                return Response.status(Response.Status.ACCEPTED).entity("http://localhost:8080/ProcedureRoom_K/app/pages/multiple_institute_procedures.xhtml?UserId=" + userId + "&UserName=" + userName +"&UserRole=" + userRole + "&Proc_Rooms=" + insList + "&userInstitution=" + institution + "&API_KEY=" + apiKey).build();
//            }
            return Response.status(Response.Status.ACCEPTED).entity("http://chims.health.gov.lk/ProcedureRoom/app/index.xhtml?UserId=" + userId + "&UserName=" + userName +"&UserRole=" + userRole + "&Proc_Rooms=" + insList + "&userInstitution=" + institution + "&API_KEY=" + apiKey).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity("Authorization issue, Please contact your system admin.").build();
        }
    }
    
    public void Sync_Institutes() {
        if (!Is_Sync()) {
            ArrayList<Institute> items;
            
            try {
                Client client = Client.create();
                WebResource webResource1 = client.resource(mainAppUrl + "get_module_institute_list");
                ClientResponse cr = webResource1.accept("application/json").get(ClientResponse.class);
                String outpt = cr.getEntity(String.class);
                JSONObject jo_ = (JSONObject) new JSONParser().parse(outpt);
                items = institutionCtrl.getSelected().getObjectList((JSONArray) jo_.get("data"));

                for (Institute inst : items) {
                    if (inst.getMainAppId() != null) {
//                        if (isInstituteExists(inst.getMainAppId())) {
//                            if (checkInstChanged(inst)) {
//                                inst.setId(getInstitute(inst.getMainAppId()).getId());
//                                institutionCtrl.getInsFacede().edit(inst);
//                            }
//                        } else {
//                            institutionCtrl.getInsFacede().create(inst);
//                        }
                    }
                }
            } catch (org.json.simple.parser.ParseException ex) {
                Logger.getLogger(InstitutionCtrl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean Is_Sync() {
        if (!Get_Institute_Hash().equals(dataSyncCtrl.Get_Inst_Sync_Hash())) {
            dataSyncCtrl.manageHash("INSTITUTION", Get_Institute_Hash());
            return false;
        }
        return true;
    }
    
    public String Get_Institute_Hash() {
        Client client = Client.create();
        WebResource webResource1 = client.resource("http://localhost:8080/chimsd/data?name=get_institute_hash");
        ClientResponse cr = webResource1.accept("application/json").get(ClientResponse.class);
        String outpt = cr.getEntity(String.class);
        JSONObject jo_;
        try {
            if (outpt != null) {
                jo_ = (JSONObject) new JSONParser().parse(outpt);
                return jo_.get("data").toString();
            } else {
                return null;
            }
        } catch (ParseException ex) {
            Logger.getLogger(InstituteFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int getProcedureRoomCount(String procListStr) {
        Pattern pattern = Pattern.compile("[^A]*A");
        Matcher matcher = pattern.matcher(procListStr);
        int count = 0;
        while (matcher.find()) {
            count++;
        }        
        return count;
    }

    private JSONObject errorMessage() {
        JSONObject jSONObjectOut = new JSONObject();
        jSONObjectOut.put("code", 400);
        jSONObjectOut.put("type", "error");
        jSONObjectOut.put("message", "Authorization issue, Please contact your system admin.");
        return jSONObjectOut;
    }
}
