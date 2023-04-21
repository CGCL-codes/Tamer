    public boolean _updateUserWithNewPassword(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        _ec.lock();
        _md.reset();
        try {
            _md.update(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            _ec.unlock();
            return false;
        }
        byte[] md5 = _md.digest();
        NSData passHash = new NSData(md5);
        EOEnterpriseObject eo;
        try {
            eo = EOUtilities.objectMatchingValues(_ec, _authRecordEntityName, new NSDictionary(new Object[] { username }, new String[] { _qualifierKeys[0] }));
        } catch (Exception e) {
            _ec.unlock();
            return false;
        }
        if (eo == null) return false;
        eo.takeStoredValueForKey(passHash, "password");
        eo.takeStoredValueForKey(null, "temppassword");
        _ec.saveChanges();
        _ec.unlock();
        return true;
    }
