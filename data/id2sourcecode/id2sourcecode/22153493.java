    public static byte[] getHashMD5(String data, String key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] textBytes = data.getBytes();
            md5.update(textBytes);
            byte[] result = md5.digest();
            return result;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
