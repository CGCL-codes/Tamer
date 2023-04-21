    public void handle(INonBlockingConnection conn, ChangeStateMessage message) throws IOException {
        Main.broadcast(message, PeterHi.SOCKET, message.toSender);
        SocketServer ss = SocketServer.getInstance();
        ClientHandle cs = ss.get(conn);
        ChangeState op = new ChangeState(cs.getChannel(), cs.getEmail(), message.state, message.changedBits);
        Persister.getInstance().execute(op);
    }
