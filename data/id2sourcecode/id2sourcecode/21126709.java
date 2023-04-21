    public void connect(IConnection conn) {
        if (!(conn instanceof RTMPConnection)) throw new RuntimeException("can only connect through RTMP connections");
        if (isConnected()) throw new RuntimeException("already connected");
        source = conn;
        SharedObjectMessage msg = new SharedObjectMessage(name, 0, isPersistentObject());
        msg.addEvent(new SharedObjectEvent(Type.SERVER_CONNECT, null, null));
        Channel c = ((RTMPConnection) conn).getChannel((byte) 3);
        c.write(msg);
    }
