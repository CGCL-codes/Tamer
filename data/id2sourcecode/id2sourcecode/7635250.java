    private void copy(InputStream in, OutputStream out, int size) throws IOException {
        for (int i = 0; i < size; i++) out.write(in.read());
    }
