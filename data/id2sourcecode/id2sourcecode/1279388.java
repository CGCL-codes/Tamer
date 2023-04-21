    public final void run() {
        logManager.writeToLog(4, "NET", "[" + threadID + "] Accepted new connection from: " + socket.getInetAddress().toString().replaceAll("/", ""));
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            din = new DataInputStream(in);
            dout = new DataOutputStream(out);
            remoteClient = new RemoteClient(threadID, socket.getInetAddress(), this);
            stateManager.addRemoteClient(threadID, remoteClient);
            sendMessage(Protocol.MSG_COMMENT, "Welcome to JaddasNode v." + nodeConfig.getNodeVersion());
            if (!authenticateConnection()) {
                logManager.writeToLog(2, "NET", "[" + threadID + "] Authentication Failed.");
                sendMessage(Protocol.MSG_NAK, "Authentication failed.");
                close();
                return;
            }
            remoteClient.setClientStatus(RemoteClient.CONNECTED);
            commandParser = new CommandParserClient(this, remoteClient);
            while (true) {
                try {
                    Message message = readMessage();
                    lastReceivedDataTime = System.nanoTime();
                    remoteClient.setLastActivityTime(System.currentTimeMillis());
                    logManager.writeToLog(6, "NET", "[" + threadID + ":" + message.getMessageID() + "] << " + message.getMessageString());
                    commandParser.processCommand(message);
                } catch (IOException e) {
                    break;
                }
            }
        } catch (IOException e) {
            logManager.writeToLog(1, "NET", "[" + threadID + "] Socket error: " + e.getMessage());
        }
        close();
    }
