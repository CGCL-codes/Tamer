    private String generateResponse(String userName, String password, String digestEntityBody) {
        if (userName == null) {
            if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                Logging.report(Logging.ERROR, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(): " + "ERROR: no userName parameter");
            }
            return null;
        }
        if (realm == null) {
            if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                Logging.report(Logging.ERROR, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(): " + "ERROR: no realm parameter");
            }
            return null;
        }
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(): " + "Trying to generate a response " + "for the user: " + userName + " , with " + "the realm: " + realm);
        }
        if (password == null) {
            if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                Logging.report(Logging.ERROR, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(): " + "ERROR: no password parameter");
            }
            return null;
        }
        if (method == null) {
            if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                Logging.report(Logging.ERROR, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(): " + "ERROR: no method parameter");
            }
            return null;
        }
        if (uri == null) {
            if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                Logging.report(Logging.ERROR, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(): " + "ERROR: no uri parameter");
            }
            return null;
        }
        if (nonce == null) {
            if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                Logging.report(Logging.ERROR, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(): " + "ERROR: no nonce parameter");
            }
            return null;
        }
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(), userName: " + userName + "!");
            Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(), realm: " + realm + "!");
            Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(), password: " + password + "!");
            Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(), uri: " + uri + "!");
            Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(), nonce: " + nonce + "!");
            Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(), method: " + method + "!");
        }
        String A1 = userName + ":" + realm + ":" + password;
        if (algorithm.equalsIgnoreCase(MD5_SESS)) {
            byte[] A1bytes = Utils.digest(A1.getBytes());
            byte[] tmp = (":" + nonce + ":" + cnonce).getBytes();
            byte[] join = new byte[A1bytes.length + tmp.length];
            System.arraycopy(A1bytes, 0, join, 0, A1bytes.length);
            System.arraycopy(tmp, 0, join, A1bytes.length, tmp.length);
            A1 = new String(join);
        }
        String A2 = method.toUpperCase() + ":" + uri;
        if (qop != null) {
            if (qop.equalsIgnoreCase("auth-int")) {
                A2 += ":" + digestEntityBody;
            }
        }
        byte mdbytes[] = Utils.digest(A1.getBytes());
        String HA1 = toHexString(mdbytes);
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(), HA1:" + HA1 + "!");
        }
        mdbytes = Utils.digest(A2.getBytes());
        String HA2 = toHexString(mdbytes);
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(), HA2: " + HA2 + "!");
        }
        String KD = HA1 + ":" + nonce;
        if (qop != null) {
            KD += ":" + nonceCountPar + ":" + cnonce + ":" + qop;
        }
        KD += ":" + HA2;
        mdbytes = Utils.digest(KD.getBytes());
        String response = toHexString(mdbytes);
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "generateResponse(): " + "response generated: " + response);
        }
        return response;
    }
