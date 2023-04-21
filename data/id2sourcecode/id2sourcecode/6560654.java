    public byte[] getHashSHA1(byte[] buf) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        sha.update(buf);
        return sha.digest();
    }
