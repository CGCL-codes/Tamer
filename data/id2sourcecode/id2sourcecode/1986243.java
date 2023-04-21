    @Primitive
    public static Value unix_sendto(final CodeRunner ctxt, final Value socket, final Value buff, final Value ofs, final Value len, final Value flags, final Value dest) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        final Block destBlock = dest.asBlock();
        DatagramPacket packet;
        try {
            final SocketAddress addr = new InetSocketAddress(InetAddress.getByAddress(destBlock.get(0).asBlock().getBytes()), destBlock.get(1).asLong());
            packet = new DatagramPacket(buff.asBlock().getBytes(), ofs.asLong(), len.asLong(), addr);
        } catch (final IOException ioe) {
            Unix.fail(ctxt, "sendto", ioe);
            return Value.UNIT;
        }
        final Channel ch = context.getChannel(socket.asLong());
        if (ch == null) {
            Unix.fail(ctxt, "sendto", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        final DatagramSocket ds = ch.asDatagramSocket();
        if (ds == null) {
            Unix.fail(ctxt, "sendto", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        context.enterBlockingSection();
        try {
            ds.send(packet);
            context.leaveBlockingSection();
            return len;
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            Unix.fail(ctxt, "sendto", ioe);
            return Value.UNIT;
        }
    }
