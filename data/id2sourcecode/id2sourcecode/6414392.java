    @javax.jws.WebMethod()
    public byte[] retrieveBinary(URI pdURI) throws SOAPException {
        byte[] _binary = null;
        try {
            PDURI _parsedURI = new PDURI(pdURI);
            InputStream _inStream = this.jcrManager.readContent(_parsedURI.getDataRegistryPath());
            ByteArrayOutputStream _outStream = new ByteArrayOutputStream(1024);
            byte[] _bytes = new byte[512];
            int _readBytes;
            while ((_readBytes = _inStream.read(_bytes)) > 0) {
                _outStream.write(_bytes, 0, _readBytes);
            }
            _binary = _outStream.toByteArray();
            _inStream.close();
            _outStream.close();
        } catch (Exception _exp) {
            throw new SOAPException(_exp);
        }
        return _binary;
    }
