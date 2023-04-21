    @Primitive
    public static Value unix_lseek_64(final CodeRunner ctxt, final Value fd, final Value ofs, final Value cmd) throws Fail.Exception, FalseExit {
        try {
            final Channel ch = ctxt.getContext().getChannel(fd.asLong());
            if (ch == null) {
                Unix.fail(ctxt, "lseek_64", Unix.INVALID_DESCRIPTOR_MSG);
                return Value.UNIT;
            }
            final long res = ch.seek(ofs.asBlock().asInt64(), cmd.asLong());
            final Block b = Block.createCustom(Custom.INT_64_SIZE, Custom.INT_64_OPS);
            b.setInt64(res);
            return Value.createFromBlock(b);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Unix.fail(ctxt, "lseek_64", ioe);
            return Value.UNIT;
        }
    }
