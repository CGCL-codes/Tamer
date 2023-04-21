    protected void setUp() throws Exception {
        super.setUp();
        init();
        if (getMaxConnections() < 1) {
            return;
        }
        connections = new XMPPConnection[getMaxConnections()];
        try {
            for (int i = 0; i < getMaxConnections(); i++) {
                connections[i] = createConnection();
                connections[i].connect();
            }
            host = connections[0].getHost();
            serviceName = connections[0].getServiceName();
            if (!getConnection(0).getAccountManager().supportsAccountCreation()) fail("Server does not support account creation");
            for (int i = 0; i < getMaxConnections(); i++) {
                try {
                    getConnection(i).getAccountManager().createAccount(usernamnePrefix + i, usernamnePrefix + i);
                } catch (XMPPException e) {
                    if (e.getXMPPError() == null || e.getXMPPError().getCode() != 409) {
                        throw e;
                    }
                }
                getConnection(i).login(usernamnePrefix + i, usernamnePrefix + i, "Smack", sendInitialPresence());
            }
            Thread.sleep(150);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
