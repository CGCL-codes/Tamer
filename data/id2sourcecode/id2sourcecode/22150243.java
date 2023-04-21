    public static int streamCopy(InputStream in, OutputStream out, int length) throws IOException {
        byte[] buffer = new byte[BUFFERSIZE];
        int read;
        int copied = 0;
        while ((read = in.read(buffer, 0, Math.min(BUFFERSIZE, length - copied))) > 0) {
            out.write(buffer, 0, read);
            copied += read;
        }
        return copied;
    }
