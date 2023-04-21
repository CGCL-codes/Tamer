    public static String computeH1(String s1, String s2) {
        String res = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte key[] = md.digest(s1.getBytes("utf-8"));
            byte ss[] = s2.getBytes("utf-8");
            byte t[] = new byte[key.length + ss.length];
            for (int i = 0; i < key.length; i++) t[i] = key[i];
            for (int i = 0; i < ss.length; i++) t[key.length + i] = ss[i];
            res = byteArrayToHexString(md.digest(t));
        } catch (Exception e) {
            cn.imgdpu.util.CatException.getMethod().catException(e, "未知异常");
        }
        return res;
    }
