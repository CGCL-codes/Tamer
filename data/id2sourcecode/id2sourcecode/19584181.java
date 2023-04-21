    public static final byte[] digest(byte[] input1, byte[] input2) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input1);
            return md5.digest(input2);
        } catch (NoSuchAlgorithmException nsae) {
            throw new Error(nsae.toString());
        }
    }
