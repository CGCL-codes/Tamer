    public void handle(INonBlockingConnection conn, ChangeStateMessage message) throws IOException {
        Main.broadcast(message, false);
        SocketServer ss = SocketServer.getInstance();
        ClientHandle cs = ss.get(conn);
        ChangeState op = new ChangeState(cs.getChannel(), cs.getEmail(), message.state);
        Persister.getInstance().execute(op);
    }
