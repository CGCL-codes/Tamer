    public void _addUser(String name, String pass) {
        _ec.lock();
        _md.reset();
        try {
            _md.update(pass.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            _ec.unlock();
            return;
        }
        byte[] md5 = _md.digest();
        NSData passHash = new NSData(md5);
        EOEnterpriseObject eo = EOUtilities.createAndInsertInstance(_ec, _authRecordEntityName);
        eo.takeStoredValueForKey(name, "username");
        eo.takeStoredValueForKey(passHash, "password");
        eo.takeStoredValueForKey(_realm, "realm");
        _ec.saveChanges();
        _ec.unlock();
    }
