    public boolean doAuthenticate(Request request, AuthorizationHeader authHeader, String user, String password) {
        String username = authHeader.getUsername();
        if (username == null || !username.equals(user)) return false;
        String realm = authHeader.getRealm();
        if (realm == null) realm = defaultRealm;
        URI uri = authHeader.getURI();
        if (uri == null) return false;
        String algorithm = authHeader.getAlgorithm();
        if (algorithm == null) algorithm = getPreferredAlgorithm();
        MessageDigest messageDigest = algorithms.get(algorithm);
        if (messageDigest == null) return false;
        byte mdbytes[];
        String A1 = username + ":" + realm + ":" + password;
        String A2 = request.getMethod().toUpperCase() + ":" + uri.toString();
        mdbytes = messageDigest.digest(A1.getBytes());
        String HA1 = SipUtils.toHexString(mdbytes);
        mdbytes = messageDigest.digest(A2.getBytes());
        String HA2 = SipUtils.toHexString(mdbytes);
        String nonce = authHeader.getNonce();
        String cnonce = authHeader.getCNonce();
        String KD = HA1 + ":" + nonce;
        if (cnonce != null) KD += ":" + cnonce;
        KD += ":" + HA2;
        mdbytes = messageDigest.digest(KD.getBytes());
        String mdString = SipUtils.toHexString(mdbytes);
        String response = authHeader.getResponse();
        return mdString.compareTo(response) == 0;
    }
