    public static void inputStream2OutputStream(InputStream stream, OutputStream out) throws IOException {
        int readedBytes;
        byte[] buf = new byte[1024];
        while ((readedBytes = stream.read(buf)) > 0) {
            out.write(buf, 0, readedBytes);
        }
        stream.close();
        out.close();
    }
