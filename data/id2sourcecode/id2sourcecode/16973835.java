    private void copy(InputStream in, OutputStream out) throws IOException {
        byte[] data = new byte[BUFFER_SIZE];
        int read = 0;
        while (read >= 0) {
            read = in.read(data);
            if (read > 0) out.write(data, 0, read);
        }
    }
