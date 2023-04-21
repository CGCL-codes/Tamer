    private void closeSockets() {
        try {
            inFromServer.close();
            outToServer.close();
            socket.close();
        } catch (Exception e) {
            Statics.logger.info("Unknown error while closing sockets/writers/readers");
        }
    }
