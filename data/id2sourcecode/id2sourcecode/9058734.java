    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(origin.getBytes("utf-8")));
        } catch (Exception ex) {
            cn.imgdpu.util.CatException.getMethod().catException(ex, "未知异常");
        }
        return resultString;
    }
