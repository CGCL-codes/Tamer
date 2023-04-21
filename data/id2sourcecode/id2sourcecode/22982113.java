    @Primitive
    public static Value unix_pipe(final CodeRunner ctxt, final Value unit) throws Fail.Exception, FalseExit {
        try {
            final PipedInputStream readStream = new PipedInputStream();
            final PipedOutputStream writeStream = new PipedOutputStream(readStream);
            final Channel readChannel = new Channel(readStream);
            final Channel writeChannel = new Channel(writeStream);
            final Context c = ctxt.getContext();
            final Block res = Block.createBlock(0, Value.createFromLong(c.addChannel(readChannel)), Value.createFromLong(c.addChannel(writeChannel)));
            return Value.createFromBlock(res);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Unix.fail(ctxt, "pipe", ioe);
            return Value.UNIT;
        }
    }
