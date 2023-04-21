    @Primitive
    public static Value unix_send(final CodeRunner ctxt, final Value socket, final Value buff, final Value ofs, final Value len, final Value flags) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        try {
            final Channel ch = context.getChannel(socket.asLong());
            if (ch == null) {
                Unix.fail(ctxt, "send", Unix.INVALID_DESCRIPTOR_MSG);
                return Value.UNIT;
            }
            final java.net.Socket s = ch.asSocket();
            final DatagramSocket ds = ch.asDatagramSocket();
            if (s != null) {
                context.enterBlockingSection();
                s.getOutputStream().write(buff.asBlock().getBytes(), ofs.asLong(), len.asLong());
                context.leaveBlockingSection();
                return len;
            } else if (ds != null) {
                final DatagramPacket p = new DatagramPacket(buff.asBlock().getBytes(), ofs.asLong(), len.asLong(), ds.getRemoteSocketAddress());
                context.enterBlockingSection();
                ds.send(p);
                context.leaveBlockingSection();
                return len;
            } else {
                Unix.fail(ctxt, "send", Unix.INVALID_DESCRIPTOR_MSG);
                return Value.UNIT;
            }
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            Unix.fail(ctxt, "send", ioe);
            return Value.UNIT;
        }
    }
