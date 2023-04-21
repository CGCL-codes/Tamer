    protected void setAuthenticateHeader(MobicentsSipServletRequest request, MobicentsSipServletResponse response, MobicentsSipLoginConfig config, String nOnce) {
        String realmName = ((SipLoginConfig) config).getRealmName();
        if (realmName == null) realmName = request.getServerName() + ":" + request.getServerPort();
        byte[] buffer = null;
        synchronized (md5Helper) {
            buffer = md5Helper.digest(nOnce.getBytes());
        }
        String authenticateHeader = "Digest realm=\"" + realmName + "\", " + "qop=\"auth\", nonce=\"" + nOnce + "\", " + "opaque=\"" + MD5_ECNODER.encode(buffer) + "\"";
        if (response.getStatus() == MobicentsSipServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED) {
            response.setHeader("Proxy-Authenticate", authenticateHeader);
        } else {
            response.setHeader("WWW-Authenticate", authenticateHeader);
        }
    }
