    public void handle(INonBlockingConnection conn, EraseMessage message) throws IOException {
        Main.broadcast(message, false);
        SocketServer ss = SocketServer.getInstance();
        ClientHandle cs = ss.get(conn);
        RemoveAllShapes rs = new RemoveAllShapes(cs.getChannel());
        Persister.getInstance().execute(rs);
    }
