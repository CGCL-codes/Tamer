    private static byte[] readAllBytesFrom(InputStream input) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[1024];
        for (int read = input.read(chunk); read > 0; read = input.read(chunk)) {
            buffer.write(chunk, 0, read);
        }
        return buffer.toByteArray();
    }
