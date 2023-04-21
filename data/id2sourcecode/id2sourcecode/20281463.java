    @Primitive
    public static Value unix_getsockname(final CodeRunner ctxt, final Value sock) throws Fail.Exception {
        final Channel ch = ctxt.getContext().getChannel(sock.asLong());
        if (ch == null) {
            Unix.fail(ctxt, "getsockname", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        return Unix.createSockAddr(ctxt, ch.getSocketAddress());
    }
