    public static void transfer(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024 * 1024];
        int read = -1;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
