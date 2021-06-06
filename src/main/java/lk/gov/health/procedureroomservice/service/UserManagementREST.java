/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
@Stateless
@Path("redirect")
public class UserManagementREST {

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
            if ((userRole.equals("System_Administrator") || userRole.equals("Institution_Administrator"))) {
                return Response.status(Response.Status.ACCEPTED).entity("http://localhost:8080/ProcedureRoom_K/app/index.xhtml?UserId=" + userId + "&UserName=" + userName +"&UserRole=" + userRole + "&Proc_Rooms=" + insList + "&userInstitution=" + institution + "&API_KEY=" + apiKey).build();
            }else if(getProcedureRoomCount(insList)==0){
                return Response.status(Response.Status.ACCEPTED).entity("http://localhost:8080/ProcedureRoom_K/app/pages/single_institute_procedures.xhtml?UserId=" + userId + "&UserName=" + userName +"&UserRole=" + userRole + "&Proc_Rooms=" + insList + "&userInstitution=" + institution + "&API_KEY=" + apiKey).build();
            }else{
                return Response.status(Response.Status.ACCEPTED).entity("http://localhost:8080/ProcedureRoom_K/app/pages/multiple_institute_procedures.xhtml?UserId=" + userId + "&UserName=" + userName +"&UserRole=" + userRole + "&Proc_Rooms=" + insList + "&userInstitution=" + institution + "&API_KEY=" + apiKey).build();
            }
//            return Response.status(Response.Status.ACCEPTED).entity("http://localhost:8080/ProcedureRoom/app/index.xhtml?UserId=" + userId + "&UserName=" + userName +"&UserRole=" + userRole + "&Proc_Rooms=" + insList + "&userInstitution=" + institution + "&API_KEY=" + apiKey).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity("Authorization issue, Please contact your system admin.").build();
        }
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
