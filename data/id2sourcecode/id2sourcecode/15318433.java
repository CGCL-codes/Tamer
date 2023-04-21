    public static String generateHexHash(byte data[], String codec) {
        try {
            MessageDigest digest = MessageDigest.getInstance(codec);
            BigInteger bigInt = new BigInteger(1, digest.digest(data));
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException err) {
            throw new IllegalArgumentException("Unknown hash type: " + codec, err);
        }
    }
