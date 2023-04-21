    protected synchronized String getMD5From(byte[] data) {
        String md5;
        try {
            MessageDigest digestol = MessageDigest.getInstance("MD5");
            digestol.update(data);
            md5 = CharToByte.bytesToString(digestol.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 couldn't be loaded due to a NoSuchAlgorithmException! (the hell?)");
        }
        return md5;
    }
