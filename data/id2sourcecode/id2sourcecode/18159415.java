    protected static String getHashOf(byte[] data, String hashType, int hashLength) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(hashType);
        BigInteger bigint = new BigInteger(1, md.digest(data));
        StringBuffer strbuf = new StringBuffer(bigint.toString(16));
        while (strbuf.length() < hashLength) {
            strbuf.insert(0, "0");
        }
        return strbuf.toString();
    }
