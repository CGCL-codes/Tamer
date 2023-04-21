    @Primitive
    public static Value caml_sys_system_command(final CodeRunner ctxt, final Value cmd) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        final String command = cmd.asBlock().asString();
        final List<String> tokens = Misc.parseCommandLine(command);
        if ((tokens.size() > 0) && (tokens.get(0).charAt(0) == '(')) {
            final StringBuilder sb = new StringBuilder();
            for (String t : tokens) {
                sb.append(t);
                sb.append(" ");
            }
            tokens.clear();
            tokens.add("sh");
            tokens.add("-c");
            tokens.add(sb.toString());
        }
        String redir = (tokens.size() >= 2) ? tokens.get(tokens.size() - 2) : "";
        String redirIn = null;
        String redirOut = null;
        String redirErr = null;
        boolean redirOutAppend = false;
        while ((redir != null) && ((redir.equals(">") || redir.equals(">>") || redir.equals("2>") || redir.equals("<")))) {
            if (redir.equals(">")) {
                redirOut = tokens.remove(tokens.size() - 1);
                redirOutAppend = false;
            } else if (redir.equals(">>")) {
                redirOut = tokens.remove(tokens.size() - 1);
                redirOutAppend = true;
            } else if (redir.equals("2>")) {
                redirErr = tokens.remove(tokens.size() - 1);
            } else if (redir.equals("<")) {
                redirIn = tokens.remove(tokens.size() - 1);
            } else {
                assert false : "invalid case";
                tokens.remove(tokens.size() - 1);
            }
            tokens.remove(tokens.size() - 1);
            redir = (tokens.size() >= 2) ? tokens.get(tokens.size() - 2) : "";
        }
        final String tmpExecutable = tokens.size() >= 1 ? tokens.get(0) : null;
        final String executable = tmpExecutable != null && !tmpExecutable.contains("/") ? "/usr/local/bin/" + tmpExecutable : null;
        if ((context.getFileHook() != null) && (executable != null)) {
            final String resource = context.resourceNameFromPath(Value.createFromBlock(Block.createString(executable)));
            final InputStream is = context.getFileHook().getInputStream(resource);
            if (is != null) {
                try {
                    final RandomAccessInputStream rais = new RandomAccessInputStream(is);
                    final List<String> tokensCopy = new LinkedList<String>(tokens);
                    final int size = tokensCopy.size();
                    final String[] args = new String[size];
                    tokensCopy.toArray(args);
                    final InputStream in;
                    if (redirIn != null) {
                        final Value f = Value.createFromBlock(Block.createString(redirIn));
                        in = new FileInputStream(context.getRealFile(f));
                    } else {
                        final Channel parentIn = context.getChannel(Channel.STDIN);
                        in = parentIn != null ? parentIn.asInputStream() : System.in;
                    }
                    final OutputStream out;
                    if (redirOut != null) {
                        final Value f = Value.createFromBlock(Block.createString(redirOut));
                        out = new FileOutputStream(context.getRealFile(f), redirOutAppend);
                    } else {
                        final Channel parentOut = context.getChannel(Channel.STDOUT);
                        out = parentOut != null ? parentOut.asOutputStream() : System.out;
                    }
                    final OutputStream err;
                    if (redirErr != null) {
                        final Value f = Value.createFromBlock(Block.createString(redirErr));
                        err = new FileOutputStream(context.getRealFile(f));
                    } else {
                        final Channel parentErr = context.getChannel(Channel.STDERR);
                        err = parentErr != null ? parentErr.asOutputStream() : System.err;
                    }
                    final ByteCodeParameters params = new ByteCodeParameters(args, false, false, in, out instanceof PrintStream ? (PrintStream) out : new PrintStream(out, true), err instanceof PrintStream ? (PrintStream) err : new PrintStream(err, true), false, false, false, false, "Unix", true, executable, false, "fr.x9c.cadmium.primitives.stdlib.Sys", false, false, false, false, 64 * 1024, 64 * 1024, new String[0], true);
                    final Interpreter interp = new Interpreter(params, context.getPwd(), rais);
                    final Value exit = interp.execute();
                    return exit.isLong() ? exit : Value.MINUS_ONE;
                } catch (final Exception e) {
                    return Value.MINUS_ONE;
                }
            }
        }
        try {
            final ProcessBuilder pb = new ProcessBuilder(tokens);
            pb.directory(context.getPwd());
            context.enterBlockingSection();
            final Process p = pb.start();
            if (redirOut != null) {
                final Value f = Value.createFromBlock(Block.createString(redirOut));
                final Thread t = new StreamCopyThread(new FileOutputStream(context.getRealFile(f), redirOutAppend), p.getInputStream());
                t.start();
            } else {
                final Channel out = context.getChannel(Channel.STDOUT);
                if (out != null) {
                    final Thread t = new StreamCopyThread(out.asOutputStream(), p.getInputStream());
                    t.start();
                }
            }
            if (redirErr != null) {
                final Value f = Value.createFromBlock(Block.createString(redirErr));
                final Thread t = new StreamCopyThread(new FileOutputStream(context.getRealFile(f)), p.getErrorStream());
                t.start();
            } else {
                final Channel err = context.getChannel(Channel.STDERR);
                if (err != null) {
                    final Thread t = new StreamCopyThread(err.asOutputStream(), p.getErrorStream());
                    t.start();
                }
            }
            if (redirIn != null) {
                final Value f = Value.createFromBlock(Block.createString(redirIn));
                final Thread t = new StreamCopyThread(p.getOutputStream(), new FileInputStream(context.getRealFile(f)));
                t.start();
            } else {
                final Channel in = context.getChannel(Channel.STDIN);
                if (in != null) {
                    final Thread t = new StreamCopyThread(p.getOutputStream(), in.asInputStream());
                    t.start();
                }
            }
            final int res = p.waitFor();
            context.leaveBlockingSection();
            return Value.createFromLong(res);
        } catch (final SecurityException se) {
            context.leaveBlockingSection();
            sysError(command, se.toString());
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            sysError(command, ioe.toString());
        } catch (final IllegalArgumentException iae) {
            sysError(command, iae.toString());
        } catch (final InterruptedException ie) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        }
        return Value.UNIT;
    }
