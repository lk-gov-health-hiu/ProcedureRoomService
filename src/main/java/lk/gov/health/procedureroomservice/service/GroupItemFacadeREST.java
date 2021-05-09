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
import lk.gov.health.procedureroomservice.GroupItem;
import lk.gov.health.procedureroomservice.MedProcedure;
import lk.gov.health.procedureroomservice.ProcedureGroup;
import lk.gov.health.procedureroomservice.ProcedureType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
@Stateless
@Path("lk.gov.health.procedureroomservice.groupitem")
public class GroupItemFacadeREST extends AbstractFacade<GroupItem> {

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;

    public GroupItemFacadeREST() {
        super(GroupItem.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(GroupItem entity) {
        entity.setId(null);
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, GroupItem entity) {
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
    public GroupItem find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<GroupItem> findAll() {
        return super.findAll();
    }

    @GET
    @Path("/filter_by_group/{groupId}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getItemsPerGroup(@PathParam("groupId") String groupId) {
        JSONArray ja_ = new JSONArray();

        String jpql;
        Map m = new HashMap();
        jpql = "SELECT gi FROM GroupItem gi WHERE gi.procGroup.procGroup = :procGroup";
        m.put("procGroup", groupId);
        List<GroupItem> groupItems = super.findByJpql(jpql, m);
        for (GroupItem i : groupItems) {
            ja_.add(getJSONObject(i));
        }
        return ja_.toString();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<GroupItem> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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

    private JSONObject getJSONObject(GroupItem gi) {
        JSONObject jo_ = new JSONObject();
        jo_.put("id", gi.getId());
        jo_.put("group", this.getProcGroupJSONObject(gi.getProcGroup()));
        jo_.put("procedure", this.getMedProcJSONObject(gi.getProcedure()));

        return jo_;
    }

    private JSONObject getProcGroupJSONObject(ProcedureGroup g_) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", g_.getId());
        jo_.put("procGroup", g_.getProcGroup());
        jo_.put("description", g_.getDescription());
        return jo_;
    }

    private JSONObject getMedProcJSONObject(MedProcedure mp_) {
        JSONObject jo_ = new JSONObject();

        jo_.put("id", mp_.getId());
        jo_.put("comment", mp_.getComment());
        jo_.put("procId", mp_.getProcId());
        jo_.put("description", mp_.getDescription());
        jo_.put("procType", getProcTypeObjct(mp_.getProcType()));

        return jo_;
    }

    public JSONObject getProcTypeObjct(ProcedureType obj) {
        JSONObject tempObj = new JSONObject();
        tempObj.put("id", obj.getId());
        tempObj.put("procedureType", obj.getProcedureType());
        tempObj.put("description", obj.getDescription());

        return tempObj;
    }

}
