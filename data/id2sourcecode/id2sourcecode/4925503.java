    public static synchronized void enqueueSignal(final Signal signal) {
        assert signal != null : "null signal";
        if (signal.isValid()) {
            final int signum = signal.getNumber();
            final Set<Context> interested = Signals.INTERESTED.get(signum);
            if (interested != null) {
                for (Context ctxt : interested) {
                    if (!ctxt.isRuntimeBusy()) {
                        final Value closure = ctxt.getSignalHandler(signum);
                        final CodeRunner runner = ctxt.getMainCodeRunner();
                        if ((closure != null) && closure.isBlock() && (runner != null)) {
                            try {
                                runner.callback(closure, Value.createFromLong(Signals.systemToOCamlIdentifier(signum)));
                            } catch (final FalseExit fe) {
                                ctxt.setAsyncException(fe);
                                ctxt.getMainThread().interrupt();
                            } catch (final Fail.Exception fe) {
                                ctxt.setAsyncException(fe);
                                ctxt.getMainThread().interrupt();
                            } catch (final Fatal.Exception fe) {
                                final Channel ch = ctxt.getChannel(Channel.STDERR);
                                if ((ch != null) && (ch.asOutputStream() != null)) {
                                    final String msg = fe.getMessage();
                                    final PrintStream err = new PrintStream(ch.asOutputStream(), true);
                                    err.println("Error in signal handler: exception " + msg);
                                    err.close();
                                }
                            } catch (final CadmiumException ie) {
                                final Channel ch = ctxt.getChannel(Channel.STDERR);
                                if ((ch != null) && (ch.asOutputStream() != null)) {
                                    final String msg = ie.getMessage();
                                    final PrintStream err = new PrintStream(ch.asOutputStream(), true);
                                    err.println("Error in signal handler: exception " + msg);
                                    err.close();
                                }
                            }
                            return;
                        }
                    }
                }
            }
            Signals.pendingSignals.add(signal.getNumber());
            while (Signals.pendingSignals.size() > Signals.PENDING_SIGNALS_MAX_LENGTH) {
                Signals.pendingSignals.remove(0);
            }
            synchronized (Signals.pendingSignals) {
                Signals.pendingSignals.notifyAll();
            }
        }
    }
