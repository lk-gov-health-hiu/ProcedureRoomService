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
import lk.gov.health.procedureroomservice.CurrentHash;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
@Stateless
@Path("lk.gov.health.procedureroomservice.currenthash")
public class CurrentHashFacadeREST extends AbstractFacade<CurrentHash> {

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;

    public CurrentHashFacadeREST() {
        super(CurrentHash.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(CurrentHash entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, CurrentHash entity) {
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
    public CurrentHash find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<CurrentHash> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<CurrentHash> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    @Path("/find_by_owner/{owner}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String findByOwner(@PathParam("owner") String owner) {
        JSONArray ja_ = new JSONArray();
        String jpql;
        Map m = new HashMap();
        
        jpql = "SELECT h FROM CurrentHash h WHERE h.owner = :owner";

        m.put("owner", owner);
        List<CurrentHash> groupItems = super.findByJpql(jpql, m);
        for (CurrentHash h : groupItems) {
            ja_.add(getJSONObject(h));
        }
        return ja_.toString();
    }
    
    private JSONObject getJSONObject(CurrentHash gi) {
        JSONObject jo_ = new JSONObject();
        jo_.put("id", gi.getId());
        jo_.put("currHash", gi.getCurrHash());
        jo_.put("owner", gi.getOwner());
        return jo_;
    }   
}
