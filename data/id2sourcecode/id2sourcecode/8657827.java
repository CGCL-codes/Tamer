    public String encode(final String password) {
        String salted = password + "{" + this.salt + "}";
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(this.algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] digest;
        try {
            digest = messageDigest.digest(salted.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported!");
        }
        if (base64Encoding) {
            return new String(Base64.encode(digest));
        } else {
            return new String(digest);
        }
    }
