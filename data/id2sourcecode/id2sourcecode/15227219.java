    static byte[] calcHandshakeHash(ByteBuffer request, ByteBuffer response) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            update(md, request);
            update(md, response);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }
