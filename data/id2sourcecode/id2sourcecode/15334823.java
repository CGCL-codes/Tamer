    public void write(final InputStream is) throws IOException {
        DataReader reader = new DataReader() {

            public int read(byte[] dest, int off, int len) throws IOException {
                return is.read(dest, off, len);
            }

            public void close() {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        };
        while (write(reader) > 0) {
        }
    }
