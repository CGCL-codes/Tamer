    public static String hashMD5(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] theTextToDigestAsBytes = s.getBytes("8859_1");
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(theTextToDigestAsBytes);
        byte[] digest = messageDigest.digest();
        String code = new String();
        for (int i = 0; i < digest.length; i++) {
            code += "" + Integer.toHexString(digest[i] & 0xff);
        }
        return code;
    }
