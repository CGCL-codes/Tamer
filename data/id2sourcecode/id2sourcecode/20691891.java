    public static void writeInputStreamToOutputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read = in.read(buffer);
        while (read >= 0) {
            out.write(buffer, 0, read);
            read = in.read(buffer);
        }
        out.flush();
        out.close();
    }
