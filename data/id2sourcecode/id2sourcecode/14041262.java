    public static String convertToMD5(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return new String(md.digest(message.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
