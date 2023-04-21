    public static String computeResponseValue(IoSession session, HashMap<String, String> map, String method, String pwd, String charsetName, String body) throws AuthenticationException, UnsupportedEncodingException {
        byte[] hA1;
        StringBuilder sb;
        boolean isMD5Sess = "md5-sess".equalsIgnoreCase(StringUtilities.getDirectiveValue(map, "algorithm", false));
        if (!isMD5Sess || (session.getAttribute(SESSION_HA1) == null)) {
            sb = new StringBuilder();
            sb.append(StringUtilities.stringTo8859_1(StringUtilities.getDirectiveValue(map, "username", true))).append(':');
            String realm = StringUtilities.stringTo8859_1(StringUtilities.getDirectiveValue(map, "realm", false));
            if (realm != null) {
                sb.append(realm);
            }
            sb.append(':').append(pwd);
            if (isMD5Sess) {
                byte[] prehA1;
                synchronized (md5) {
                    md5.reset();
                    prehA1 = md5.digest(sb.toString().getBytes(charsetName));
                }
                sb = new StringBuilder();
                sb.append(ByteUtilities.asHex(prehA1));
                sb.append(':').append(StringUtilities.stringTo8859_1(StringUtilities.getDirectiveValue(map, "nonce", true)));
                sb.append(':').append(StringUtilities.stringTo8859_1(StringUtilities.getDirectiveValue(map, "cnonce", true)));
                synchronized (md5) {
                    md5.reset();
                    hA1 = md5.digest(sb.toString().getBytes(charsetName));
                }
                session.setAttribute(SESSION_HA1, hA1);
            } else {
                synchronized (md5) {
                    md5.reset();
                    hA1 = md5.digest(sb.toString().getBytes(charsetName));
                }
            }
        } else {
            hA1 = (byte[]) session.getAttribute(SESSION_HA1);
        }
        sb = new StringBuilder(method);
        sb.append(':');
        sb.append(StringUtilities.getDirectiveValue(map, "uri", false));
        String qop = StringUtilities.getDirectiveValue(map, "qop", false);
        if ("auth-int".equalsIgnoreCase(qop)) {
            ProxyIoSession proxyIoSession = (ProxyIoSession) session.getAttribute(ProxyIoSession.PROXY_SESSION);
            byte[] hEntity;
            synchronized (md5) {
                md5.reset();
                hEntity = md5.digest(body.getBytes(proxyIoSession.getCharsetName()));
            }
            sb.append(':').append(hEntity);
        }
        byte[] hA2;
        synchronized (md5) {
            md5.reset();
            hA2 = md5.digest(sb.toString().getBytes(charsetName));
        }
        sb = new StringBuilder();
        sb.append(ByteUtilities.asHex(hA1));
        sb.append(':').append(StringUtilities.getDirectiveValue(map, "nonce", true));
        sb.append(":00000001:");
        sb.append(StringUtilities.getDirectiveValue(map, "cnonce", true));
        sb.append(':').append(qop).append(':');
        sb.append(ByteUtilities.asHex(hA2));
        byte[] hFinal;
        synchronized (md5) {
            md5.reset();
            hFinal = md5.digest(sb.toString().getBytes(charsetName));
        }
        return ByteUtilities.asHex(hFinal);
    }
