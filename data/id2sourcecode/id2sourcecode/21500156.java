    public void connect() throws IllegalStateException, IOException, ProtocolException {
        if (loginState != LoginState.DISCONNECTED) {
            throw new IllegalStateException("The client is no longer at init state. Maybe it is already connected?");
        }
        udpChannel = DatagramChannel.open();
        udpChannel.socket().setSoTimeout(SOCKET_READ_TIMEOUT);
        udpChannel.configureBlocking(true);
        udpChannel.connect(serverAddress);
        writeWorker = new WriteWorker(udpChannel);
        writeWorker.startWorking();
        OutboundPacket cp = new SendLogin(this.clientApplicationName, this.clientOS, this.nickname, this.login, this.password, this.clientVersion);
        writeWorker.writePacketSynchroniously(cp);
        ByteBuffer b = ByteBuffer.allocate(512);
        int loginReplySize = udpChannel.read(b);
        b.limit(loginReplySize);
        LoginReply p = null;
        try {
            p = (LoginReply) InboundPacket.getPacketHandler(b);
        } catch (ProtocolException pe) {
            throw pe;
        }
        if (p.getLoginState() != LoginState.LOGGEDIN) {
            throw new ProtocolException("Could not join server. Reason: " + p.getLoginState().toString());
        }
        sessionKey = p.getSessionKey();
        clientId = p.getClientId();
        serverName = p.getServerName();
        serverWelcomeMessage = p.getWelcomeMessage();
        serverVersion = p.getServerVersion();
        readWorker = new ReadWorker(udpChannel);
        readWorker.startWorking();
        sessionLayer = new SessionLayer(sessionKey, clientId, readWorker, writeWorker);
        sessionLayer.onTimeout.addObserver(onServerTimeout);
        sessionLayer.addContentRecievedListener(onServerContent);
        sessionLayer.addVoiceRecievedListener(onVoiceData);
        sessionLayer.addVoiceRecievedListener(inboundVoiceStream.onVoiceRecieved);
        sessionLayer.startWorking();
        SendLoginChannelContent sendLoginChannelPayload = new SendLoginChannelContent();
        OutboundPacket[] slc = ReliableOutboundPacket.encapsulatePayload(sessionKey, clientId, clientAcknowledgmentNumber++, sendLoginChannelPayload);
        writeWorker.writePacketSynchroniously(slc);
    }
