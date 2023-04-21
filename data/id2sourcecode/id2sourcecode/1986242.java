    @Primitive
    public static Value unix_recvfrom(final CodeRunner ctxt, final Value socket, final Value buff, final Value ofs, final Value len, final Value flags) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        final Channel ch = context.getChannel(socket.asLong());
        if (ch == null) {
            Unix.fail(ctxt, "recvfrom", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        final DatagramSocket ds = ch.asDatagramSocket();
        if (ds == null) {
            Unix.fail(ctxt, "recvfrom", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        final DatagramPacket packet = new DatagramPacket(buff.asBlock().getBytes(), ofs.asLong(), len.asLong());
        try {
            context.enterBlockingSection();
            ds.receive(packet);
            context.leaveBlockingSection();
            final Block res = Block.createBlock(0, Value.createFromLong(packet.getLength()), Unix.createSockAddr(ctxt, (InetSocketAddress) packet.getSocketAddress()));
            return Value.createFromBlock(res);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            Unix.fail(ctxt, "recvfrom", ioe);
            return Value.UNIT;
        }
    }
