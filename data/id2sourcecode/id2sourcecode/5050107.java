    public static String md5(String strSrc) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            digest = md5.digest(strSrc.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(byteHEX(digest[i]));
        }
        return sb.toString();
    }
