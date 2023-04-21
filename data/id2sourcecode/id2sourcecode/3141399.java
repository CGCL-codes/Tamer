    public void reconnect(String hostName, int port) throws IOException {
        log.info("disconnecting from auth server/connecting to BOS server...");
        disconnect();
        connectToHost(hostName, port, Status.BOS_CONNECTING);
        writer = new WriterThread(connection, writeBuffer);
        writer.start();
    }
