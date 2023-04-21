    public boolean doAuthenticate(String user, AuthorizationHeader authHeader, Request request) {
        String realm = authHeader.getRealm();
        String username = authHeader.getUsername();
        URI requestURI = request.getRequestURI();
        if (username == null) {
            ProxyDebug.println("DEBUG, DigestAuthenticateMethod, doAuthenticate(): " + "WARNING: userName parameter not set in the header received!!!");
            username = user;
        }
        if (realm == null) {
            ProxyDebug.println("DEBUG, DigestAuthenticateMethod, doAuthenticate(): " + "WARNING: realm parameter not set in the header received!!! WE use the default one");
            realm = DEFAULT_REALM;
        }
        ProxyDebug.println("DEBUG, DigestAuthenticateMethod, doAuthenticate(): " + "Trying to authenticate user: " + username + " for " + " the realm: " + realm);
        String password = (String) passwordTable.get(username + "@" + realm);
        if (password == null) {
            ProxyDebug.println("DEBUG, DigestAuthenticateMethod, doAuthenticate(): " + "ERROR: password not found for the user: " + username + "@" + realm);
            return false;
        }
        String nonce = authHeader.getNonce();
        URI uri = authHeader.getURI();
        if (uri == null) {
            ProxyDebug.println("DEBUG, DigestAuthenticateMethod, doAuthenticate(): " + "ERROR: uri paramater not set in the header received!");
            return false;
        }
        ProxyDebug.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), username:" + username + "!");
        ProxyDebug.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), realm:" + realm + "!");
        ProxyDebug.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), password:" + password + "!");
        ProxyDebug.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), uri:" + uri + "!");
        ProxyDebug.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), nonce:" + nonce + "!");
        ProxyDebug.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), method:" + request.getMethod() + "!");
        String A1 = username + ":" + realm + ":" + password;
        String A2 = request.getMethod().toUpperCase() + ":" + uri.toString();
        byte mdbytes[] = messageDigest.digest(A1.getBytes());
        String HA1 = ProxyUtilities.toHexString(mdbytes);
        ProxyDebug.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), HA1:" + HA1 + "!");
        mdbytes = messageDigest.digest(A2.getBytes());
        String HA2 = ProxyUtilities.toHexString(mdbytes);
        ProxyDebug.println("DEBUG, DigestAuthenticationMethod, doAuthenticate(), HA2:" + HA2 + "!");
        String cnonce = authHeader.getCNonce();
        String KD = HA1 + ":" + nonce;
        if (cnonce != null) {
            KD += ":" + cnonce;
        }
        KD += ":" + HA2;
        mdbytes = messageDigest.digest(KD.getBytes());
        String mdString = ProxyUtilities.toHexString(mdbytes);
        String response = authHeader.getResponse();
        ProxyDebug.println("DEBUG, DigestAuthenticateMethod, doAuthenticate(): " + "we have to compare his response: " + response + " with our computed" + " response: " + mdString);
        int res = (mdString.compareTo(response));
        if (res == 0) {
            ProxyDebug.println("DEBUG, DigestAuthenticateMethod, doAuthenticate(): " + "User authenticated...");
        } else {
            ProxyDebug.println("DEBUG, DigestAuthenticateMethod, doAuthenticate(): " + "User not authenticated...");
        }
        return res == 0;
    }
