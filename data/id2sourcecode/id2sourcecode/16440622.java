    public static void copy(InputStream in, OutputStream out, int batch) throws IOException {
        if (batch < 4096) {
            batch = 4096;
        }
        byte[] buffer = new byte[batch];
        do {
            int read = in.read(buffer);
            if (read > 0) {
                out.write(buffer, 0, read);
            } else if (read < 0) {
                break;
            }
        } while (!Thread.currentThread().isInterrupted());
    }
