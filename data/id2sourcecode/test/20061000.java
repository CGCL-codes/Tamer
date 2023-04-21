    @Primitive
    public static Value unix_execve(final CodeRunner ctxt, final Value path, final Value args, final Value env) throws Fail.Exception, Fatal.Exception, FalseExit {
        final Context context = ctxt.getContext();
        final int sz = args.asBlock().getWoSize();
        final String[] a = new String[sz];
        for (int i = 0; i < sz; i++) {
            a[i] = args.asBlock().get(i).asBlock().asString();
        }
        a[0] = context.getRealFile(path).getAbsolutePath();
        final int sze = env.asBlock().getWoSize();
        final String[] e = new String[sze];
        for (int i = 0; i < sze; i++) {
            e[i] = env.asBlock().get(i).asBlock().asString();
        }
        try {
            final ProcessBuilder pb = new ProcessBuilder(Misc.prepareArguments(a));
            pb.directory(context.getPwd());
            final Process p = pb.start();
            final Channel out = context.getChannel(Channel.STDOUT);
            if (out != null) {
                final Thread t = new StreamCopyThread(out.asOutputStream(), p.getInputStream());
                t.start();
            }
            final Channel err = context.getChannel(Channel.STDERR);
            if (err != null) {
                final Thread t = new StreamCopyThread(err.asOutputStream(), p.getErrorStream());
                t.start();
            }
            final Channel in = context.getChannel(Channel.STDIN);
            if (in != null) {
                final Thread t = new StreamCopyThread(p.getOutputStream(), in.asInputStream());
                t.start();
            }
            return Sys.caml_sys_exit(ctxt, Value.createFromLong(p.waitFor()));
        } catch (final FalseExit sfe) {
            throw sfe;
        } catch (final InterruptedException ie) {
            Unix.fail(ctxt, "execv", ie);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Unix.fail(ctxt, "execv", ioe);
        }
        return Value.UNIT;
    }
