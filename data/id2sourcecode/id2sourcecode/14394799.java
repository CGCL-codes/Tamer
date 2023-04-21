    public static byte[] md5(byte[] buf) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(buf);
            byte[] hash = digest.digest();
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
