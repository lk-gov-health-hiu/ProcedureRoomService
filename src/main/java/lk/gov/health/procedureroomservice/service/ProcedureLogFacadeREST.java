/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureroomservice.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import lk.gov.health.procedureroomservice.ProcedureLog;

/**
 *
 * @author user
 */
@Stateless
@Path("lk.gov.health.procedureroomservice.procedure_log")
public class ProcedureLogFacadeREST extends AbstractFacade<ProcedureLog> {

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;
    
    public ProcedureLogFacadeREST() {
        super(ProcedureLog.class);
    }
    
    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(ProcedureLog entity) {
        entity.setId(null);
        super.create(entity);
    }
    
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, ProcedureLog entity) {
        super.edit(entity);
    }
    
    
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
