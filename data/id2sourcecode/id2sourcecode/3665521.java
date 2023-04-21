    private byte[] readFully(InputStream is) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read = 0;
        while ((read = is.read(buffer)) >= 0) baos.write(buffer, 0, read);
        return baos.toByteArray();
    }
