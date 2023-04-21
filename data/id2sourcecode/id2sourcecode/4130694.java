    public static byte[] getMD5(String s) {
        byte[] hash = new byte[0];
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            hash = digest.digest(s.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hash;
    }
