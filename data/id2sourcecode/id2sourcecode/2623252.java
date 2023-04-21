    public static final synchronized byte[] md5(byte[] buff) throws NoSuchAlgorithmException {
        MessageDigest md5Obj = MessageDigest.getInstance("MD5");
        md5Obj.reset();
        return md5Obj.digest(buff);
    }
