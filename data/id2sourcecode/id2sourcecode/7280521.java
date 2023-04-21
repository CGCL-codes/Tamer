    public String getDigestHexString() {
        return new BigInteger(1, messageDigest.digest()).toString(16);
    }
