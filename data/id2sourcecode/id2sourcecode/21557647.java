    protected void setAuthenticateHeader(HttpServletRequest request, HttpServletResponse response) {
        byte[] buffer = null;
        String nOnce = generateNOnce(request);
        synchronized (md5Helper) {
            buffer = md5Helper.digest(nOnce.getBytes());
        }
        String authenticateHeader = "Digest realm=\"" + theRealm + "\", " + "qop=\"auth\", nonce=\"" + nOnce + "\", " + "opaque=\"" + md5Encoder.encode(buffer) + "\"";
        response.setHeader("WWW-Authenticate", authenticateHeader);
    }
