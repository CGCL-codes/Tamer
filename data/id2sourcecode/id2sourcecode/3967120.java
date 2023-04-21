    public static final byte[] computeChap(byte id, byte[] password, byte[] challenge) {
        MessageDigest md5 = getMd5();
        md5.update(id);
        md5.update(password);
        md5.update(challenge);
        return md5.digest();
    }
