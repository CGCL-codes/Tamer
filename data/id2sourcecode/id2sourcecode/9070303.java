    Client(boolean gameStarted, boolean isHost, boolean listenToClient, String message, boolean requireSpectator, MessageServer server, Socket socket) {
        id = currID++;
        EncryptedMessageReader reader = null;
        try {
            reader = new EncryptedMessageReader(new MessageReader(socket.getInputStream()), "RSA", 128, server.getSettings().getKeyPair().getPrivate());
            writer = new EncryptedMessageWriter(this, new MessageWriter(socket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.isHost = isHost;
        this.server = server;
        this.socket = socket;
        if (listenToClient) {
            NetworkUtilities.sendMessage(writer, new Message(Message.Type.CONNECTED, "connected", server.getSettings().getKeyPair().getPublic()), false);
            ch = new ClientHandler(this, gameStarted, message, reader, requireSpectator, server, writer);
        } else {
            NetworkUtilities.sendMessage(writer, new Message(Message.Type.CONNECTION_DENIED, message), false);
        }
    }
