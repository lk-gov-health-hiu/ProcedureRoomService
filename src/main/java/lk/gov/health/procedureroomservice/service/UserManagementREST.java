/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.service;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
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
    public String userRedirection(
            @QueryParam("API_KEY") String apiKey,
            @QueryParam("User_Name") String userName,
            @QueryParam("User_Role") String userRole,
            @QueryParam("Privileges") String privilege,
            @QueryParam("Institution") String institution,
            @QueryParam("UserID") String UserId) {
System.out.println("666666666666666-->"+apiKey);
System.out.println("666666666666666-userName->"+userName);
System.out.println("666666666666666-userRole->"+userRole);
System.out.println("666666666666666-privilege->"+privilege);
System.out.println("666666666666666-institution->"+institution);
System.out.println("666666666666666-UserId->"+UserId);
        if (apiKey != null && !apiKey.trim().equals("") && apiKey.trim().equals("EF16A5D4EF8AA6AA0580AF1390CF0600")) {
            JSONObject jo_ = new JSONObject();
//            jo_.put("url", "http://localhost:8080/ProcedureRoom_K/index.xhtml?User_Name=" + userName + "&Privileges=" + privilege + "&UserID=" + UserId + "&Institution=" + institution + "&API_KEY=" + apiKey);
            return "http://localhost:8080/ProcedureRoom_K/index.xhtml?User_Name=" + userName + "&Privileges=" + privilege + "&UserID=" + UserId + "&Institution=" + institution + "&API_KEY=" + apiKey;
//            return Response.status(Response.Status.ACCEPTED).entity(jo_).build();
        } else {
//           return Response.status(Response.Status.CONFLICT).entity("Authorization issue, Please contact your system admin.").build();
        }
        return null;
        
    }

    private JSONObject errorMessage() {
        JSONObject jSONObjectOut = new JSONObject();
        jSONObjectOut.put("code", 400);
        jSONObjectOut.put("type", "error");
        jSONObjectOut.put("message", "Authorization issue, Please contact your system admin.");
        return jSONObjectOut;
    }
}
