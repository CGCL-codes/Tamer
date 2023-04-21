    public static String getMD5ofStr(String origString) {
        String origMD5 = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] result = md5.digest(origString.getBytes());
            origMD5 = byteArray2HexStr(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return origMD5;
    }
