    public byte[] getHashMD5(byte[] buf) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(buf);
        return md5.digest();
    }
