package com.sms.admintenant.models;

import org.springframework.security.core.GrantedAuthority;



public enum Authority implements GrantedAuthority {
    
    /** The role user. */
	/*
	 * ROLE_CLIENT_MASTER,ROLE_USER, ROLE_ADMIN,ORG_TENANT, ORG_ADMIN;
	 */
	
	ROLE_USER,ROLE_CLIENT_MASTER,ROLE_SITE_ADMIN,ROLE_SITE_USER,
	 ORG_ADMIN,ORG_TENANT,
	 ROLE_ADMIN;
	
    @Override
    public String getAuthority() {
        return this.name();
    }
}
