    public Digest(String algorithm, byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest digest = java.security.MessageDigest.getInstance(algorithm);
        digest.update(bytes);
        number = new BigInteger(1, digest.digest());
    }
