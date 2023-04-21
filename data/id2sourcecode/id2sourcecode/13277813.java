    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            logger.debug("waiting for accept");
            while (true) {
                clientSocket = serverSocket.accept();
                logger.debug("connection established via serverport " + port + " on " + clientSocket.getPort());
                new ListenThread(clientSocket, this).start();
                writer = new WriteThread(clientSocket, this);
                writer.start();
            }
        } catch (Exception e) {
            exceptionCaught(e);
        }
    }
