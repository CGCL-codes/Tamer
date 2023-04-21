    private static final int testPOP3Access(final PrintStream out, final BufferedReader in, final String url, final String user, final String pass) {
        final int pp = url.lastIndexOf(':');
        final String host = (pp < 0) ? url : url.substring(0, pp);
        final int port = (pp < 0) ? POP3Protocol.IPPORT_POP3 : Integer.parseInt(url.substring(pp + 1));
        final NetServerWelcomeLine wl = new NetServerWelcomeLine();
        for (; ; ) {
            final String ans = getval(out, in, "(re-)run test ([y]/n)");
            if ((ans != null) && (ans.length() > 0) && (Character.toUpperCase(ans.charAt(0)) != 'Y')) break;
            final POP3Accessor sess = new POP3Session();
            try {
                sess.setReadTimeout(30 * 1000);
                {
                    final long cStart = System.currentTimeMillis();
                    sess.connect(host, port, wl);
                    final long cEnd = System.currentTimeMillis(), cDuration = cEnd - cStart;
                    out.println("Connected to " + host + " on port " + port + " in " + cDuration + " msec.: " + wl);
                }
                {
                    final Map.Entry<String, String> ident = POP3ServerIdentityAnalyzer.DEFAULT.getServerIdentity(wl.getLine());
                    if (null == ident) System.err.println("Failed to identify server"); else out.println("\tType=" + ident.getKey() + "/Version=" + ident.getValue());
                }
                {
                    final long aStart = System.currentTimeMillis();
                    final POP3Response rsp = sess.login(user, pass);
                    final long aEnd = System.currentTimeMillis(), aDuration = aEnd - aStart;
                    if (!rsp.isOKResponse()) {
                        System.err.println("Authentication failed in " + aDuration + " msec.: " + rsp);
                        continue;
                    }
                    out.println("Authenticated in " + aDuration + " msec.: " + rsp);
                }
                testPOP3Access(out, in, sess);
            } catch (IOException ce) {
                System.err.println(ce.getClass().getName() + " on handle session: " + ce.getMessage());
            } finally {
                try {
                    sess.close();
                } catch (IOException ce) {
                    System.err.println(ce.getClass().getName() + " on close session: " + ce.getMessage());
                }
            }
        }
        return 0;
    }
