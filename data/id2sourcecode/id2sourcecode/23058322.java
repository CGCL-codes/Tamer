    public static byte[] hash(final InputStream is) throws GeneralSecurityException, IOException {
        final MessageDigest digest = MessageDigest.getInstance(MD5);
        final byte[] buf = new byte[BUF_SIZE];
        int read = is.read(buf);
        while (read > 0) {
            digest.update(buf, 0, read);
            read = is.read(buf);
        }
        return digest.digest();
    }
