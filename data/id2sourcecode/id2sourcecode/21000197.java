    public static final void copy(InputStream in, OutputStream out) throws IOException {
        byte[] readBuffer = new byte[readBufferSize];
        while (true) {
            int n = in.read(readBuffer);
            if (n > 0) {
                out.write(readBuffer, 0, n);
            } else {
                break;
            }
        }
        out.flush();
    }
