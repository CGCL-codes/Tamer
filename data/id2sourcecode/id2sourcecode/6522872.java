    private File inputStreamToFile(InputStream inputStream, String prefix, String suffix) throws IOException {
        File tmp = File.createTempFile(prefix, suffix);
        final int BUFFER_SIZE = 2048;
        final byte[] data = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(inputStream, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tmp), BUFFER_SIZE);
        int n;
        while ((n = in.read(data, 0, BUFFER_SIZE)) != -1) out.write(data, 0, n);
        out.flush();
        out.close();
        return tmp;
    }
