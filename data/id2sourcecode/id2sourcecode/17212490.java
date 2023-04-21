    public static byte[] hash(ByteBuffer hashThis) {
        try {
            byte[] hash = new byte[20];
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(hashThis);
            return md.digest();
        } catch (NoSuchAlgorithmException nsae) {
        }
        return null;
    }
