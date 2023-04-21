    @RequestResponse
    public void connect(Value request) throws FaultException {
        if (connection != null) {
            connection.disconnect();
        }
        ConnectionConfiguration config;
        int port = request.getFirstChild("port").intValue();
        if (request.hasChildren("host") && port > 0) {
            config = new ConnectionConfiguration(request.getFirstChild("host").strValue(), port, request.getFirstChild("serviceName").strValue());
        } else {
            config = new ConnectionConfiguration(request.getFirstChild("serviceName").strValue());
        }
        connection = new XMPPConnection(config);
        try {
            connection.connect();
            if (request.hasChildren("resource")) {
                connection.login(request.getFirstChild("username").strValue(), request.getFirstChild("password").strValue(), request.getFirstChild("resource").strValue());
            } else {
                connection.login(request.getFirstChild("username").strValue(), request.getFirstChild("password").strValue(), "Jolie");
            }
        } catch (XMPPException e) {
            throw new FaultException("XMPPException", e);
        }
    }
