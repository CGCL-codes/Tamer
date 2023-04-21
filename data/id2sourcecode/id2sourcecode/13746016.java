    public Principal authenticate(String username, String clientDigest, String nOnce, String nc, String cnonce, String qop, String realm, String md5a2) {
        String md5a1 = getDigest(username, realm);
        if (md5a1 == null) return null;
        String serverDigestValue = md5a1 + ":" + nOnce + ":" + nc + ":" + cnonce + ":" + qop + ":" + md5a2;
        byte[] valueBytes = null;
        if (getDigestEncoding() == null) {
            valueBytes = serverDigestValue.getBytes();
        } else {
            try {
                valueBytes = serverDigestValue.getBytes(getDigestEncoding());
            } catch (UnsupportedEncodingException uee) {
                log.error("Illegal digestEncoding: " + getDigestEncoding(), uee);
                throw new IllegalArgumentException(uee.getMessage());
            }
        }
        String serverDigest = null;
        synchronized (md5Helper) {
            serverDigest = md5Encoder.encode(md5Helper.digest(valueBytes));
        }
        if (log.isDebugEnabled()) {
            log.debug("Digest : " + clientDigest + " Username:" + username + " ClientSigest:" + clientDigest + " nOnce:" + nOnce + " nc:" + nc + " cnonce:" + cnonce + " qop:" + qop + " realm:" + realm + "md5a2:" + md5a2 + " Server digest:" + serverDigest);
        }
        if (serverDigest.equals(clientDigest)) return getPrincipal(username); else return null;
    }
