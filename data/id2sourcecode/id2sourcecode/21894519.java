    public static String sign(String parameter, String secret) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        byte[] digest = null;
        try {
            digest = md.digest((parameter + secret).getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        BASE64Encoder encode = new BASE64Encoder();
        String a = encode.encode(digest);
        return a;
    }
