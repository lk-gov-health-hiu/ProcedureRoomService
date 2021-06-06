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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
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
import lk.gov.health.procedureroomservice.Institute;
import lk.gov.health.procedureservice.serviceutils.InstitutionFacade;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author user
 */
@Dependent
@Path("lk.gov.health.procedureroomservice.institute")
public class InstituteFacadeREST extends AbstractFacade<Institute> {

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;
    
    @EJB
    private InstitutionFacade insFacede = new InstitutionFacade();

    private WebTarget webTarget;
    private javax.ws.rs.client.Client client;

    public InstituteFacadeREST() {
        super(Institute.class);
    }
    public List<Institute> instItems;

    Institute institute = new Institute();

    @Resource(name = "baseAppUrlEnv")
    private String baseAppUrlEnv;

//    @Inject
//    InstitutionCtrl institutionCtrl;
    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Institute entity) {
        entity.setId(null);
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Institute entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Institute find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String getAll() {
        JSONArray ja_ = new JSONArray();

        List<Institute> institutionList;
        institutionList = super.findAll();

        for (Institute institute : institutionList) {
            ja_.add(getJSONObject(institute));
        }
        return ja_.toString();
    }

    @GET
    @Path("/get_procedure_rooms/{user_institute}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getInstitutionsRooms(@PathParam("user_institute") String userInstituteCode) {
        JSONArray ja_ = new JSONArray();
        Sync_Institutes();

        if (userInstituteCode.equals("NO_FILTER")) {
            for (Institute institute : Get_Procedure_Rooms()) {
                ja_.add(getJSONObject(institute));
            }
        } else {
            for (Institute institute : Get_Procedure_Rooms()) {
                if (Is_Procedure_Room_Child(userInstituteCode, institute)) {
                    ja_.add(getJSONObject(institute));
                }
            }
        }
        return ja_.toString();
    }

    @GET
    @Path("is_exists/{ins_code}")
    @Produces({MediaType.APPLICATION_JSON})
    public String isExists(@PathParam("ins_code") String userInstituteCode) {
        JSONArray ja_ = new JSONArray();

        for (Institute institute : Get_Procedure_Rooms()) {
            if (userInstituteCode == null) {
                ja_.add(getJSONObject(institute));
            } else {
                if (Is_Procedure_Room_Child(userInstituteCode, institute)) {
                    ja_.add(getJSONObject(institute));
                }
            }
        }
        return ja_.toString();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Institute> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    private JSONObject getJSONObject(Institute institute) {
        JSONObject jo_ = new JSONObject();
        jo_.put("id", institute.getId());
        jo_.put("code", institute.getCode());
        jo_.put("name", institute.getName());
        jo_.put("hin", institute.getHin());
        jo_.put("address", institute.getAddress());
        jo_.put("provinceId", institute.getProvinceId());
        jo_.put("districtId", institute.getDistrictId());
        jo_.put("childInstitutes", institute.getChildrenInstitutes());
        jo_.put("editedAt", institute.getEditedAt().toString());

        return jo_;
    }

    @GET
    @Path("/filer_list/{searchVal}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findFilteredList(@PathParam("searchVal") String searchVal) {
        JSONArray ja_ = new JSONArray();

//        if (!Is_Sync()) {
//            Sync_Institutes();
//            CurrentHash hashObj = currHash.findByField("owner", "INSTITUTE");
//            hashObj.setCurrHash(this.Get_Institute_Hash());
//            currHash.edit(hashObj);
//        }
        String jpql;
        Map m = new HashMap();
        jpql = "SELECT i FROM Institute i WHERE upper(i.code) like :searchVal";
        m.put("searchVal", "%" + searchVal.toUpperCase() + "%");

        List<Institute> instList = super.findByJpql(jpql, m);

        for (Institute inst : instList) {
            ja_.add(getJSONObject(inst));
        }
        return ja_.toString();
    }

    public void Sync_Institutes() {
        if (!Is_Sync()) {
            ArrayList<Institute> items;

            try {
                client = javax.ws.rs.client.ClientBuilder.newBuilder().sslContext(getSSLContext()).build();
                webTarget = client.target(baseAppUrlEnv + "?name=get_institute_and_unit_list");

                WebTarget resource = webTarget;
                String outpt = resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
                JSONObject jo_ = (JSONObject) new JSONParser().parse(outpt);
                items = institute.getObjectList((JSONArray) jo_.get("data"));

                for (Institute inst : items) {
                    if (inst.getMainAppId() != null) {
                        if (isInstituteExists(inst.getMainAppId())) {
                            if (checkInstChanged(inst)) {
                                inst.setId(getInstitute(inst.getMainAppId()).getId());
                                insFacede.edit(inst);
                            }
                        } else {
                            insFacede.create(inst);
                        }
                    }
                }
            } catch (org.json.simple.parser.ParseException ex) {
                Logger.getLogger(InstituteFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean checkInstChanged(Institute inst) {
        return (getInstitute(inst.getMainAppId()).getEditedAt() != inst.getEditedAt());
    }

    public boolean isInstituteExists(Long insCode) {
        HashMap<String, Object> p_ = new HashMap<>();
        String jpql_ = "SELECT i FROM Institute i WHERE i.mainAppId = :insCode";
        p_.put("insCode", insCode);

        return !super.findByJpql(jpql_, p_).isEmpty();
    }

    public Institute getInstitute(Long insCode) {
        HashMap<String, Object> p_ = new HashMap<>();
        String jpql_ = "SELECT i FROM Institute i WHERE i.mainAppId = :insCode";
        p_.put("insCode", insCode);

        return super.findByJpql(jpql_, p_).get(0);
    }

    public Institute getInstituteById(Long insCode) {
        HashMap<String, Object> p_ = new HashMap<>();
        String jpql_ = "SELECT i FROM Institute i WHERE i.id = :insCode";
        p_.put("insCode", insCode);

        return super.findByJpql(jpql_, p_).get(0);
    }

    public boolean Is_Sync() {
        return Get_Institute_Hash().equals(getLocalInstitutionHash());
    }

    public String Get_Institute_Hash() {
        client = javax.ws.rs.client.ClientBuilder.newBuilder().sslContext(getSSLContext()).build();
        webTarget = client.target(baseAppUrlEnv + "?name=get_institutes_list_hash");
        WebTarget resource = webTarget;
        String outpt = resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
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

    public List<Institute> Get_Procedure_Rooms() {
        HashMap<String, Object> p_ = new HashMap<>();
        String jpql_ = "SELECT i FROM Institute i WHERE i.intituteTypeDb = :intituteTypeDb";
        p_.put("intituteTypeDb", "Procedure_Room");

        return super.findByJpql(jpql_, p_);
    }

    public boolean Is_Procedure_Room_Child(String insCode, Institute institute) {
        HashMap<String, Object> p_ = new HashMap<>();
        String jpql_ = "SELECT i FROM Institute i WHERE i.mainAppId = :insCode AND i.childrenInstitutes LIKE '%" + institute.getCode() + "%'";
        p_.put("insCode", Long.parseLong(insCode));
        return !super.findByJpql(jpql_, p_).isEmpty();
    }

    private List<Institute> fillAllInstitutes() {
        return super.findByJpql("select i from Institute i order by i.name");
    }

    public Institute getClientInstitute(Long insCode) {
        HashMap<String, Object> p_ = new HashMap<>();
        String jpql_ = "SELECT i FROM Institute i WHERE i.mainAppId = :insCode";
        p_.put("insCode", insCode);

        return super.findByJpql(jpql_, p_).get(0);
    }

    public List<Institute> getInstitutions() {
        if (instItems == null) {
            instItems = fillAllInstitutes();
        }
        return instItems;
    }

    public String getLocalInstitutionHash() {
        return DigestUtils.md5Hex(getInstitutions().toString()).toUpperCase();
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
