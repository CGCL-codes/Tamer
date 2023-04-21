    public static final void copyStream(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) output.write(buffer, 0, bytesRead);
    }
