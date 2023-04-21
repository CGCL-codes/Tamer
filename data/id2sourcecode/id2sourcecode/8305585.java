    public static String sha512(byte[] plainText) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        String sha512 = new BigInteger(1, messageDigest.digest(plainText)).toString(16);
        if (sha512.length() < 128) {
            sha512 = "0" + sha512;
        }
        messageDigest.reset();
        if (log.isDebugEnabled()) {
            log.debug(new String(plainText, CommonUtil.UTF8) + "'s SHA512: " + sha512);
        }
        return sha512;
    }
