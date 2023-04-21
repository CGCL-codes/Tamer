    public static byte[] getMD5Checksum(byte[] byteSequence) throws NoSuchAlgorithmException {
        MessageDigest _md5Algorithm = MessageDigest.getInstance(FileHandler.MD5_ALGORITHM_IDENTIFER);
        _md5Algorithm.update(byteSequence);
        return _md5Algorithm.digest();
    }
