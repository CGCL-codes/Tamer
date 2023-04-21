    private static byte[] sha256(byte[] bytes) throws TokenIssuanceException {
        MessageDigest mdAlgorithm;
        try {
            mdAlgorithm = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new TokenIssuanceException(e);
        }
        mdAlgorithm.update(bytes);
        byte[] digest = mdAlgorithm.digest();
        return digest;
    }
