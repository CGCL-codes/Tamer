    public static void pump(InputStream in, OutputStream out, int bufferSize) throws IOException {
        byte buffer[] = new byte[bufferSize];
        int read;
        while ((read = in.read(buffer)) > 0) {
            out.write(buffer, 0, read);
        }
    }
