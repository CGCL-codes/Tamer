    public String flushAll() {
        String resp = Operation.ERROR;
        ServerConnection serverConnection = getServerConnection("");
        if (serverConnection == null) {
            return (Operation.ERROR);
        }
        String command = "flush_all\r\n";
        ByteBuffer[] sendBuffers = new ByteBuffer[1];
        sendBuffers[0] = Operation.UTF8.encode(command);
        int bytesToWrite = sendBuffers[0].limit();
        BufferSet bs = getBufferSet();
        try {
            Operation.writeToChannel(serverConnection.getChannel(), sendBuffers, bytesToWrite);
            Operation.readResponse(serverConnection, bs, 0, Operation.END_OF_LINE);
            resp = Operation.readLine(new ByteBufferInputStream(bs));
            serverConnection.recycleConnection();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            serverConnection.closeConnection();
        }
        bs.freeBuffers();
        return (resp);
    }
