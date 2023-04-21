    private int interestOps(Socket socket) throws Exception {
        Selector selector = (Selector) PrivilegedAccessor.getValue(NIODispatcher.instance(), "selector");
        return socket.getChannel().keyFor(selector).interestOps();
    }
