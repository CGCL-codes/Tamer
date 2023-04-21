    private static final int testIMAP4Access(final PrintStream out, final BufferedReader in, final String url, final String user, final String pass) {
        final int pp = url.lastIndexOf(':');
        final String host = (pp < 0) ? url : url.substring(0, pp);
        final int port = (pp < 0) ? IMAP4Protocol.IPPORT_IMAP4 : Integer.parseInt(url.substring(pp + 1));
        final NetServerWelcomeLine wl = new NetServerWelcomeLine();
        for (; ; ) {
            final String ans = getval(out, in, "(re-)run test ([y]/n)");
            if ((ans != null) && (ans.length() > 0) && (Character.toUpperCase(ans.charAt(0)) != 'Y')) break;
            final IMAP4Session sess = new IMAP4Session();
            try {
                sess.setReadTimeout(30 * 1000);
                {
                    final long cStart = System.currentTimeMillis();
                    sess.connect(host, port, wl);
                    final long cEnd = System.currentTimeMillis(), cDuration = cEnd - cStart;
                    out.println("Connected to " + host + " on port " + port + " in " + cDuration + " msec.: " + wl);
                }
                {
                    final Map.Entry<String, String> ident = IMAP4ServerIdentityAnalyzer.DEFAULT.getServerIdentity(wl.getLine());
                    if (null == ident) System.err.println("Failed to identify server"); else out.println("\tType=" + ident.getKey() + "/Version=" + ident.getValue());
                }
                boolean hasQuota = false, hasNamespace = false;
                {
                    final long capStart = System.currentTimeMillis();
                    final IMAP4Capabilities rsp = sess.capability();
                    final long capEnd = System.currentTimeMillis(), capDuration = (capEnd - capStart);
                    if (rsp.isOKResponse()) {
                        final Collection<String> caps = rsp.getCapabilities();
                        final int numCaps = (null == caps) ? 0 : caps.size();
                        out.println("got " + numCaps + " capabilities in " + capDuration + "msec.: " + rsp);
                        if (numCaps > 0) {
                            out.println(IMAP4Protocol.IMAP4CapabilityCmd);
                            for (final String c : caps) out.println("\t" + c);
                        }
                        hasQuota = rsp.hasQuota();
                        hasNamespace = rsp.hasNamespace();
                    } else System.err.println("Failed to get CAPABILITY after " + capDuration + " msec.: " + rsp);
                }
                {
                    final long aStart = System.currentTimeMillis();
                    final IMAP4TaggedResponse rsp = sess.login(user, pass);
                    final long aEnd = System.currentTimeMillis(), aDuration = aEnd - aStart;
                    if (!rsp.isOKResponse()) {
                        System.err.println("Authentication failed in " + aDuration + " msec.: " + rsp);
                        continue;
                    }
                    out.println("Authenticated in " + aDuration + " msec.: " + rsp);
                }
                if (hasQuota) {
                    final long qtStart = System.currentTimeMillis();
                    final IMAP4QuotarootInfo qtInfo = sess.getquotaroot();
                    final long qtEnd = System.currentTimeMillis(), qtDuration = (qtEnd - qtStart);
                    if (qtInfo.isOKResponse()) {
                        out.println("Got response in " + qtDuration + " msec.: " + qtInfo);
                        out.println(IMAP4Protocol.IMAP4GetQuotaRootCmd);
                        out.println("\t" + IMAP4Protocol.IMAP4QuotaStorageRes + ": " + qtInfo.getCurStorageKB() + " out of " + qtInfo.getMaxStorageKB());
                        out.println("\t" + IMAP4Protocol.IMAP4QuotaMessageRes + ": " + qtInfo.getCurMessages() + " out of " + qtInfo.getMaxMessages());
                    } else System.err.println("Failed to get QUOTAROOT (after " + qtDuration + " msec.) : " + qtInfo);
                }
                if (hasNamespace) {
                    final long nsStart = System.currentTimeMillis();
                    final IMAP4NamespacesInfo nsInfo = sess.namespace();
                    final long nsEnd = System.currentTimeMillis(), nsDuration = (nsEnd - nsStart);
                    if (nsInfo.isOKResponse()) {
                        out.println("Got response in " + nsDuration + " msec.: " + nsInfo);
                        out.println(IMAP4Protocol.IMAP4NamespaceCmd);
                        showNamespace(out, "Pesonal", nsInfo.getPersonal());
                        showNamespace(out, "Shared", nsInfo.getShared());
                        showNamespace(out, "Other", nsInfo.getOther());
                    } else System.err.println("Failed to get NAMESPACE (after " + nsDuration + " msec.) : " + nsInfo);
                }
                testIMAP4Access(out, in, sess);
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
