    public static byte[] createT2(byte[] T1, byte[] key) {
        byte T2[] = null;
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(T1);
            algorithm.update(key);
            T2 = algorithm.digest();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Error while generating T2", e);
            T2 = null;
        }
        return T2;
    }
