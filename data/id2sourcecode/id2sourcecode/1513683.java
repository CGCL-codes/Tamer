    @Primitive
    public static Value unix_write(final CodeRunner ctxt, final Value fd, final Value buf, final Value vofs, final Value vlen) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        final Channel ch = context.getChannel(fd.asLong());
        if (ch == null) {
            Unix.fail(ctxt, "write", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        final int ofs = vofs.asLong();
        final int len = vlen.asLong();
        final byte[] buff = buf.asBlock().getBytes();
        context.enterBlockingSection();
        try {
            ch.asDataOutput().write(buff, ofs, len);
            context.leaveBlockingSection();
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            Unix.fail(ctxt, "write", ioe);
        }
        return vlen;
    }
