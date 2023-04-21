    private byte[] readFully(InputStream in) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        for (int read = in.read(buffer, 0, buffer.length); read != -1; read = in.read(buffer, 0, buffer.length)) {
            content.write(buffer, 0, read);
        }
        return content.toByteArray();
    }
