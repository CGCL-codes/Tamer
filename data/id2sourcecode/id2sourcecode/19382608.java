    private void pullStream(InputStream stream) throws IOException {
        if (stream == null || stream.available() <= 0) {
            return;
        }
        byte[] buffer = new byte[256];
        while (true) {
            int read = stream.read(buffer);
            if (read == -1) {
                break;
            }
            System.err.write(buffer, 0, read);
        }
    }
