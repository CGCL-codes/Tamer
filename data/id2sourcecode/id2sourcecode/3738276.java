    public static byte[] sha1(byte[] b) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(b, 0, b.length);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new Error(e);
        }
    }
