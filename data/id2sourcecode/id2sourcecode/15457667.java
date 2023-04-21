    public static DS login(String host, int port, final String loginId, final String password, final Session ses, final PluginContext ctx) throws IOException, InterruptedException, MessagingNetworkException {
        final PluginContext pctx = ctx;
        DS ds = null;
        boolean authSuccess = false;
        StringTokenizer r = null;
        int t;
        String sp = null;
        if (password == null) throw new AssertException("password is null");
        if (password.indexOf(' ') != -1 || password.indexOf('\t') != -1 || password.indexOf('\r') != -1 || password.indexOf('\n') != -1) {
            throw new MessagingNetworkException("MSN password cannot contain whitespace", MessagingNetworkException.CATEGORY_NOT_CATEGORIZED, MessagingNetworkException.ENDUSER_CANNOT_LOGIN_WRONG_PASSWORD);
        }
        long plannedAbortTime = System.currentTimeMillis() + 1000 * MSNMessagingNetwork.REQPARAM_SOCKET_TIMEOUT_SECONDS;
        if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("connecting to DS " + host + ":" + port);
        for (; ; ) {
            if (Thread.currentThread().isInterrupted()) throw new InterruptedException();
            ds = new DS(host, port, ctx);
            ses.add(ds);
            ses.setDS(ds);
            ses.setRunning(true);
            ds.start(new Transaction() {

                final String CMD1 = "VER";

                public void clientRequest(TransactionContext ctx) throws IOException, InterruptedException, MessagingNetworkException {
                    post(CMD1, PROTOCOL_VERSIONS_STRING, ctx);
                }

                public void serverResponse(String cmd, String args, TransactionContext ctx, final Session ses, final PluginContext pctx) throws IOException, InterruptedException, MessagingNetworkException {
                    StringTokenizer r = new StringTokenizer(args);
                    if (!cmd.equals(CMD1)) throwProtocolViolated("cmd must be " + CMD1);
                    while (r.hasMoreTokens()) {
                        String dialect = r.nextToken();
                        if ("0".equals(dialect)) throwProtocolViolated("DS server reported version negotiation failure");
                    }
                    finish();
                }
            }).waitFor(true, ses, pctx);
            ds.start(new Transaction() {

                final String CMD1 = "INF";

                public void clientRequest(TransactionContext ctx) throws IOException, InterruptedException, MessagingNetworkException {
                    post(CMD1, "", ctx);
                }

                public void serverResponse(String cmd, String args, TransactionContext ctx, final Session ses, final PluginContext pctx) throws IOException, InterruptedException, MessagingNetworkException {
                    StringTokenizer r = new StringTokenizer(args);
                    if (!cmd.equals(CMD1)) throwProtocolViolated("cmd must be " + CMD1);
                    r = new StringTokenizer(tok(r), ", \t");
                    if (!r.hasMoreElements()) throwProtocolViolated("DS server did not report server security policy id; cannot proceed");
                    boolean md5found = false;
                    while (r.hasMoreElements()) {
                        String sp = r.nextToken();
                        if ("MD5".equals(sp)) {
                            md5found = true;
                            break;
                        }
                    }
                    if (!md5found) throwProtocolViolated("DS server reported no known security packages; cannot proceed");
                    finish();
                }
            }).waitFor(true, ses, pctx);
            if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("sending login id");
            USRLoginTransaction usrt = new USRLoginTransaction(loginId);
            ds.start(usrt).waitFor(true, ses, pctx);
            r = usrt.stringTokenizer;
            if (usrt.isUSR) break;
            ds.close("changing NS server", MessagingNetworkException.CATEGORY_NOT_CATEGORIZED, MessagingNetworkException.ENDUSER_NO_ERROR);
            ds = null;
            if (System.currentTimeMillis() > plannedAbortTime) throw new MessagingNetworkException("login timed out", MessagingNetworkException.CATEGORY_NOT_CATEGORIZED, MessagingNetworkException.ENDUSER_LOGGED_OFF_DUE_TO_MESSAGING_OPERATION_TIMEOUT);
            HostPort hp = parseReferral(r, "NS");
            host = hp.host;
            port = hp.port;
            if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("referred to " + host + ":" + port);
        }
        if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("handshaking");
        tok(r);
        tok(r);
        String challenge = tok(r);
        String response;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] ba = md.digest((challenge + password).getBytes("ASCII"));
            md = null;
            response = byteArrayToHexString(ba);
            ba = null;
        } catch (java.security.NoSuchAlgorithmException ex) {
            throw new AssertException("MSN plugin reports: MD5 security provider is not installed, see java.security.MessageDigest class. MD5 provider is present in JDK1.3.1+.");
        }
        final String response_ = response;
        ds.start(new Transaction() {

            public void clientRequest(TransactionContext ctx) throws IOException, InterruptedException, MessagingNetworkException {
                post("USR", "MD5 S " + response_, ctx);
            }

            public void serverResponse(String cmd, String args, TransactionContext ctx, final Session ses, final PluginContext pctx) throws IOException, InterruptedException, MessagingNetworkException {
                StringTokenizer r = new StringTokenizer(args);
                if (!cmd.equals("USR")) throwProtocolViolated("cmd must be USR");
                finish();
            }
        }).waitFor(true, ses, pctx);
        if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("auth success");
        authSuccess = true;
        return ds;
    }
