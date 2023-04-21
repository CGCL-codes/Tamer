    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (this.custom != null) {
            this.custom.finalize(Value.createFromBlock(this));
        }
        if (this.finalizers != null) {
            final Context ctxt = this.codeRunner.getContext();
            final List<Value> finals = new LinkedList<Value>(this.finalizers);
            this.finalizers = null;
            for (Value closure : finals) {
                try {
                    ctxt.acquireFinalizeLock();
                } catch (final Fail.Exception fe) {
                    return;
                } catch (final FalseExit fe) {
                    return;
                }
                try {
                    this.codeRunner.callback(closure, Value.createFromBlock(duplicate()));
                } catch (final Fail.Exception fe) {
                    final Channel ch = ctxt.getChannel(Channel.STDERR);
                    if ((ch != null) && (ch.asOutputStream() != null)) {
                        final String msg = fr.x9c.cadmium.kernel.Misc.convertException(fe.asValue(ctxt.getGlobalData()));
                        final PrintStream err = new PrintStream(ch.asOutputStream(), true);
                        err.println("Error in finalizer: exception " + msg);
                        err.close();
                    }
                } catch (final Fatal.Exception fe) {
                    final Channel ch = ctxt.getChannel(Channel.STDERR);
                    if ((ch != null) && (ch.asOutputStream() != null)) {
                        final PrintStream err = new PrintStream(ch.asOutputStream(), true);
                        err.println("Error in finalizer: exception " + fe.getMessage());
                        err.close();
                    }
                } catch (final CadmiumException ie) {
                    final Channel ch = ctxt.getChannel(Channel.STDERR);
                    if ((ch != null) && (ch.asOutputStream() != null)) {
                        final PrintStream err = new PrintStream(ch.asOutputStream(), true);
                        err.println("Error in finalizer: exception " + ie.getMessage());
                        err.close();
                    }
                } finally {
                    ctxt.releaseFinalizeLock();
                }
            }
        }
    }
