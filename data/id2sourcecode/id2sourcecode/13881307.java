    public static void copyStreams(InputStream is, OutputStream os, MessageDigest digest) throws IOException {
        byte[] buffer = new byte[8192];
        int read = -1;
        while ((read = is.read(buffer)) > 0) {
            os.write(buffer, 0, read);
            digest.update(buffer, 0, read);
        }
    }
