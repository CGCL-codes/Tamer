    @Override
    public void write(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        byte[] snippet = new byte[1024];
        int bytesread;
        while ((bytesread = bais.read(snippet)) > 0) {
            write(snippet, 0, bytesread);
        }
    }
