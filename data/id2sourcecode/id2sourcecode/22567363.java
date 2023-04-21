    public static String computeStrongName(byte[][] contents) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error initializing MD5", e);
        }
        ByteBuffer b = ByteBuffer.allocate((contents.length + 1) * 4);
        b.putInt(contents.length);
        for (int i = 0; i < contents.length; i++) {
            b.putInt(contents[i].length);
        }
        b.flip();
        md5.update(b);
        for (int i = 0; i < contents.length; i++) {
            md5.update(contents[i]);
        }
        return toHexString(md5.digest());
    }
