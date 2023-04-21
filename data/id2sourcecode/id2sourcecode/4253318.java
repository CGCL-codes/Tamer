    protected String request(byte[] request, byte[] objects, byte[] schema) throws Exception {
        if (this._c.checkProperty("directory.status", "master") || this._c.checkProperty("directory.status", "slave")) {
            return "remote product is already grouped";
        }
        if (this._c.checkProperty("directory.request", "request")) {
            return "remote product has another active request";
        }
        ByteArrayInputStream _bais = new ByteArrayInputStream(request);
        FileOutputStream _fos = new FileOutputStream(WBSAgnitioConfiguration.getHARequestFile());
        while (_bais.available() > 0) {
            _fos.write(_bais.read());
        }
        _fos.close();
        _bais.close();
        if (objects != null) {
            _bais = new ByteArrayInputStream(objects);
            _fos = new FileOutputStream(WBSAgnitioConfiguration.getSchemaObjectRequestFile());
            while (_bais.available() > 0) {
                _fos.write(_bais.read());
            }
            _fos.close();
            _bais.close();
        }
        if (schema != null) {
            _bais = new ByteArrayInputStream(schema);
            _fos = new FileOutputStream(WBSAgnitioConfiguration.getOptionalSchemaRequestFile());
            while (_bais.available() > 0) {
                _fos.write(_bais.read());
            }
            _fos.close();
            _bais.close();
        }
        HashMap<String, String> values = getValues(WBSAgnitioConfiguration.getHARequestFile());
        if (!values.containsKey("ldap.basedn")) {
            return "failed to establish the base domain of the request";
        }
        if (!values.get("ldap.basedn").equals(this._c.getProperty("ldap.basedn"))) {
            return "base domain does not match with remote product";
        }
        this._c.setProperty("directory.request", "request");
        this._c.setProperty("directory.remote", values.get("address.real"));
        this._c.store();
        return "done";
    }
