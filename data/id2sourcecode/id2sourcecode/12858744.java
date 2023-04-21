    private static byte[] getHashNio(File f, String algo) throws IOException {
        if (!f.exists()) throw new FileNotFoundException(f.toString());
        InputStream close_me = null;
        try {
            long buf_size = f.length();
            if (buf_size < 512) buf_size = 512;
            if (buf_size > 65536) buf_size = 65536;
            FileInputStream in = new FileInputStream(f);
            close_me = in;
            FileChannel fileChannel = in.getChannel();
            byte[] buf = new byte[(int) buf_size];
            ByteBuffer bb = ByteBuffer.wrap(buf);
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance(algo);
            } catch (NoSuchAlgorithmException e) {
                new FileNotFoundException(algo + " Algorithm not supported by this JVM");
            }
            int size = 0;
            while ((size = fileChannel.read(bb)) >= 0) {
                digest.update(buf, 0, size);
                bb.clear();
            }
            fileChannel.close();
            fileChannel = null;
            in = null;
            close_me = null;
            bb = null;
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
