    void send(ByteBuffer bbuf) throws IOException {
        mgr.getChannel().send(bbuf, remote);
    }
