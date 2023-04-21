    private static byte[] getHash(File f, String algo) throws IOException {
        if (!f.exists()) throw new FileNotFoundException(f.toString());
        InputStream close_me = null;
        try {
            long buf_size = f.length();
            if (buf_size < 512) buf_size = 512;
            if (buf_size > 65536) buf_size = 65536;
            byte[] buf = new byte[(int) buf_size];
            FileInputStream in = new FileInputStream(f);
            close_me = in;
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance(algo);
            } catch (NoSuchAlgorithmException e) {
                new FileNotFoundException(algo + " Algorithm not supported by this JVM");
            }
            int read = 0;
            while ((read = in.read(buf)) >= 0) {
                digest.update(buf, 0, read);
            }
            in.close();
            in = null;
            close_me = null;
            buf = null;
            buf = digest.digest();
            digest = null;
            return buf;
        } catch (IOException e) {
            if (close_me != null) try {
                close_me.close();
            } catch (Exception e2) {
            }
            throw e;
        }
    }
