    public static String md5Encode32(String v) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(MD5);
            return byteArrayToHexString(md.digest(v.getBytes(CHARSET_UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
