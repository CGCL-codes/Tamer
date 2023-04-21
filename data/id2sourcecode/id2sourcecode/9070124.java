    protected String request(byte[] request) throws Exception {
        if (this._c.checkProperty("cluster.status", "master") || this._c.checkProperty("cluster.status", "slave")) {
            return "remote product is already grouped";
        }
        if (this._c.checkProperty("cluster.request", "request")) {
            return "remote product has another active request";
        }
        ByteArrayInputStream _bais = new ByteArrayInputStream(request);
        FileOutputStream _fos = new FileOutputStream(WBSAirbackConfiguration.getHARequestFile());
        while (_bais.available() > 0) {
            _fos.write(_bais.read());
        }
        _fos.close();
        _bais.close();
        HashMap<String, String> values = getValues(WBSAirbackConfiguration.getHARequestFile());
        this._c.setProperty("cluster.request", "request");
        this._c.setProperty("cluster.remote", values.get("address.real"));
        this._c.store();
        return "done";
    }
