    public NSData(InputStream stream, int chunkSize) throws IOException {
        super();
        byte[] b = new byte[chunkSize];
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int read = 0;
        do {
            read = stream.read(b);
            if (read > 0) bout.write(b, 0, read);
        } while (read > 0);
        bytes = bout.toByteArray();
    }
