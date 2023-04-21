    public static final byte[] digest(byte[] input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return md5.digest(input);
        } catch (NoSuchAlgorithmException nsae) {
            throw new Error(nsae.toString());
        }
    }
