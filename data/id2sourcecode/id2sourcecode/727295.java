    public static String getDigestHex(byte[] buffer) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(buffer);
            return new BigInteger(1, md5.digest()).toString(16).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
