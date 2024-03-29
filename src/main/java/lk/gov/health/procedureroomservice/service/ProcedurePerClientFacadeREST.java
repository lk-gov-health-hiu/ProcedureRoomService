/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.net.ssl.SSLContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import lk.gov.health.procedureroomservice.ClientProcedure;
import lk.gov.health.procedureroomservice.Institute;
import lk.gov.health.procedureroomservice.MedProcedure;
import lk.gov.health.procedureroomservice.ProcedurePerClient;
import lk.gov.health.procedureroomservice.ProcedurePerInstitute;
import lk.gov.health.procedureroomservice.ProcedureType;
import lk.gov.health.procedureroomservice.bean.ProcedurePerClientCtrl;
import lk.gov.health.procedureservice.enums.ProcPerClientStates;
import lk.gov.health.procedureservice.serviceutils.InstitutionFacade;
import lk.gov.health.procedureservice.serviceutils.ProcedurePerClientFacade;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author user
 */
@Dependent
@Path("lk.gov.health.procedureroomservice.procedureperclient")
public class ProcedurePerClientFacadeREST extends AbstractFacade<ProcedurePerClient> {

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;

    public ProcedurePerClientFacadeREST() {
        super(ProcedurePerClient.class);
    }

    @EJB
    private InstitutionFacade insFacede = new InstitutionFacade();
    @EJB
    private ProcedurePerClientFacade procPerClientFacade = new ProcedurePerClientFacade();

    @Inject
    private InstituteFacadeREST instituteFacadeREST;
    @Inject
    private ProcedurePerInstituteFacadeREST ProcedurePerInstituteFacadeREST;
    @Inject
    private ProcedurePerClientCtrl procedurePerClientCtrl;

    private WebTarget webTarget;
    private javax.ws.rs.client.Client client;
    private WebTarget webTarget2;
    private javax.ws.rs.client.Client client2;

    @Resource(name = "baseAppUrlEnv")
    private String baseAppUrlEnv;

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(ProcedurePerClient entity) {
        entity.setId(null);
        entity.setCreatedAt(new Date());
        super.create(entity);
    }

    @POST
    @Path("/register_procedure")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void register_procedure(ClientProcedure entity) {
        ProcedurePerClient entity_ = new ProcedurePerClient();
        entity_.setId(null);
        entity_.setPhn(entity.getPhn());
        entity_.setInstituteId(getProcedureInstituteObj(entity.getInstituteId()));
        entity_.setCreatedBy(entity.getCreatedBy());
        entity_.setCreatedAt(new Date());
        entity_.setStatus(ProcPerClientStates.CREATED);

        procedurePerClientCtrl.getProcClientFacade().create(entity_);
    }

    private ProcedurePerInstitute getProcedurePerInstitueObj(String procPerInstitute) {
        HashMap<String, Object> m_ = new HashMap<>();

        String jpql_ = "SELECT pins FROM ProcedurePerInstitute pins WHERE pins.procedure.procId=:val_";
        m_.put("val_", procPerInstitute);

        return this.ProcedurePerInstituteFacadeREST.findByJpql(jpql_, m_).get(0);
    }

    private Institute getProcedureInstituteObj(String code) {
        HashMap<String, Object> m_ = new HashMap<>();

        String jpql_ = "SELECT pr FROM Institute pr WHERE pr.intituteTypeDb = :type AND pr.code=:insCode";
        m_.put("type", "Procedure_Room");
        m_.put("insCode", code);

        return this.instituteFacadeREST.findByJpql(jpql_, m_).get(0);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, ProcedurePerClient entity) {
        super.edit(entity);
    }

    @PUT
    @Path("/update_procedure/{id}/{newStatus}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void update_procedure(@PathParam("id") Long id, @PathParam("newStatus") String newStatus) {
        ProcPerClientStates procStatus = ProcPerClientStates.valueOf(newStatus);
        ProcedurePerClient entity_ = procPerClientFacade.find(id);
        entity_.setStatus(procStatus);
        procPerClientFacade.edit(entity_);
        
        Client client = Client.create();
        WebResource webResource1 = client.resource(baseAppUrlEnv+"/update_client_procedure/" + entity_.getMainAppId().toString() + "/" + newStatus);
        ClientResponse cr = webResource1.accept("application/json").get(ClientResponse.class);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String find(@PathParam("id") Long id) {
        return getJSONObject(super.find(id)).toString();
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String getAll() {
        JSONArray ja_ = new JSONArray();

        List<ProcedurePerClient> procPerClientList;
        procPerClientList = super.findAll();

        for (ProcedurePerClient procPerClient : procPerClientList) {
            ja_.add(getJSONObject(procPerClient));
        }
        return ja_.toString();
    }

    @GET
    @Path("/filer_list/{searchVal}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String findClintProcedurePerInst(@PathParam("searchVal") String instCode) {
        JSONArray ja_ = new JSONArray();
        String jpql;
        Map m = new HashMap();

        Institute insObj = instituteFacadeREST.getInstituteById(Long.valueOf(instCode));

        Sync_Procedures(insObj.getMainAppId().toString());
        jpql = "SELECT i FROM ProcedurePerClient i WHERE i.instituteId.id=:searchVal";
        m.put("searchVal", Long.valueOf(instCode));

        List<ProcedurePerClient> procList = super.findByJpql(jpql, m);

        for (ProcedurePerClient procPerClient : procList) {
            ja_.add(getJSONObject(procPerClient));
        }
        return ja_.toString();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        JSONArray ja_ = new JSONArray();

        List<ProcedurePerClient> procPerClientList;
        procPerClientList = super.findRange(new int[]{from, to});

        procPerClientList.forEach(procPerClient -> {
            ja_.add(getJSONObject(procPerClient));
        });
        return ja_.toString();
    }

    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public String countREST() {
        JSONObject jo = new JSONObject();
        jo.put("count", String.valueOf(super.count()));
        return jo.toJSONString();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    private void Sync_Procedures(String instId) {
        ArrayList<ProcedurePerClient> items;

        client = javax.ws.rs.client.ClientBuilder.newBuilder().sslContext(getSSLContext()).build();
        webTarget = client.target(baseAppUrlEnv + "?name=get_procedures_pending");
        try {
            WebTarget resource = webTarget;
            String outpt = resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);

            JSONObject jo_ = (JSONObject) new JSONParser().parse(outpt);
            items = getObjectList((JSONArray) jo_.get("data"));

            for (ProcedurePerClient item : items) {
                procedurePerClientCtrl.getProcClientFacade().create(item);
                client2 = javax.ws.rs.client.ClientBuilder.newBuilder().sslContext(getSSLContext()).build();
                webTarget2 = client2.target(baseAppUrlEnv + "?name=mark_request_as_received&id=" + item.getMainAppId());
                WebTarget resource2 = webTarget2;
                resource2.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
            }

        } catch (org.json.simple.parser.ParseException ex) {
            Logger.getLogger(ProcedurePerClientFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<ProcedurePerClient> getObjectList(JSONArray ja_) {
        ArrayList<ProcedurePerClient> ObjectList = new ArrayList<>();

        for (int i = 0; i < ja_.size(); i++) {
            JSONObject jo_ = (JSONObject) ja_.get(i);

            if (jo_.containsKey("institute_id")) {
                ObjectList.add(getObject(jo_));
            }
        }
        return ObjectList;
    }

    public ProcedurePerClient getObject(JSONObject jo_) {
        ProcedurePerClient procPerClient = new ProcedurePerClient();
        if (jo_.containsKey("institute_id")) {
            procPerClient.setMainAppId(jo_.containsKey("procedure_request_id") ? Long.parseLong(jo_.get("procedure_request_id").toString()) : null);
            procPerClient.setPhn(jo_.containsKey("client_phn") ? jo_.get("client_phn").toString() : null);
            procPerClient.setProcedureId(jo_.containsKey("procedure_id") ? jo_.get("procedure_id").toString() : null);
            procPerClient.setProcedureCode(jo_.containsKey("procedure_code") ? jo_.get("procedure_code").toString() : null);
            procPerClient.setProcedureName(jo_.containsKey("procedure_name") ? jo_.get("procedure_name").toString() : null);
            procPerClient.setClientName(jo_.containsKey("client_name") ? jo_.get("client_name").toString() : null);
            procPerClient.setInstituteId(jo_.containsKey("institute_id") ? getClientInstitute(Long.parseLong(jo_.get("institute_id").toString())) : null);
            procPerClient.setCreatedBy(jo_.containsKey("user_name") ? jo_.get("user_name").toString() : null);
            procPerClient.setCreatedAt(new Date());
            procPerClient.setStatus(ProcPerClientStates.CREATED);
        }
        return procPerClient;
    }

    public Institute getClientInstitute(Long insCode) {
        HashMap<String, Object> p_ = new HashMap<>();
        String jpql_ = "SELECT i FROM Institute i WHERE i.mainAppId = :insCode";
        p_.put("insCode", insCode);

        return insFacede.findByJpql(jpql_, p_).get(0);
    }

    private JSONObject getJSONObject(ProcedurePerClient procPerClient) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", procPerClient.getId());
        jo_.put("phn", procPerClient.getPhn());
        jo_.put("procedureName", procPerClient.getProcedureName());
        jo_.put("clientName", procPerClient.getClientName());
        jo_.put("createdBy", procPerClient.getCreatedBy());
        jo_.put("createdAt", procPerClient.getCreatedAt() != null ? new SimpleDateFormat("yyyy-MM-dd").format(procPerClient.getCreatedAt()) : new Date().toString());
        jo_.put("status", procPerClient.getStatus().toString());

        return jo_;
    }

    private JSONObject getProPerInstJSONObject(ProcedurePerInstitute proc) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", proc.getId());
        jo_.put("procedure", this.getMedProcJSONObject(proc.getProcedure()));
        jo_.put("institute", this.getInstitute(proc.getInstituteId()));

        return jo_;
    }

    private JSONObject getMedProcJSONObject(MedProcedure proc) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", proc.getId());
        jo_.put("procId", proc.getProcId());
        jo_.put("description", proc.getDescription());
        jo_.put("procType", getProcTypeObject(proc.getProcType()));
        jo_.put("comment", proc.getComment());
        jo_.put("status", proc.getStatus().toString());

        return jo_;
    }

    public JSONObject getProcTypeObject(ProcedureType obj) {
        JSONObject tempObj = new JSONObject();
        tempObj.put("id", obj.getId());
        tempObj.put("procedureType", obj.getProcedureType());
        tempObj.put("description", obj.getDescription());

        return tempObj;
    }

    public JSONObject getInstitute(Institute obj) {
        JSONObject tempObj = new JSONObject();
        tempObj.put("id", obj.getId());
        tempObj.put("code", obj.getCode());
        tempObj.put("institute_type_db", obj.getIntituteTypeDb());
        tempObj.put("institute_type", obj.getIntituteType());
        tempObj.put("hin", obj.getHin());
        tempObj.put("address", obj.getAddress());
        tempObj.put("provinceId", obj.getProvinceId());
        tempObj.put("districtId", obj.getDistrictId());

        return tempObj;
    }

    public InstituteFacadeREST getInstituteFacadeREST() {
        return instituteFacadeREST;
    }

    public void setInstituteFacadeREST(InstituteFacadeREST instituteFacadeREST) {
        this.instituteFacadeREST = instituteFacadeREST;
    }

    public ProcedurePerInstituteFacadeREST getProcedurePerInstituteFacadeREST() {
        return ProcedurePerInstituteFacadeREST;
    }

    public void setProcedurePerInstituteFacadeREST(ProcedurePerInstituteFacadeREST ProcedurePerInstituteFacadeREST) {
        this.ProcedurePerInstituteFacadeREST = ProcedurePerInstituteFacadeREST;
    }

    public ProcedurePerClientCtrl getProcedurePerClientCtrl() {
        return procedurePerClientCtrl;
    }

    public void setProcedurePerClientCtrl(ProcedurePerClientCtrl procedurePerClientCtrl) {
        this.procedurePerClientCtrl = procedurePerClientCtrl;
    }

    private SSLContext getSSLContext() {
        // for alternative implementation checkout org.glassfish.jersey.SslConfigurator
        javax.net.ssl.TrustManager x509 = new javax.net.ssl.X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
                return;
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
                return;
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, new javax.net.ssl.TrustManager[]{x509}, null);
        } catch (java.security.GeneralSecurityException ex) {
        }
        return ctx;
    }
}
