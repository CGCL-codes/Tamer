    public boolean updateAuthRecord(NSDictionary userInfoDictionary) {
        String username = (String) userInfoDictionary.valueForKey("username");
        if ((username == null) || (username.equals(""))) {
            LOG.warn("updateAuthRecord: NO USERNAME");
            return false;
        }
        if (!existsAuthRecord(username)) {
            LOG.warn("updateAuthRecord: " + username + " doesn't exist!");
            return false;
        }
        try {
            EOQualifier userAndRealm = ERXQ.and(ERXQ.equals(AuthRecordEO.USERNAME_KEY, username), ERXQ.equals(AuthRecordEO.REALM_KEY, _authRealm));
            AuthRecordEO authenticationRecord = AuthRecordEO.fetchAuthRecord(_xec, userAndRealm);
            LOG.info("updateAuthRecord: attempting to update AuthRecordEO: " + authenticationRecord.snapshot());
            String pass = (String) userInfoDictionary.valueForKey("password");
            if (pass != null) {
                _md.reset();
                try {
                    _md.update(pass.getBytes("UTF-8"));
                } catch (Exception x) {
                    LOG.error("updateAuthRecord: undigestable password rejected ..", x);
                    return false;
                }
                authenticationRecord.setPassword(new NSData(_md.digest()));
            }
            try {
                _xec.saveChanges();
                LOG.info("updateAuthRecord: SUCCESS");
            } catch (Exception exc) {
                _xec.revert();
                LOG.warn("updateAuthRecord: FAILURE");
            }
            return true;
        } catch (Exception x) {
            _xec.unlock();
            LOG.error("updateAuthRecord: - EXCEPTION SAVING USER RECORD", x);
            return false;
        }
    }
