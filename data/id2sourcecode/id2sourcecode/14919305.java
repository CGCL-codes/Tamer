    public String getDigest() {
        byte[] hash = digest.digest();
        BigInteger bigInt = new BigInteger(1, hash);
        return bigInt.toString(16);
    }
