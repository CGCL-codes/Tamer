    public SocketFactoryConnectionImpl(ORB orb, CorbaContactInfo contactInfo, boolean useSelectThreadToWait, boolean useWorkerThread) {
        super(orb, useSelectThreadToWait, useWorkerThread);
        this.contactInfo = contactInfo;
        boolean isBlocking = !useSelectThreadToWait;
        SocketInfo socketInfo = ((SocketFactoryContactInfoImpl) contactInfo).socketInfo;
        try {
            socket = orb.getORBData().getLegacySocketFactory().createSocket(socketInfo);
            socketChannel = socket.getChannel();
            if (socketChannel != null) {
                socketChannel.configureBlocking(isBlocking);
            } else {
                setUseSelectThreadToWait(false);
            }
            if (orb.transportDebugFlag) {
                dprint(".initialize: connection created: " + socket);
            }
        } catch (GetEndPointInfoAgainException ex) {
            throw wrapper.connectFailure(ex, socketInfo.getType(), socketInfo.getHost(), Integer.toString(socketInfo.getPort()));
        } catch (Exception ex) {
            throw wrapper.connectFailure(ex, socketInfo.getType(), socketInfo.getHost(), Integer.toString(socketInfo.getPort()));
        }
        state = OPENING;
    }
