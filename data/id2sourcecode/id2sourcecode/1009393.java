    protected ByteBuffer getFileAsBytes(File file) throws IOException {
        byte buffer[] = new byte[2048];
        int n;
        InputStream input = new FileInputStream(file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while ((n = input.read(buffer)) != -1) {
            outputStream.write(buffer, 0, n);
        }
        return ByteBuffer.wrap(outputStream.toByteArray());
    }
