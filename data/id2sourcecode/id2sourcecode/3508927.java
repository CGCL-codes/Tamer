    @Primitive
    public static Value unix_getpeername(final CodeRunner ctxt, final Value sock) throws Fail.Exception {
        final Channel ch = ctxt.getContext().getChannel(sock.asLong());
        if (ch == null) {
            Unix.fail(ctxt, "getpeername", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        final java.net.Socket s = ch.asSocket();
        final java.net.DatagramSocket ds = ch.asDatagramSocket();
        if (s != null) {
            return Unix.createSockAddr(ctxt, (InetSocketAddress) s.getRemoteSocketAddress());
        } else if (ds != null) {
            return Unix.createSockAddr(ctxt, (InetSocketAddress) ds.getRemoteSocketAddress());
        } else {
            Unix.fail(ctxt, "getpeername", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
    }
