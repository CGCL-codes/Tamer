package com.netime.commons.enterprise.acl;

public class Credential {

    private BaseSubject subject;

    private Permission[] permissions;

    public void setSubject(BaseSubject subject) {
        this.subject = subject;
    }

    public BaseSubject getSubject() {
        return subject;
    }

    public void setPermissions(Permission[] permissions) {
        this.permissions = permissions;
    }

    public Permission[] getPermissions() {
        return permissions;
    }
}
