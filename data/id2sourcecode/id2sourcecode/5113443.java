    public static byte[] digest(byte data[], String algorithm) {
        return getDigest(algorithm).digest(data);
    }
