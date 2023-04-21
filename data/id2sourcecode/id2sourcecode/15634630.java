    public static String hashMD5(String str) {
        byte[] b = str.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return (base64Encode(md.digest()));
    }
