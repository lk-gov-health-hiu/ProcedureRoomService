/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.procedureservice.enums;

/**
 *
 * @author user
 */
public enum UserRole {
    SYSTEM_ADMINISTRATOR("System Administrator"),
    SUPER_USER("Super User"),
    USER("User"),
    INSTITUTION_USER("Institution User"),
    INSTITUTION_SUPER_USER("Institution Super User"),
    INSTITUTION_ADMINISTRATOR("Institution Administrator"),
    ME_USER("Monitoring & Evaluation Administrator"),
    ME_SUPER_USER("Monitoring & Evaluation Super User"),
    ME_ADMIN("Monitoring & Evaluation User"),
    DOCTOR("Doctor"),
    NURSE("Nurse"),
    MIDWIFE("Midwife"),
    CLIENT("Client");

    private final String label;
    
    private UserRole(String label){
        this.label = label;
    }
    
    public String getLabel(){
        return label;
    } 
}
