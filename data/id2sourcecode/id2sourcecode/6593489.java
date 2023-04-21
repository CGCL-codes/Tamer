    public static byte[] getDigest(byte[] input) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("MD5 Digest Algorithm Not Initialized.");
        }
        md5.update(input);
        return md5.digest();
    }
