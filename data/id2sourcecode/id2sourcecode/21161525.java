    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_LENGTH];
        int bytes_read;
        while ((bytes_read = in.read(buffer)) != END_OF_FILE) out.write(buffer, 0, bytes_read);
    }
