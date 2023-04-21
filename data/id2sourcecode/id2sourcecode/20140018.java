    public static byte[] hash(byte[] data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MD5 not supported");
        }
        return md.digest(data);
    }
