    public static final byte[] readFile(String fileName) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);
        FileInputStream origFile = new FileInputStream(fileName);
        byte[] data = new byte[128];
        int read;
        while ((read = origFile.read(data)) > 0) {
            buffer.write(data, 0, read);
        }
        return buffer.toByteArray();
    }
