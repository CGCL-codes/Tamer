    public byte[] readLine() throws IOException {
        int read = read();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (-1 == read) return null;
        while ((read != -1) && (read != '\n')) {
            baos.write(read);
            read = read();
        }
        return baos.toByteArray();
    }
