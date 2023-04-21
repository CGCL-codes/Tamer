    public void receiveMSG(MessageMSG message) {
        LOG.debug("--> receiveMSG(" + message + ")");
        try {
            boolean isDiscovery = false;
            RequestHandler requestHandler = null;
            RemoteUserProxyExt proxy = null;
            Channel channel = null;
            if (!NetworkServiceImpl.getInstance().isStopped()) {
                InputDataStream input = message.getDataStream();
                byte[] rawData = DataStreamHelper.read(input);
                Request response = null;
                synchronized (MUTEX) {
                    deserializer.deserialize(rawData, handler);
                    response = handler.getResult();
                }
                channel = message.getChannel();
                int type = response.getType();
                if (type == ProtocolConstants.CHANNEL_MAIN) {
                    requestHandler = mainHandler;
                    proxy = (RemoteUserProxyExt) response.getPayload();
                    isDiscovery = (proxy != null);
                } else if (type == ProtocolConstants.CHANNEL_SESSION) {
                    DocumentInfo info = (DocumentInfo) response.getPayload();
                    requestHandler = SessionRequestHandlerFactory.getInstance().createHandler(info);
                } else {
                    LOG.warn("unkown channel type, use main as default");
                    requestHandler = mainHandler;
                }
                channel.setRequestHandler(requestHandler);
            }
            try {
                if (isDiscovery) {
                    LOG.debug("discovered new user: " + proxy);
                    processDiscoveredUser(message, channel, proxy);
                } else {
                    OutputDataStream os = new OutputDataStream();
                    os.setComplete();
                    message.sendRPY(os);
                }
            } catch (Exception e) {
                LOG.error("could not send confirmation [" + e + ", " + e.getMessage() + "]");
            }
            cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("could not process request [" + e + "]");
        }
        LOG.debug("<-- receiveMSG()");
    }
