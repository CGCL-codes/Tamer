    public static String md5(String text) {
        if (text == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(MD5_HASH_ALGORITHM);
            byte digest[] = md.digest(text.getBytes());
            return ArrayHelper.toHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
