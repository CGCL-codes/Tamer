    public static String getAsMD5(String text) throws Exception {
        byte[] theTextToDigestAsBytes = text.getBytes("8859_1");
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(theTextToDigestAsBytes);
        byte[] digest = md.digest();
        StringBuffer result = new StringBuffer();
        for (byte b : digest) {
            result.append(Integer.toHexString(b & 0xff));
        }
        return result.toString();
    }
