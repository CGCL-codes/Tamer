    public static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = input.read(buffer)) >= 0) {
            if (read == 0) {
                ThreadKit.sleep(50);
                continue;
            }
            output.write(buffer, 0, read);
        }
    }
