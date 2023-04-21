    private static byte[] md5(byte[] bytes) throws NoSuchAlgorithmException {
        return getMd5Digest().digest(bytes);
    }
