    public static String sha(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] bytes = md.digest(source.getBytes());
            return byteArrayToHexString(bytes);
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
    }
