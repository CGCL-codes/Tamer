    public String performAuthenticationInContext(CXAuthContext context) {
        String user = (String) context.infoForKey(CXAuthContext.UsernameKey);
        String pass = (String) context.infoForKey(CXAuthContext.PasswordKey);
        CXDirectoryPerson person = (CXDirectoryPerson) context.infoForKey("person");
        EOEnterpriseObject record;
        if ((person != null) && (isAdmin(person))) {
            _ec.lock();
            try {
                record = EOUtilities.objectMatchingValues(_ec, _authRecordEntityName, new NSDictionary(new Object[] { user }, _qualifierKeysAdminPassthrough));
            } catch (Exception e) {
                record = null;
            }
            _ec.unlock();
        } else if (user == null || pass == null) {
            return null;
        } else {
            _ec.lock();
            _md.reset();
            try {
                _md.update(pass.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException uee) {
                _ec.unlock();
                return null;
            }
            byte[] md5 = _md.digest();
            NSData passHash = new NSData(md5);
            try {
                record = EOUtilities.objectMatchingValues(_ec, _authRecordEntityName, new NSDictionary(new Object[] { user, passHash }, _qualifierKeys));
            } catch (Exception e) {
                record = null;
            }
            if (record == null) {
                try {
                    record = EOUtilities.objectMatchingValues(_ec, _authRecordEntityName, new NSDictionary(new Object[] { user, passHash }, _qualifierKeysSecondary));
                } catch (Exception e2) {
                    record = null;
                }
            }
            _ec.unlock();
        }
        return (record != null) ? user + "@" + _realm : null;
    }
