    public static byte[] digest(String digestAlgorithm, InputStream input) throws NoSuchAlgorithmException, IOException {
        CCNDigestHelper dh = new CCNDigestHelper(digestAlgorithm);
        byte[] buffer = new byte[1024];
        int read = 0;
        while (read >= 0) {
            read = input.read(buffer);
            if (read > 0) {
                dh.update(buffer, 0, read);
            }
        }
        return dh.digest();
    }
