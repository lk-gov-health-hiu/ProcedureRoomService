/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
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
import javax.ws.rs.core.MediaType;
import lk.gov.health.procedureroomservice.Institute;
import lk.gov.health.procedureroomservice.ProcedureGroup;
import lk.gov.health.procedureroomservice.ProcedureGroupPerInstitute;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
@Stateless
@Path("lk.gov.health.procedureroomservice.proceduregroupperinstitute")
public class ProcedureGroupPerInstituteFacadeREST extends AbstractFacade<ProcedureGroupPerInstitute> {

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;

    public ProcedureGroupPerInstituteFacadeREST() {
        super(ProcedureGroupPerInstitute.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(ProcedureGroupPerInstitute entity) {
        entity.setId(null);
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, ProcedureGroupPerInstitute entity) {
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
    public ProcedureGroupPerInstitute find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ProcedureGroupPerInstitute> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ProcedureGroupPerInstitute> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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

    @GET
    @Path("/proc_group_list/{instCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findFilteredList(@PathParam("instCode") String instCode) {
        JSONArray ja_ = new JSONArray();
        String jpql;
        Map m = new HashMap();
        jpql = "SELECT pi FROM ProcedureGroupPerInstitute pi WHERE pi.institute.code = :instVal";

        m.put("instVal", instCode);

        List<ProcedureGroupPerInstitute> procGroupPerInstList = super.findByJpql(jpql, m);
        procGroupPerInstList.forEach(proc -> {
            ja_.add(getJSONObject(proc));
        });
        return ja_.toString();
    }

    private JSONObject getJSONObject(ProcedureGroupPerInstitute proc) {
        JSONObject jo_ = new JSONObject();
        if (proc.getProcedureGroup().getId() != null && proc.getInstitute().getId() != null) {
            jo_.put("id", proc.getId());
            jo_.put("procGroup", this.getProcGroupJSONObject(proc.getProcedureGroup()));
            jo_.put("institute", this.getInstitute(proc.getInstitute()));
        }
        return jo_;
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

    private JSONObject getProcGroupJSONObject(ProcedureGroup pg_) {
        JSONObject jo_ = new JSONObject();
        jo_.put("id", pg_.getId());
        jo_.put("procGroup", pg_.getProcGroup());
        jo_.put("description", pg_.getDescription());
        return jo_;
    }

}
