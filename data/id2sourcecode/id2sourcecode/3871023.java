    private void savePayload(String filename, BufferedInputStream is) throws java.io.IOException {
        int c;
        PushbackInputStream input = new PushbackInputStream(is, 128);
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        while ((c = read(input, boundary)) >= 0) blob.write(c);
        saveBlob(filename, blob.toByteArray());
        blob.close();
    }
