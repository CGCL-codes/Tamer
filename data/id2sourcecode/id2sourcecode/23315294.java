    protected void initConnection(Connection connection) throws IOException {
        connection.getChannel().socket().setSendBufferSize(sendBufferSize * 1024);
        connection.getChannel().socket().setReceiveBufferSize(receiveBufferSize * 1024);
        connection.getChannel().socket().setTcpNoDelay(tcpNoDelay);
        connection.getChannel().socket().setKeepAlive(keepAlive);
    }
