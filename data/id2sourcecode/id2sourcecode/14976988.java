    public final void execute() {
        this.context.setMainCodeRunner(this);
        setup(null);
        final CadmiumThread thread = new CadmiumThread(this.context.getThreadGroup(), this);
        this.context.setMainThread(thread);
        thread.start();
        while (thread.isAlive()) {
            try {
                thread.join();
            } catch (final InterruptedException ie) {
                return;
            }
        }
        Signals.unregisterContext(this.context);
        this.context.clearSignals();
        if ((this.exception != null) && !(this.exception instanceof FalseExit)) {
            final Channel ch = this.context.getChannel(Channel.STDERR);
            final PrintStream err;
            final ByteArrayOutputStream altErr;
            if ((ch != null) && (ch.asOutputStream() != null)) {
                altErr = null;
                err = new PrintStream(ch.asOutputStream(), true);
            } else {
                altErr = new ByteArrayOutputStream();
                err = new PrintStream(altErr, true);
            }
            final boolean backtrace = this.context.isBacktraceActive();
            this.context.setBacktraceActive(false);
            final Value atExit = this.context.getCallback("Pervasives.do_at_exit");
            if (atExit != null) {
                try {
                    callback(atExit, Value.UNIT);
                } catch (final Throwable t) {
                }
            }
            this.context.setBacktraceActive(backtrace);
            if (this.exception instanceof Fail.Exception) {
                final String msg = Misc.convertException(((Fail.Exception) this.exception).asValue(this));
                err.println("Fatal error: exception " + msg);
                if (this.context.isBacktraceActive()) {
                    printExceptionBacktrace(err);
                }
                err.close();
                return;
            } else if (this.exception instanceof Fatal.Exception) {
                err.println(((Fatal.Exception) this.exception).getMessage());
                err.close();
                return;
            } else {
                err.println(this.exception.toString());
                err.close();
                return;
            }
        } else {
            final boolean backtrace = this.context.isBacktraceActive();
            this.context.setBacktraceActive(false);
            final Value atExit = this.context.getCallback("Pervasives.do_at_exit");
            if (atExit != null) {
                try {
                    callback(atExit, Value.UNIT);
                } catch (final Throwable t) {
                }
            }
            this.context.setBacktraceActive(backtrace);
        }
    }
