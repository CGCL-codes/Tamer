    public static void copy(InputStream in, OutputStream out, ProgressObserver observer, int bufferSize) throws IOException {
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        byte[] buffer = new byte[bufferSize];
        int bytes_read = in.read(buffer, 0, bufferSize);
        while (bytes_read > 0) {
            out.write(buffer, 0, bytes_read);
            if (observer != null) {
                observer.increment(bytes_read);
            }
            bytes_read = in.read(buffer, 0, bufferSize);
        }
        out.flush();
    }
