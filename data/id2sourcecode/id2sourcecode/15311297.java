    public static void transfer(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[2048];
        int read = -1;
        while ((read = input.read(buffer)) > 0) {
            output.write(buffer, 0, read);
        }
    }
