    public static byte[] computeMd5Hash(byte[] in) {
        Md5MessageDigest md5 = new Md5MessageDigest();
        return md5.digest(in);
    }
