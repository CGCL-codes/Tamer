    public static String getEncode(String str) {
        String resStr = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resStr = byteArrayToHexString(md.digest(str.getBytes()));
        } catch (Exception ex) {
        }
        return resStr;
    }
