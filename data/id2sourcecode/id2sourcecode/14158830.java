    ClientHandler(Client client, boolean gameStarted, String message, EncryptedMessageReader reader, boolean requireSpectator, MessageServer server, EncryptedMessageWriter writer) {
        this.c = client;
        this.gameStarted = gameStarted;
        this.message = message;
        this.requireSpectator = requireSpectator;
        this.server = server;
        listenerThread = new ListenerThread(reader);
        listenerThread.start();
    }
