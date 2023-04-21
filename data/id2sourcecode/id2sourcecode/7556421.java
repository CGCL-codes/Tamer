    public void startup() throws XMPPException {
        String host;
        Integer port;
        String service;
        synchronized (mDefaultSettings) {
            host = (String) mDefaultSettings.get("host");
            port = Integer.parseInt((String) mDefaultSettings.get("port"));
            service = (String) mDefaultSettings.get("service");
        }
        ConnectionConfiguration connConfig = new ConnectionConfiguration(host, port, service);
        mConnection = new XMPPConnection(connConfig);
        mConnection.connect();
        String password = null;
        String resource = null;
        synchronized (mDefaultSettings) {
            mJid = mDefaultSettings.get("username") + "@" + mDefaultSettings.get("service");
            password = (String) mDefaultSettings.get("password");
            resource = (String) mDefaultSettings.get("resource");
        }
        mConnection.login(mJid, password, resource);
        mConnection.addConnectionListener(this);
        ServiceDiscoveryManager sdm = ServiceDiscoveryManager.getInstanceFor(mConnection);
        try {
            sdm.addFeature(MobilisManager.discoNamespace);
        } catch (Exception e) {
            MobilisManager.getLogger().warning("Problem with ServiceDiscoveryManager: " + e.getMessage());
        }
        synchronized (mServices) {
            for (MobilisService ms : mServices) {
                try {
                    ms.startup();
                    sdm.setNodeInformationProvider(ms.getNode(), ms);
                } catch (Exception e) {
                    MobilisManager.getLogger().warning("Couldn't startup Mobilis Service (" + ms.getIdent() + ") because of " + e.getClass().getName() + ": " + e.getMessage());
                }
            }
        }
        try {
            sdm.setNodeInformationProvider(MobilisManager.discoServicesNode, this);
        } catch (Exception e) {
            MobilisManager.getLogger().warning("Problem with NodeInformationProvider: " + MobilisManager.discoServicesNode + " (" + getIdent() + ") " + e.getMessage());
        }
        MobilisManager.getLogger().info("Mobilis Agent (" + getIdent() + ") started up.");
    }
