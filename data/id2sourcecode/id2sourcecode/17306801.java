    public static String getChecksumAsHex(InputStream input, HashAlgorithm algorithm) throws IOException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("invalid format", e);
        }
        md.update(IOUtil.readStreamAsBytes(input));
        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        return bigInt.toString(16);
    }
