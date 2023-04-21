    private static byte[] y(byte abyte0[], byte abyte1[]) {
        byte abyte2[] = new byte[32];
        MessageDigest messagedigest;
        try {
            messagedigest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException _ex) {
            return null;
        }
        messagedigest.reset();
        byte byte0 = 20;
        if (abyte0 != null && abyte0.length > 0) {
            messagedigest.update(abyte0);
        }
        if (abyte1 != null && abyte1.length > 0) {
            messagedigest.update(abyte1);
        }
        messagedigest.update((byte) 1);
        byte abyte3[] = messagedigest.digest();
        System.arraycopy(abyte3, 0, abyte2, 0, byte0);
        messagedigest.reset();
        if (abyte0 != null && abyte0.length > 0) {
            messagedigest.update(abyte0);
        }
        if (abyte1 != null && abyte1.length > 0) {
            messagedigest.update(abyte1);
        }
        messagedigest.update((byte) 2);
        abyte3 = messagedigest.digest();
        System.arraycopy(abyte3, 0, abyte2, byte0, 32 - byte0);
        return abyte2;
    }
