    @Primitive
    public static Value caml_thread_uncaught_exception(final CodeRunner ctxt, final Value exn) throws FalseExit, Fail.Exception {
        final CodeRunner cr = ((CadmiumThread) Thread.currentThread()).getRunner();
        final Channel err = ctxt.getContext().getChannel(Channel.STDERR);
        if ((err != null) && (err.asOutputStream() != null)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            sb.append(cr.getThreadStatus().asBlock().get(Threads.IDENT).asLong());
            sb.append(" killed on uncaught exception ");
            sb.append(fr.x9c.cadmium.kernel.Misc.convertException(exn));
            sb.append('\n');
            try {
                err.asOutputStream().write(Misc.convertStringToBytes(sb.toString()));
            } catch (final InterruptedIOException iioe) {
                final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
                fe.fillInStackTrace();
                throw fe;
            } catch (final IOException ioe) {
            }
            if (ctxt.getContext().isBacktraceActive()) {
                ctxt.printExceptionBacktrace(new PrintStream(err.asOutputStream(), true));
            }
        }
        return Value.UNIT;
    }
