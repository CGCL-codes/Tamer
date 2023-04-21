    public static byte[] calculateMD4(byte[] data) {
        MessageDigest md4 = createMD4();
        md4.update(data);
        return md4.digest();
    }
