    public static String md5(String data) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] ds = md.digest(StringUtils.getBytesUtf8(data));
            String s = Base64.encodeBase64String(ds);
            return s.replace("\r", "").replace("\n", "");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
