    public static String md5(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.getBytes());
            return byteArrayToHexString(bytes);
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
    }
