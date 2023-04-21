    protected String getMD5Hash(final String msg) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            return new String(Hex.encodeHex(md5.digest(msg.getBytes("UTF-8"))));
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }
