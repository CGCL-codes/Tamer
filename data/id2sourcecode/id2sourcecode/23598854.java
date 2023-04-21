    public void request(String[] virtualAddress, String remoteAddress) throws Exception {
        if (this._c.getProperty("directory.request") != null) {
            throw new Exception("an active request already exists");
        }
        if (!this._nm.isNetworkAddress(this._nm.getInterface(0), NetworkManager.toAddress(remoteAddress))) {
            throw new Exception("remote address is not in the network range");
        }
        if (!this._nm.isNetworkAddress(this._nm.getInterface(0), virtualAddress)) {
            throw new Exception("virtual address is not in the network range");
        }
        StringBuilder xml_content = new StringBuilder();
        xml_content.append("<ha><address.real>");
        xml_content.append(this._nm.getStringAddress(this._nm.getInterface(0)));
        xml_content.append("</address.real><address.virtual>");
        xml_content.append(virtualAddress[0] + "." + virtualAddress[1] + "." + virtualAddress[2] + "." + virtualAddress[3]);
        xml_content.append("</address.virtual>");
        xml_content.append("<ldap.basedn>" + this._c.getProperty("ldap.basedn"));
        xml_content.append("</ldap.basedn></ha>");
        HTTPClient _hc = new HTTPClient(remoteAddress);
        if (TomcatConfiguration.checkHTTPS()) {
            _hc.setSecure(true);
        }
        HashMap<String, String> _parameters = new HashMap<String, String>();
        HashMap<String, byte[]> _files = new HashMap<String, byte[]>();
        _parameters.put("type", String.valueOf(CommResponse.TYPE_HA));
        _parameters.put("command", String.valueOf(CommResponse.COMMAND_REQUEST));
        _files.put("ha-request.xml", xml_content.toString().getBytes());
        if (new File(WBSAgnitioConfiguration.getSchemaObjectFile()).exists()) {
            ByteArrayOutputStream _baos = new ByteArrayOutputStream();
            FileInputStream _fis = new FileInputStream(WBSAgnitioConfiguration.getSchemaObjectFile());
            while (_fis.available() > 0) {
                _baos.write(_fis.read());
            }
            _fis.close();
            _files.put("schema_objects.xml", _baos.toByteArray());
            _baos.close();
        }
        if (new File(WBSAgnitioConfiguration.getOptionalSchemaFile()).exists()) {
            ByteArrayOutputStream _baos = new ByteArrayOutputStream();
            FileInputStream _fis = new FileInputStream(WBSAgnitioConfiguration.getOptionalSchemaFile());
            while (_fis.available() > 0) {
                _baos.write(_fis.read());
            }
            _fis.close();
            _files.put("optional.schema", _baos.toByteArray());
            _baos.close();
        }
        _hc.multipartLoad("/admin/Comm", _parameters, _files);
        String _reply = new String(_hc.getContent());
        if (_reply.isEmpty()) {
            throw new Exception("remote product has not sent any reply");
        } else if (_reply.indexOf("done") == -1) {
            throw new Exception(_reply);
        }
        this._c.setProperty("directory.remote", remoteAddress);
        this._c.setProperty("directory.request", "request");
        this._c.store();
    }
