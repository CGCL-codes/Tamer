    @Override
    public void login() throws ConnectionImException {
        String resource = user.getResource();
        ConnectionConfiguration connectionConfig = null;
        if (resource == null || resource.equals("")) {
            resource = "notify4b";
        }
        if (serviceName != null) {
            connectionConfig = new ConnectionConfiguration(host, port, serviceName);
        } else {
            connectionConfig = new ConnectionConfiguration(host, port);
        }
        connector = new XMPPConnection(connectionConfig);
        try {
            connector.connect();
            connector.login(user.getId(), user.getPassword(), resource);
        } catch (XMPPException xmppE) {
            throw new ConnectionImException(xmppE);
        }
    }
