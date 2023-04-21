    private String calculateMd5(byte[] buffer, int size) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(buffer, 0, size);
        total.update(buffer, 0, size);
        byte[] md5 = digest.digest();
        return digestToString(md5);
    }
