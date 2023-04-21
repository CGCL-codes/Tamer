    private byte[] stream2byteArray(InputStream resource) throws IOException {
        byte[] buffer = new byte[4096];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read;
        while ((read = resource.read(buffer)) > 0) baos.write(buffer, 0, read);
        return baos.toByteArray();
    }
