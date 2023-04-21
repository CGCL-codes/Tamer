    @Primitive
    public static Value unix_accept(final CodeRunner ctxt, final Value socket) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        final Channel ch = context.getChannel(socket.asLong());
        if (ch == null) {
            Unix.fail(ctxt, "accept", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        final ServerSocket serv = ch.asServerSocket();
        if (serv == null) {
            Unix.fail(ctxt, "accept", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        try {
            context.enterBlockingSection();
            final java.net.Socket s = serv.accept();
            context.leaveBlockingSection();
            final int fd = ctxt.getContext().addChannel(new Channel(s));
            final Block res = Block.createBlock(0, Value.createFromLong(fd), Unix.createSockAddr(ctxt, (InetSocketAddress) s.getRemoteSocketAddress()));
            return Value.createFromBlock(res);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            Unix.fail(ctxt, "accept", ioe);
            return Value.UNIT;
        }
    }
