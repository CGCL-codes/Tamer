    public void receiveMSG(MessageMSG message) {
        LOG.debug("--> receiveMSG(" + message + ")");
        try {
            Request request = null;
            if (!NetworkServiceImpl.getInstance().isStopped()) {
                InputDataStream input = message.getDataStream();
                byte[] rawData = DataStreamHelper.read(input);
                LOG.debug("received " + rawData.length + " bytes. [" + (new String(rawData)) + "]");
                deserializer.deserialize(rawData, handler);
                request = handler.getResult();
                String userid = request.getUserId();
                DiscoveryManager discoveryManager = DiscoveryManagerFactory.getDiscoveryManager();
                RemoteUserProxyExt user = discoveryManager.getUser(userid);
                if (user == null) {
                    if (request.getType() != ProtocolConstants.CONCEAL) {
                        LOG.debug("add new RemoteUserProxy for [" + userid + "] [" + request.getType() + "]");
                        MutableUserDetails details = new MutableUserDetails(UNKNOWN_USER);
                        user = RemoteUserProxyFactory.getInstance().createProxy(userid, details);
                        discoveryManager.addUser(user);
                    } else {
                        LOG.debug("user null and requestType == CONCEAL, ignore request");
                        request = new RequestImpl(ProtocolConstants.NULL, null, null);
                    }
                }
                if (!discoveryManager.hasSessionEstablished(userid) && request.getType() != ProtocolConstants.NULL) {
                    LOG.debug("create RemoteUserSession for [" + user.getMutableUserDetails().getUsername() + "]");
                    SessionManager manager = SessionManager.getInstance();
                    Channel mainChannel = message.getChannel();
                    manager.createSession(user, (TCPSession) mainChannel.getSession(), mainChannel);
                }
            } else {
                request = new RequestImpl(ProtocolConstants.SHUTDOWN, null, null);
            }
            request.setMessage(message);
            filter.process(request);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("could not process request [" + e + "]");
        }
        LOG.debug("<-- receiveMSG");
    }
