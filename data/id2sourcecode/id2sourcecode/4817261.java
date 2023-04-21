    public Connection getNextNewConnection() {
        Connection con = (Connection) newConnections.removeFirst();
        try {
            con.getChannel().socket().setReceiveBufferSize(65536);
        } catch (SocketException e) {
            log.log(Level.WARNING, "receive buffer size failed", e);
        }
        return con;
    }
