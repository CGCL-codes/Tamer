        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            final String methodName = this.translation != null ? this.translation.get(method) : method.getName();
            if (methodName != null) {
                try {
                    final Context ctxt = this.codeRunner.getContext();
                    final Value encoder = ctxt.getCallback(Proxies.ENCODE_VALUE);
                    final Value decoder = ctxt.getCallback(Proxies.DECODE_VALUE);
                    final Value h = Hash.hashVariant(methodName);
                    final Value meth = AbstractCodeRunner.getMethod(this.object, h.getRawValue());
                    final int len = args != null ? args.length : 0;
                    final Value[] params = new Value[len + 1];
                    params[0] = this.object;
                    for (int i = 0; i < len; i++) {
                        params[i + 1] = this.codeRunner.callback(encoder, Cadmium.encodeObject(args[i]));
                    }
                    final Value rawResult = this.codeRunner.callback(meth, params);
                    final Value decodedResult = this.codeRunner.callback(decoder, rawResult);
                    return Cadmium.decodeObject(decodedResult);
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
