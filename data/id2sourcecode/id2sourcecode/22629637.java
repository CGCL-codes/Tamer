    public static ByteArrayOutputStream getOutput(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(input.available());
        int read = 0;
        byte[] data = new byte[4096];
        while ((read = input.read(data)) > 0) {
            output.write(data, 0, read);
        }
        output.flush();
        return output;
    }
