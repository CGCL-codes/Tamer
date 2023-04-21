    public static String md5Encode(String plain) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] result = md5.digest(plain.getBytes("UTF-8"));
            String encoded = Util.encodeHexStr(result, 0, result.length);
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
