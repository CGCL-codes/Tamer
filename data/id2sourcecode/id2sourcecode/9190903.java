    public void run() {
        try {
            serverSocket = new ServerSocket(portNumber);
            serverSocket.setSoTimeout(0);
            getLogger().addMessage("Starting LogginServer, it accepts clients at address " + serverSocket.getInetAddress().getHostAddress() + ":" + String.valueOf(portNumber));
            getLogger().addMessage("Jude Main Database read and write files in the directory " + getKnowledgeBaseDirectory());
        } catch (Exception ex) {
            getLogger().addErrorMessage(ex);
        }
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                getLogger().addDebugMessage("Received connection request from: " + clientSocket.getInetAddress());
                ServerDatabase server = new ServerDatabase(this, clientSocket, getXSBManager(), getKnowledgeBaseDirectory(), getLogger());
                server.start();
            } catch (Exception ex) {
                getLogger().addErrorMessage(ex);
            }
        }
    }
