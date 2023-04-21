    public boolean doAuthenticate(String user, AuthorizationHeader authHeader, Request request) {
        String realm = authHeader.getRealm();
        String username = authHeader.getUsername();
        if (username == null) {
            System.out.println("DEBUG, DigestAuthenticateMethod, doAuthenticate(): " + "WARNING: userName parameter not set in the header received!!!");
            username = user;
        }
        if (realm == null) {
            System.out.println("DEBUG, DigestAuthenticateMethod, doAuthenticate(): " + "WARNING: realm parameter not set in the header received!!! WE use the default one");
            realm = DEFAULT_REALM;
        }
        System.out.println("DEBUG, DigestAuthenticateMethod, doAuthenticate(): " + "Trying to authenticate user: " + username + " for " + " the realm: " + realm);
        String nonce = authHeader.getNonce();
        URI uri = authHeader.getURI();
        if (uri == null) {
            System.out.println("DEBUG, DigestAuthenticateMethod, doAuthenticate(): " + "ERROR: uri paramater not set in the header received!");
            return false;
        }
        System.out.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), username:" + username);
        System.out.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), realm:" + realm);
        System.out.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), password:" + PASS_AUTH);
        System.out.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), uri:" + uri);
        System.out.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), nonce:" + nonce);
        System.out.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), method:" + request.getMethod());
        String A1 = username + ":" + realm + ":" + PASS_AUTH;
        String A2 = request.getMethod().toUpperCase() + ":" + uri.toString();
        byte mdbytes[] = messageDigest.digest(A1.getBytes());
        String HA1 = toHexString(mdbytes);
        System.out.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), HA1:" + HA1);
        mdbytes = messageDigest.digest(A2.getBytes());
        String HA2 = toHexString(mdbytes);
        String KD = HA1 + ":" + nonce;
        System.out.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), HA2:" + HA2);
        String nonceCount = authHeader.getParameter("nc");
        String cnonce = authHeader.getCNonce();
        String qop = authHeader.getQop();
        if (cnonce != null && nonceCount != null && qop != null && (qop.equalsIgnoreCase("auth") || qop.equalsIgnoreCase("auth-int"))) {
            System.out.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), cnonce:" + cnonce);
            System.out.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), nonceCount:" + nonceCount);
            System.out.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), qop:" + qop);
            KD += ":" + nonceCount;
            KD += ":" + cnonce;
            KD += ":" + qop;
        }
        KD += ":" + HA2;
        mdbytes = messageDigest.digest(KD.getBytes());
        String mdString = toHexString(mdbytes);
        String response = authHeader.getResponse();
        System.out.println("DEBUG, DigestAuthenticateMethod, doAuthenticate(): " + "we have to compare his response: " + response + " with our computed" + " response: " + mdString);
        int res = (mdString.compareTo(response));
        if (res == 0) {
            System.out.println("DEBUG, DigestAuthenticateMethod, doAuthenticate(): " + "User authenticated...");
        } else {
            System.out.println("DEBUG, DigestAuthenticateMethod, doAuthenticate(): " + "User not authenticated...");
        }
        return res == 0;
    }
