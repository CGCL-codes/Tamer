    public java.io.InputStream KBgetModel(String modelName, int cmd) throws IOException, javax.jmi.xmi.MalformedXMIException {
        if (_MDRsupported) {
            clearCache();
            InputStream in = getConnection(cmd, modelName);
            MDRmanager.loadModel(in, _refp);
            try {
                if (_packg == null) _refo = (RefObject) _refp.refClass(_modelObjName).refAllOfType().iterator().next(); else _refo = (RefObject) _refp.refPackage(_packg).refClass(_modelObjName).refAllOfType().iterator().next();
            } catch (Exception ex) {
            }
            ;
            _newmodel = false;
            return null;
        } else {
            _buffer.reset();
            InputStream ins = getConnection(cmd, modelName);
            int x;
            System.out.println("Retrieving...");
            System.out.flush();
            while ((x = ins.read()) != -1) _buffer.write(x);
            _buffer.flush();
            return new ByteArrayInputStream(_buffer.toByteArray());
        }
    }
