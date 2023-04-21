    public static void get(URL url, OutputStream out, int bufferSize, long timeout, StreamReaderListener listener) throws IOException {
        InputStream in = null;
        NonBlockingStreamReader reader = null;
        try {
            in = url.openConnection().getInputStream();
            reader = new NonBlockingStreamReader(listener);
            reader.read(in, out, bufferSize, timeout, null);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
