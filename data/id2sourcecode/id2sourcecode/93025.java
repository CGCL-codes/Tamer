    private byte[] md5_key(byte[] md5_content) {
        java.security.MessageDigest digest = null;
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(md5_content);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digest.digest();
    }
