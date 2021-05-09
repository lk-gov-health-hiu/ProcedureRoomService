/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureservice.serviceutils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lk.gov.health.procedureroomservice.Institute;
import lk.gov.health.procedureroomservice.service.AbstractFacade;

/**
 *
 * @author user
 */
@Stateless
public class InstitutionFacade extends AbstractFacade<Institute>{

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public InstitutionFacade() {
        super(Institute.class);
    } 
}
