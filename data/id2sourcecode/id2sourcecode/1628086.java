    public void receiveMSG(MessageMSG message) {
        LOG.debug("--> receiveMSG");
        InputDataStream input = message.getDataStream();
        try {
            byte[] rawData = readData(input);
            LOG.debug("received " + rawData.length + " bytes. [" + (new String(rawData)) + "]");
            Request request = null;
            synchronized (MUTEX) {
                deserializer.deserialize(rawData, handler);
                request = (Request) handler.getResult();
                String userid = request.getUserId();
                DiscoveryManager discoveryManager = DiscoveryManagerFactory.getDiscoveryManager(null);
                if (!discoveryManager.hasSessionEstablished(userid)) {
                    RemoteUserProxyExt user = discoveryManager.getUser(userid);
                    LOG.debug("create new session for [" + user.getMutableUserDetails().getUsername() + "]");
                    SessionManager manager = SessionManager.getInstance();
                    manager.createSession(user, (TCPSession) message.getChannel().getSession());
                }
            }
            request.setMessage(message);
            filter.process(request);
        } catch (Exception e) {
            LOG.error("could not process request [" + e + "]");
        }
        LOG.debug("<-- receiveMSG");
    }
