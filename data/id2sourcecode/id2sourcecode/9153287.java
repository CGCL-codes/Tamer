    protected boolean doIsBound(String name) {
        ServerSocketChannel ssc = null;
        Listener listener = container.getListener(name);
        ssc = (ServerSocketChannel) listener.getChannel();
        if (ssc != null) {
            if (!ssc.socket().isClosed()) {
                return ssc.socket().isBound();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
