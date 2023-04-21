    public static void pump(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int bytes_read = 0;
        while (bytes_read != -1) {
            bytes_read = in.read(buffer);
            if (bytes_read == -1) break;
            out.write(buffer, 0, bytes_read);
        }
    }
