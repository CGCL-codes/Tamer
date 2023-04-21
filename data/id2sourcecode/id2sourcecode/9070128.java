    protected String breakRequest(String remoteAddress, byte[] request) throws Exception {
        if (!this._c.checkProperty("cluster.status", "master") && !this._c.checkProperty("cluster.status", "slave")) {
            return "remote product is not currently grouped";
        }
        if (!this._c.checkProperty("cluster.remote", remoteAddress)) {
            return "network address is not equivalent to the grouped product";
        }
        ByteArrayInputStream _bais = new ByteArrayInputStream(request);
        FileOutputStream _fos = new FileOutputStream(WBSAirbackConfiguration.getHABreakFile());
        while (_bais.available() > 0) {
            _fos.write(_bais.read());
        }
        _fos.close();
        _bais.close();
        this._c.setProperty("cluster.request", "break");
        this._c.store();
        return "done";
    }
