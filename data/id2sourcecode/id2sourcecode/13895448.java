    @Primitive
    public static Value unix_select(final CodeRunner ctxt, final Value readfds, final Value writefds, final Value exceptfds, final Value timeout) throws FalseExit, Fail.Exception {
        final Context context = ctxt.getContext();
        if ((readfds == Value.EMPTY_LIST) && (writefds == Value.EMPTY_LIST) && (exceptfds == Value.EMPTY_LIST)) {
            final double t = timeout.asBlock().asDouble();
            if (t > 0.0) {
                context.enterBlockingSection();
                try {
                    Thread.sleep((long) (t * Unix.MILLISECS_PER_SEC));
                } catch (final InterruptedException ie) {
                    final FalseExit fe = FalseExit.createFromContext(context);
                    fe.fillInStackTrace();
                    throw fe;
                }
                context.leaveBlockingSection();
            }
        }
        final Block res = Block.createBlock(0, readfds, writefds, exceptfds);
        return Value.createFromBlock(res);
    }
