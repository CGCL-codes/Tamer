    private String createDigest(final Credentials credentials) throws AuthenticationException {
        String uri = getParameter("uri");
        String realm = getParameter("realm");
        String nonce = getParameter("nonce");
        String method = getParameter("methodname");
        String algorithm = getParameter("algorithm");
        if (uri == null) {
            throw new IllegalStateException("URI may not be null");
        }
        if (realm == null) {
            throw new IllegalStateException("Realm may not be null");
        }
        if (nonce == null) {
            throw new IllegalStateException("Nonce may not be null");
        }
        if (algorithm == null) {
            algorithm = "MD5";
        }
        String charset = getParameter("charset");
        if (charset == null) {
            charset = "ISO-8859-1";
        }
        if (qopVariant == QOP_AUTH_INT) {
            throw new AuthenticationException("Unsupported qop in HTTP Digest authentication");
        }
        String digAlg = algorithm;
        if (digAlg.equalsIgnoreCase("MD5-sess")) {
            digAlg = "MD5";
        }
        MessageDigest digester = createMessageDigest(digAlg);
        String uname = credentials.getUserPrincipal().getName();
        String pwd = credentials.getPassword();
        StringBuilder tmp = new StringBuilder(uname.length() + realm.length() + pwd.length() + 2);
        tmp.append(uname);
        tmp.append(':');
        tmp.append(realm);
        tmp.append(':');
        tmp.append(pwd);
        String a1 = tmp.toString();
        if (algorithm.equalsIgnoreCase("MD5-sess")) {
            algorithm = "MD5";
            String cnonce = getCnonce();
            String tmp2 = encode(digester.digest(EncodingUtils.getBytes(a1, charset)));
            StringBuilder tmp3 = new StringBuilder(tmp2.length() + nonce.length() + cnonce.length() + 2);
            tmp3.append(tmp2);
            tmp3.append(':');
            tmp3.append(nonce);
            tmp3.append(':');
            tmp3.append(cnonce);
            a1 = tmp3.toString();
        }
        String hasha1 = encode(digester.digest(EncodingUtils.getBytes(a1, charset)));
        String a2 = null;
        if (qopVariant == QOP_AUTH_INT) {
        } else {
            a2 = method + ':' + uri;
        }
        String hasha2 = encode(digester.digest(EncodingUtils.getAsciiBytes(a2)));
        String serverDigestValue;
        if (qopVariant == QOP_MISSING) {
            StringBuilder tmp2 = new StringBuilder(hasha1.length() + nonce.length() + hasha1.length());
            tmp2.append(hasha1);
            tmp2.append(':');
            tmp2.append(nonce);
            tmp2.append(':');
            tmp2.append(hasha2);
            serverDigestValue = tmp2.toString();
        } else {
            String qopOption = getQopVariantString();
            String cnonce = getCnonce();
            StringBuilder tmp2 = new StringBuilder(hasha1.length() + nonce.length() + NC.length() + cnonce.length() + qopOption.length() + hasha2.length() + 5);
            tmp2.append(hasha1);
            tmp2.append(':');
            tmp2.append(nonce);
            tmp2.append(':');
            tmp2.append(NC);
            tmp2.append(':');
            tmp2.append(cnonce);
            tmp2.append(':');
            tmp2.append(qopOption);
            tmp2.append(':');
            tmp2.append(hasha2);
            serverDigestValue = tmp2.toString();
        }
        String serverDigest = encode(digester.digest(EncodingUtils.getAsciiBytes(serverDigestValue)));
        return serverDigest;
    }
