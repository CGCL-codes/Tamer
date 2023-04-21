    private String calculateSignature() throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(buildMD5String());
        byte[] digestBytes = digest.digest();
        BigInteger bigInteger = new BigInteger(1, digestBytes);
        return bigInteger.toString(16);
    }
