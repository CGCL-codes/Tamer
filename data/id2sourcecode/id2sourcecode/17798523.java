    @Override
    public long apply(byte[] data) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return -1;
        }
        byte[] hashBytes = digest.digest(data);
        return new BigInteger(1, hashBytes).longValue();
    }
