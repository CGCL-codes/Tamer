    private static byte[] hashFile(File f, MessageDigest digest) throws IOException {
        digest.reset();
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(f));
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = is.read(bytes)) >= 0) {
            digest.update(bytes, 0, len);
        }
        is.close();
        return digest.digest();
    }
