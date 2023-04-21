    public static long streamCopy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFERSIZE];
        int read;
        long copied = 0;
        while ((read = in.read(buffer)) > 0) {
            out.write(buffer, 0, read);
            copied += read;
        }
        return copied;
    }
