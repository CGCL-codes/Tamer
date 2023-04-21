    public AuthorizationHeader getAuthorizationHeader(String method, String uri, String requestBody, Header authHeader, String username, String password, String nonce, int nc) {
        String response = null;
        boolean isResponseHeader = true;
        WWWAuthenticateHeader wwwAuthenticateHeader = null;
        if (authHeader instanceof WWWAuthenticateHeader) {
            wwwAuthenticateHeader = (WWWAuthenticateHeader) authHeader;
        }
        AuthorizationHeader authorizationHeader = null;
        if (authHeader instanceof AuthorizationHeader) {
            authorizationHeader = (AuthorizationHeader) authHeader;
            isResponseHeader = false;
        }
        String qopList = null;
        if (isResponseHeader) {
            qopList = wwwAuthenticateHeader.getQop();
        } else {
            qopList = authorizationHeader.getQop();
        }
        String algorithm = null;
        if (isResponseHeader) {
            algorithm = wwwAuthenticateHeader.getAlgorithm();
        } else {
            algorithm = authorizationHeader.getAlgorithm();
        }
        String realm = null;
        if (isResponseHeader) {
            realm = wwwAuthenticateHeader.getRealm();
        } else {
            realm = authorizationHeader.getRealm();
        }
        String scheme = null;
        if (isResponseHeader) {
            scheme = wwwAuthenticateHeader.getScheme();
        } else {
            scheme = authorizationHeader.getScheme();
        }
        String opaque = null;
        if (isResponseHeader) {
            opaque = wwwAuthenticateHeader.getOpaque();
        } else {
            opaque = authorizationHeader.getOpaque();
        }
        String qop = (qopList != null) ? "auth" : null;
        String nc_value = String.format("%08x", nc);
        long currentTime = System.currentTimeMillis();
        String nOnceValue = currentTime + ":" + "mobicents" + response;
        byte[] buffer = null;
        synchronized (md5Helper) {
            buffer = md5Helper.digest(nOnceValue.getBytes());
        }
        String cnonce = MD5_ECNODER.encode(buffer);
        try {
            response = MessageDigestResponseAlgorithm.calculateResponse(algorithm, username, realm, password, nonce, nc_value, cnonce, method, uri, requestBody, qop);
        } catch (NullPointerException exc) {
            throw new IllegalStateException("The authenticate header was malformatted", exc);
        }
        AuthorizationHeader authorization = null;
        try {
            if (authHeader instanceof ProxyAuthenticateHeader || authHeader instanceof ProxyAuthorizationHeader) {
                authorization = headerFactory.createProxyAuthorizationHeader(scheme);
            } else {
                authorization = headerFactory.createAuthorizationHeader(scheme);
            }
            authorization.setUsername(username);
            authorization.setRealm(realm);
            authorization.setNonce(nonce);
            authorization.setParameter("uri", uri);
            authorization.setResponse(response);
            if (algorithm != null) {
                authorization.setAlgorithm(algorithm);
            }
            if (opaque != null && opaque.length() > 0) {
                authorization.setOpaque(opaque);
            }
            if (qop != null) {
                authorization.setQop(qop);
                authorization.setCNonce(cnonce);
                authorization.setNonceCount(Integer.parseInt(nc_value, 16));
            }
            authorization.setResponse(response);
        } catch (ParseException ex) {
            throw new SecurityException("Failed to create an authorization header!", ex);
        }
        return authorization;
    }
