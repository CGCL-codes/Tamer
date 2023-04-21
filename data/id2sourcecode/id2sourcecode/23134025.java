    public String authenticateWithContext(CXAuthContext context) {
        String username = context.getUsername();
        if (username == null || username.length() == 0) {
            LOG.warn("performAuthenticationInContext: blank username rejected ..");
            return null;
        }
        if (context.getNewPerson() == null) {
            _md.reset();
            try {
                _md.update(context.getPassword().getBytes("UTF-8"));
            } catch (Exception x) {
                LOG.warn("performAuthenticationInContext: " + "Undigestable password rejected ..");
                return null;
            }
            NSData passHash = new NSData(_md.digest());
            EOQualifier userAndPW1 = ERXQ.and(ERXQ.equals(AuthRecordEO.USERNAME_KEY, username), ERXQ.equals(AuthRecordEO.PASSWORD_KEY, passHash));
            AuthRecordEO authenticationRecord = AuthRecordEO.fetchAuthRecord(_xec, userAndPW1);
            if (authenticationRecord == null) {
                LOG.warn("performAuthenticationInContext: " + "Not primary password.");
                EOQualifier userAndPW2 = ERXQ.and(ERXQ.equals(AuthRecordEO.USERNAME_KEY, username), ERXQ.equals(AuthRecordEO.TEMPPASSWORD_KEY, passHash));
                authenticationRecord = AuthRecordEO.fetchAuthRecord(_xec, userAndPW2);
                if (authenticationRecord == null) {
                    LOG.warn("performAuthenticationInContext: " + "Not secondary password.");
                    return null;
                }
            }
            return username + "@" + _authRealm;
        }
        if (context.getOldPerson().isAdministrator()) {
            LOG.info("performAuthenticationInContext: " + "admin now impersonating ..");
            return username + "@" + _authRealm;
        }
        LOG.error("performAuthenticationInContext: " + "non-admin trying to impersonate .. FAIL");
        return null;
    }
