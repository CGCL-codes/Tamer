    protected byte[] loadFile(String fileName) throws IOException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (resourceAsStream == null) {
            throw new IOException("File not found:" + fileName);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int read = 0;
        while (read > -1) {
            read = resourceAsStream.read();
            if (read != -1) {
                byteArrayOutputStream.write(read);
            }
        }
        byte[] content = byteArrayOutputStream.toByteArray();
        return content;
    }
