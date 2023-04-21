    protected String getDigest(String username, String realmName) {
        if (md5Helper == null) {
            try {
                md5Helper = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                log.error("Couldn't get MD5 digest: ", e);
                throw new IllegalStateException(e.getMessage());
            }
        }
        if (hasMessageDigest()) {
            return getPassword(username);
        }
        String digestValue = username + ":" + realmName + ":" + getPassword(username);
        byte[] valueBytes = null;
        if (getDigestEncoding() == null) {
            valueBytes = digestValue.getBytes();
        } else {
            try {
                valueBytes = digestValue.getBytes(getDigestEncoding());
            } catch (UnsupportedEncodingException uee) {
                log.error("Illegal digestEncoding: " + getDigestEncoding(), uee);
                throw new IllegalArgumentException(uee.getMessage());
            }
        }
        byte[] digest = null;
        synchronized (md5Helper) {
            digest = md5Helper.digest(valueBytes);
        }
        return md5Encoder.encode(digest);
    }
