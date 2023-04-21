    public static String encoderByMD5(String str) {
        String result = "";
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            result = base64en.encode(mDigest.digest(str.getBytes("utf-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
