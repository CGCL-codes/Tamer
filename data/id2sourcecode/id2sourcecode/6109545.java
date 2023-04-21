    protected int copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        assertNotNull("inputStream == null", inputStream);
        assertNotNull("outputStream == null", outputStream);
        int total = 0;
        int read = 0;
        byte[] bytes = new byte[8196];
        while ((read = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, read);
            total += read;
        }
        return total;
    }
