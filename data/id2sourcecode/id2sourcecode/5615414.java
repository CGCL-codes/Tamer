        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            final Value v = this.bindings.get(method);
            if (v != null) {
                try {
                    final int len = args != null ? args.length : 0;
                    final Value[] params = new Value[len];
                    for (int i = 0; i < len; i++) {
                        params[i] = Cadmium.encodeObject(args[i]);
                    }
                    final Value res = this.codeRunner.callback(v, Cadmium.createList(params));
                    return Cadmium.decodeObject(res);
                } catch (final Fail.Exception fe) {
                    final Context ctxt = this.codeRunner.getContext();
                    final Value exn = fe.asValue(ctxt.getGlobalData());
                    final Value javaExn = ctxt.getCallback("Cadmium.Java_exception");
                    try {
                        if ((javaExn != null) && exn.isBlock() && (exn.asBlock().sizeValues() >= 1) && (Compare.caml_equal(this.codeRunner, exn.asBlock().get(0), javaExn) == Value.TRUE)) {
                            final Object obj = exn.asBlock().get(1).asBlock().get(2).asBlock().asCustom();
                            ((Throwable) obj).fillInStackTrace();
                            if (obj instanceof Error) {
                                throw (Error) obj;
                            } else if (obj instanceof RuntimeException) {
                                throw (RuntimeException) obj;
                            }
                        } else {
                            final Channel ch = ctxt.getChannel(Channel.STDERR);
                            if ((ch != null) && (ch.asOutputStream() != null)) {
                                final String msg = Misc.convertException(fe.asValue(ctxt.getGlobalData()));
                                final PrintStream err = new PrintStream(ch.asOutputStream(), true);
                                err.println("Error in proxy: exception " + msg);
                                err.close();
                            }
                        }
                    } catch (final Fail.Exception fe2) {
                        final Channel ch = ctxt.getChannel(Channel.STDERR);
                        if ((ch != null) && (ch.asOutputStream() != null)) {
                            final String msg = Misc.convertException(fe2.asValue(ctxt.getGlobalData()));
                            final PrintStream err = new PrintStream(ch.asOutputStream(), true);
                            err.println("Error in proxy: exception " + msg);
                            err.close();
                        }
                    }
                } catch (final Fatal.Exception fe) {
                    final Context ctxt = this.codeRunner.getContext();
                    final Channel ch = ctxt.getChannel(Channel.STDERR);
                    if ((ch != null) && (ch.asOutputStream() != null)) {
                        final String msg = fe.getMessage();
                        final PrintStream err = new PrintStream(ch.asOutputStream(), true);
                        err.println("Error in proxy: exception " + msg);
                        err.close();
                    }
                }
                return null;
            } else {
                return null;
            }
        }
