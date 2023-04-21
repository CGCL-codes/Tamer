    public static byte[] md5(byte[] buff) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("MD5").digest(buff);
    }
