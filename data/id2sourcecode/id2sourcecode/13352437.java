    public static byte[] createDigest(InputStream is, String digestType) throws Exception {
        byte[] buffer = new byte[1024 * 10];
        MessageDigest complete = MessageDigest.getInstance(digestType);
        int n;
        do {
            n = is.read(buffer);
            if (n > 0) {
                complete.update(buffer, 0, n);
            }
        } while (n != -1);
        is.close();
        return complete.digest();
    }
