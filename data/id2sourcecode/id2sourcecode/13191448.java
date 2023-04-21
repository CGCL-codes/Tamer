        @Override
        public void run() {
            do {
                this.start = System.currentTimeMillis();
                try {
                    Thread.sleep(this.millis);
                } catch (final InterruptedException ie) {
                    return;
                }
                if (this.activated) {
                    Signals.enqueueSignal(new Signal(this.signal));
                    try {
                        Signals.processSignal(this.codeRunner);
                    } catch (final FalseExit fe) {
                        this.context.setAsyncException(fe);
                    } catch (final Fail.Exception fe) {
                        final Channel ch = this.context.getChannel(Channel.STDERR);
                        if ((ch != null) && (ch.asOutputStream() != null)) {
                            final String msg = Misc.convertException(fe.asValue(this.context.getGlobalData()));
                            final PrintStream err = new PrintStream(ch.asOutputStream(), true);
                            err.println("Error in signal handler: exception " + msg);
                            err.close();
                        }
                    } catch (final Fatal.Exception fe) {
                        final Channel ch = this.context.getChannel(Channel.STDERR);
                        if ((ch != null) && (ch.asOutputStream() != null)) {
                            final PrintStream err = new PrintStream(ch.asOutputStream(), true);
                            err.println("Error in signal handler: exception " + fe.getMessage());
                            err.close();
                        }
                    } catch (final CadmiumException ie) {
                        final Channel ch = this.context.getChannel(Channel.STDERR);
                        if ((ch != null) && (ch.asOutputStream() != null)) {
                            final PrintStream err = new PrintStream(ch.asOutputStream(), true);
                            err.println("Error in signal handler: exception " + ie.getMessage());
                            err.close();
                        }
                    }
                }
                this.millis = this.interval;
            } while (this.millis > 0);
        }
