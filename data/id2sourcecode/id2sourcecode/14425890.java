    public static String MD5digest(String timestamp) throws NoSuchAlgorithmException {
        int i;
        byte[] digest;
        StringBuffer buffer, digestBuffer;
        MessageDigest md5;
        md5 = MessageDigest.getInstance("MD5");
        digest = md5.digest(timestamp.getBytes());
        digestBuffer = new StringBuffer(128);
        for (i = 0; i < digest.length; i++) digestBuffer.append(Integer.toHexString(digest[i] & 0xff));
        return digestBuffer.toString();
    }
