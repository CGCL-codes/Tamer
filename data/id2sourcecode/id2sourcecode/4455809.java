    private Value runMain(final Value closure, final Map<String, Value> bindings, final Value... params) throws CadmiumException {
        final ByteCodeRunner runner = new ByteCodeRunner(this, null, true);
        runner.setBindings(bindings);
        this.context.setMainCodeRunner(runner);
        runner.setup(closure, params);
        final CadmiumThread thread = new CadmiumThread(this.context.getThreadGroup(), runner);
        this.context.setMainThread(thread);
        thread.start();
        while (thread.isAlive()) {
            try {
                thread.join();
            } catch (final InterruptedException ie) {
                Signals.unregisterContext(this.context);
                this.context.clearSignals();
                try {
                    final int exitCode = FalseExit.createFromContext(this.context).getExitCode();
                    return Value.createFromLong(exitCode);
                } catch (final Fail.Exception fe) {
                }
            }
        }
        Signals.unregisterContext(this.context);
        this.context.clearSignals();
        final Throwable exn = runner.getException();
        if (exn == null) {
            try {
                Debugger.handleEvent(runner, Debugger.EventKind.PROGRAM_EXIT);
            } catch (final FalseExit fe) {
                throw new CadmiumException("error during debugger event handling", fe);
            } catch (final Fail.Exception fe) {
                throw new CadmiumException("error during debugger event handling", fe);
            } catch (final Fatal.Exception fe) {
                throw new CadmiumException("error during debugger event handling", fe);
            }
            return runner.getResult();
        } else {
            try {
                Debugger.handleEvent(runner, Debugger.EventKind.UNCAUGHT_EXC);
            } catch (final FalseExit fe) {
                throw new CadmiumException("error during debugger event handling", fe);
            } catch (final Fail.Exception fe) {
                throw new CadmiumException("error during debugger event handling", fe);
            } catch (final Fatal.Exception fe) {
                throw new CadmiumException("error during debugger event handling", fe);
            }
            if (closure != null) {
                throw new CadmiumException("callback exception", exn);
            }
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
                    runner.callback(atExit, Value.UNIT);
                } catch (final Exception e) {
                }
            }
            this.context.setBacktraceActive(backtrace);
            if (exn instanceof Fail.Exception) {
                final String msg = Misc.convertException(((Fail.Exception) exn).asValue(this.context.getGlobalData()));
                err.println("Fatal error: exception " + msg);
                if (this.context.isBacktraceActive() && !this.context.isDebuggerInUse()) {
                    runner.printExceptionBacktrace(err);
                }
                err.close();
                throw new CadmiumException(altErr != null ? altErr.toString() : "program exception", exn);
            } else if (exn instanceof Fatal.Exception) {
                err.println(((Fatal.Exception) exn).getMessage());
                err.close();
                throw new CadmiumException(altErr != null ? altErr.toString() : "fatal error", exn);
            } else {
                err.println(exn.toString());
                err.close();
                throw new CadmiumException(altErr != null ? altErr.toString() : "internal error", exn);
            }
        }
    }
