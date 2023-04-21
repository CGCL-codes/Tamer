    public void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        int readed = 0;
        while ((readed = input.read(buffer)) > 0) {
            output.write(buffer, 0, readed);
        }
    }
