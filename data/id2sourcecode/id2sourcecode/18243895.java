    public static String md5(byte[] plainText, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(salt);
        String md5 = new BigInteger(1, messageDigest.digest(plainText)).toString(16);
        if (md5.length() < 32) {
            md5 = "0" + md5;
        }
        messageDigest.reset();
        if (log.isDebugEnabled()) {
            log.debug(new String(plainText, "UTF-8") + "[salt=" + new String(salt, "UTF-8") + "] 's MD5: " + md5);
        }
        return md5;
    }
