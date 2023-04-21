    public static byte[] hashMethod(byte[] byteSource) throws Exception {
        try {
            MessageDigest currentAlgorithm = MessageDigest.getInstance(HASH);
            currentAlgorithm.reset();
            currentAlgorithm.update(byteSource);
            return currentAlgorithm.digest();
        } catch (Exception e) {
            throw e;
        }
    }
