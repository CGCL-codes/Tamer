    protected String breakRequest(String remoteAddress, byte[] request) throws Exception {
        if (!this._c.checkProperty("directory.status", "master") && !this._c.checkProperty("directory.status", "slave")) {
            return "remote product is not currently grouped";
        }
        if (!this._c.checkProperty("directory.remote", remoteAddress)) {
            return "network address is not equivalent to the grouped product";
        }
        ByteArrayInputStream _bais = new ByteArrayInputStream(request);
        FileOutputStream _fos = new FileOutputStream(WBSAgnitioConfiguration.getHABreakFile());
        while (_bais.available() > 0) {
            _fos.write(_bais.read());
        }
        _fos.close();
        _bais.close();
        this._c.setProperty("directory.request", "break");
        this._c.store();
        return "done";
    }
