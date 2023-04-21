    private static byte[] digest(File file, MessageDigest digest) throws IOException {
        digest.reset();
        byte[] buffer = new byte[1024];
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        try {
            int read = in.read(buffer);
            while (read >= 0) {
                digest.update(buffer, 0, read);
                read = in.read(buffer);
            }
            return digest.digest();
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
        }
    }
