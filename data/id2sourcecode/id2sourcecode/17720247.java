    protected String createHash(byte[] entity) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            byte abyte0[] = messagedigest.digest(entity);
            return byteArrayToHexString(abyte0);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
