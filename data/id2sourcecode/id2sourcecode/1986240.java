    @Primitive
    public static Value unix_recv(final CodeRunner ctxt, final Value socket, final Value buff, final Value ofs, final Value len, final Value flags) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        try {
            final Channel ch = context.getChannel(socket.asLong());
            if (ch == null) {
                Unix.fail(ctxt, "recv", Unix.INVALID_DESCRIPTOR_MSG);
                return Value.UNIT;
            }
            final java.net.Socket s = ch.asSocket();
            final DatagramSocket ds = ch.asDatagramSocket();
            if (s != null) {
                context.enterBlockingSection();
                final int res = s.getInputStream().read(buff.asBlock().getBytes(), ofs.asLong(), len.asLong());
                context.leaveBlockingSection();
                return Value.createFromLong(res);
            } else if (ds != null) {
                final DatagramPacket p = new DatagramPacket(buff.asBlock().getBytes(), ofs.asLong(), len.asLong());
                context.enterBlockingSection();
                ds.receive(p);
                context.leaveBlockingSection();
                return Value.createFromLong(p.getLength());
            } else {
                Unix.fail(ctxt, "recv", Unix.INVALID_DESCRIPTOR_MSG);
                return Value.UNIT;
            }
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            Unix.fail(ctxt, "recv", ioe);
            return Value.UNIT;
        }
    }
