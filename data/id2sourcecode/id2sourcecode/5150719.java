    static void copyStreamsWithoutClose(InputStream in, OutputStream out, byte[] buffer) throws IOException {
        int b;
        while ((b = in.read(buffer)) != -1) out.write(buffer, 0, b);
    }
