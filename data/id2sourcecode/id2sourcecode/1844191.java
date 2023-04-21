    public static String getHash(String str) {
        try {
            MessageDigest sha = MessageDigest.getInstance("MD5");
            byte[] strBytes = sha.digest(str.getBytes("UTF-8"));
            return new String(new BASE64Encoder().encode(strBytes));
        } catch (Exception e) {
            Log.msg("Util.getHash()", e);
            return null;
        }
    }
