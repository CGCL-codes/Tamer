    public static byte[] digestStream(MessageDigest digest, InputStream is) throws IOException {
        byte[] buf = new byte[4096];
        int nRead;
        while ((nRead = is.read(buf)) != -1) {
            digest.update(buf, 0, nRead);
        }
        return digest.digest();
    }
