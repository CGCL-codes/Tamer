    @Primitive
    public static Value unix_shutdown(final CodeRunner ctxt, final Value socket, final Value cmd) throws Fail.Exception, FalseExit {
        final Channel ch = ctxt.getContext().getChannel(socket.asLong());
        if (ch == null) {
            Unix.fail(ctxt, "shutdown", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        final java.net.Socket s = ch.asSocket();
        if (s != null) {
            try {
                switch(cmd.asLong()) {
                    case Shutdown.SHUTDOWN_RECEIVE:
                        s.shutdownInput();
                        break;
                    case Shutdown.SHUTDOWN_SEND:
                        s.shutdownOutput();
                        break;
                    case Shutdown.SHUTDOWN_ALL:
                        s.shutdownOutput();
                        s.shutdownInput();
                        break;
                    default:
                        assert false : "invalid shutdown command";
                }
            } catch (final InterruptedIOException iioe) {
                final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
                fe.fillInStackTrace();
                throw fe;
            } catch (final IOException ioe) {
                Unix.fail(ctxt, "shutdown", ioe);
            }
        } else {
            Unix.fail(ctxt, "shutdown", Unix.INVALID_DESCRIPTOR_MSG);
        }
        return Value.UNIT;
    }
