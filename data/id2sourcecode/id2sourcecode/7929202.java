    public static String md5(String pString) {
        try {
            MessageDigest lMessageDigest = MessageDigest.getInstance("MD5");
            byte[] lByteArray = lMessageDigest.digest(pString.getBytes());
            return new BigInteger(1, lByteArray).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            throw new ExInternal("MD5 not implemented", ex);
        }
    }
