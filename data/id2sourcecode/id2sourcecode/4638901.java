    @Primitive
    public static Value unix_lseek(final CodeRunner ctxt, final Value fd, final Value ofs, final Value cmd) throws Fail.Exception, FalseExit {
        try {
            final Channel ch = ctxt.getContext().getChannel(fd.asLong());
            if (ch == null) {
                Unix.fail(ctxt, "lseek", Unix.INVALID_DESCRIPTOR_MSG);
                return Value.UNIT;
            }
            final long res = ch.seek(ofs.asLong(), cmd.asLong());
            return Value.createFromLong((int) res);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Unix.fail(ctxt, "lseek", ioe);
            return Value.UNIT;
        }
    }
