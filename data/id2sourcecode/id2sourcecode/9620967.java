    protected static String hash(MessageDigest md, String plaintext) {
        BASE64Encoder enc = new BASE64Encoder();
        return enc.encodeBuffer(md.digest(plaintext.getBytes()));
    }
