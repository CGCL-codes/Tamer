    public static String md5(String text) {
        if (text == null || text.length() == 0) return "";
        byte[] src = text.getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src);
            byte[] bytes = md.digest(KEYVALUE.getBytes());
            return StringUtil.byte2hex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
