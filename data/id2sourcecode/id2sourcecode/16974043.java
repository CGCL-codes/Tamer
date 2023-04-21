    private byte[] loadClassData(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(BUF_SIZE);
        byte[] buffer = new byte[BUF_SIZE];
        int readCount;
        while ((readCount = input.read(buffer, 0, BUF_SIZE)) >= 0) {
            output.write(buffer, 0, readCount);
        }
        return output.toByteArray();
    }
