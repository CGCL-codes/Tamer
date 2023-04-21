    private String generateRandomNonce() throws Exception {
        Random random = new Random();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = new byte[4];
        for (int i = 0; i < 8; i++) {
            random.nextBytes(bytes);
            md.digest(bytes);
        }
        return new String(Hex.encodeHex(md.digest()));
    }
