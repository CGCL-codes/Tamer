    @Primitive
    public static Value unix_dup2(final CodeRunner ctxt, final Value fd1, final Value fd2) throws Fail.Exception, FalseExit {
        final Context c = ctxt.getContext();
        final Channel old = c.removeChannel(fd2.asLong());
        if (old != null) {
            try {
                old.setFD(-1);
                old.close();
            } catch (final InterruptedIOException iioe) {
                final FalseExit fe = FalseExit.createFromContext(c);
                fe.fillInStackTrace();
                throw fe;
            } catch (final IOException ioe) {
                Unix.fail(ctxt, "dup2", ioe);
            }
        }
        final Channel ch = c.getChannel(fd1.asLong());
        if (ch == null) {
            Unix.fail(ctxt, "dup2", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        c.setChannel(fd2.asLong(), ch);
        return Value.UNIT;
    }
