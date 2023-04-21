    private static byte[] messageDigestSum(byte[] bytes, String algorithm) throws TechnicalException {
        MessageDigest algoDigest;
        try {
            algoDigest = MessageDigest.getInstance(algorithm);
            algoDigest.reset();
        } catch (NoSuchAlgorithmException e) {
            throw new TechnicalException(e);
        }
        return algoDigest.digest(bytes);
    }
