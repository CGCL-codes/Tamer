    public byte[] md5() throws NoSuchAlgorithmException {
        MessageDigest digest = null;
        digest = java.security.MessageDigest.getInstance("MD5");
        return digest.digest(payload);
    }
