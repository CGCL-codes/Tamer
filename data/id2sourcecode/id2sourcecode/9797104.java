    private String digestStream(InputStream stream, String algorithm) throws java.security.NoSuchAlgorithmException, java.io.IOException {
        DigestInputStream dStream = new DigestInputStream(stream, MessageDigest.getInstance(algorithm));
        byte[] bytes = new byte[BYTE_ARRAY_SIZE];
        while (dStream.read(bytes, 0, BYTE_ARRAY_SIZE) != -1) {
        }
        return Utils.toHex(dStream.getMessageDigest().digest());
    }
