/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureservice.serviceutils;

import java.util.HashMap;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lk.gov.health.procedureroomservice.CurrentHash;
import lk.gov.health.procedureroomservice.service.AbstractFacade;

/**
 *
 * @author user
 */
@Stateless
public class CurrentHashFacade extends AbstractFacade<CurrentHash> {

    @PersistenceContext(unitName = "hmisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CurrentHashFacade() {
        super(CurrentHash.class);
    }

    public String Get_Current_Hash() {
        HashMap<String, Object> p_ = new HashMap<>();
        String jpql_ = "SELECT h FROM CurrentHash h WHERE h.owner = :owner";
        p_.put("owner", "INSTITITUE");

        if (!this.findByJpql(jpql_, p_).isEmpty()) {
            return (this.findByJpql(jpql_, p_)).get(0).getCurrHash();
        } else {
            return null;
        }
    }
}
