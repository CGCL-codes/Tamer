    public static String get() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return new ByteBuffer(md.digest(getByteBuffer().getBytes()), false).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new Error(e.getMessage());
        }
    }
