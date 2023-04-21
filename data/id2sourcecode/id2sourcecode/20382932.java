    public static void pump(InputStream in, OutputStream out, int bufferSize, long start, long end) throws IOException {
        in.skip(start);
        long bytesToPump = end - start + 1;
        byte buffer[] = new byte[bufferSize];
        int read;
        while (bytesToPump > 0 && (read = in.read(buffer)) > 0) {
            if (bytesToPump >= read) {
                out.write(buffer, 0, read);
                bytesToPump -= read;
            } else {
                out.write(buffer, 0, (int) bytesToPump);
                bytesToPump = 0;
            }
        }
    }
