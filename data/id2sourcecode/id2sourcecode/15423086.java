    public void receiveMSG(MessageMSG msg) {
        Channel channel = msg.getChannel();
        InputDataStreamAdapter is = msg.getDataStream().getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String data;
        try {
            try {
                data = reader.readLine();
            } catch (IOException e) {
                msg.sendERR(BEEPError.CODE_PARAMETER_ERROR, "Error reading data");
                return;
            }
            if (data.equals(READY1) == false && data.equals(READY2) == false) {
                msg.sendERR(BEEPError.CODE_PARAMETER_INVALID, "Expected READY element");
            }
            this.begin(channel);
            msg.sendRPY(new StringOutputDataStream(PROCEED2));
        } catch (BEEPException e1) {
            channel.getSession().terminate("unable to send ERR");
            return;
        }
        try {
            Socket oldSocket = ((TCPSession) channel.getSession()).getSocket();
            SSLSocket newSocket = (SSLSocket) socketFactory.createSocket(oldSocket, oldSocket.getInetAddress().getHostName(), oldSocket.getPort(), true);
            BeepListenerHCL l = new BeepListenerHCL(channel);
            newSocket.addHandshakeCompletedListener(l);
            newSocket.setUseClientMode(false);
            newSocket.setNeedClientAuth(needClientAuth);
            newSocket.setEnabledCipherSuites(newSocket.getSupportedCipherSuites());
            newSocket.startHandshake();
        } catch (IOException e) {
            channel.getSession().terminate("TLS error: " + e.getMessage());
            return;
        }
    }
