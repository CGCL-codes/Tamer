    @Primitive
    public static Value unix_listen(final CodeRunner ctxt, final Value socket, final Value backlog) throws Fail.Exception, FalseExit {
        final Channel ch = ctxt.getContext().getChannel(socket.asLong());
        if (ch == null) {
            Unix.fail(ctxt, "listen", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        try {
            ch.socketListen(backlog.asLong());
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Unix.fail(ctxt, "listen", ioe);
        }
        return Value.UNIT;
    }
