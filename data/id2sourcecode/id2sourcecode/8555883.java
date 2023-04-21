    @Primitive
    public static Value unix_ftruncate_64(final CodeRunner ctxt, final Value fd, final Value len) throws Fail.Exception, FalseExit {
        try {
            final Channel ch = ctxt.getContext().getChannel(fd.asLong());
            if (ch == null) {
                Unix.fail(ctxt, "ftruncate_64", Unix.INVALID_DESCRIPTOR_MSG);
                return Value.UNIT;
            }
            final RandomAccessFile raf = ch.asStream();
            if (raf == null) {
                Unix.fail(ctxt, "ftruncate_64", Unix.INVALID_DESCRIPTOR_MSG);
                return Value.UNIT;
            }
            raf.setLength(len.asBlock().asInt64());
            return Value.UNIT;
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Unix.fail(ctxt, "ftruncate_64", ioe);
            return Value.UNIT;
        }
    }
