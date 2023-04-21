    public static String md5(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = md.digest(s.getBytes("UTF-8"));
        String res = "";
        for (int i = 0; i < bytes.length; i++) {
            int k = 0;
            if (bytes[i] < 0) k = 256 + bytes[i]; else k = bytes[i];
            String t = Integer.toString(k, 16);
            if (t.length() < 2) t = "0" + t;
            res += t;
        }
        return res;
    }
