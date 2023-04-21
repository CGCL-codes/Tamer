    private int interestOps(Socket socket) throws Exception {
        Selector selector = (Selector) PrivilegedAccessor.getValue(NIODispatcher.instance(), "primarySelector");
        return socket.getChannel().keyFor(selector).interestOps();
    }
