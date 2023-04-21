    public static final void pumpExactly(InputStream is, OutputStream os, int bytes) throws IOException {
        for (int i = 0; i < bytes; i++) {
            os.write(is.read());
        }
        os.flush();
        os.close();
        is.close();
    }
