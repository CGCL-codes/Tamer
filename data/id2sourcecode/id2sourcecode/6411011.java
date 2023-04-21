    public SocketChannel getChannel() throws InterruptedException {
        if (srv == null) {
            if (srvrun) {
                throw new IllegalArgumentException("dont call when no server listens");
            }
        } else if (!srvstart) {
            init();
        }
        return buf.take();
    }
