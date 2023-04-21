    public void insertAuthRecord(String username, String pass) {
        _md.reset();
        try {
            _md.update(pass.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            return;
        }
        try {
            AuthRecordEO.createAuthRecord(_xec, new NSData(_md.digest()), _authRealm, username);
            _xec.saveChanges();
            LOG.info("insertAuthRecord: SUCCESS");
        } catch (Exception exc) {
            _xec.revert();
            LOG.warn("insertAuthRecord: FAILURE");
        }
    }
