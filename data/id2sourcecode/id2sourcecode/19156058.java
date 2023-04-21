    public static byte[] createChecksum(File file, String algorithm) throws Exception {
        if (file.isDirectory()) {
            throw new IllegalArgumentException("file cannot be directory");
        }
        InputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance(algorithm);
        int numRead;
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        fis.close();
        return complete.digest();
    }
