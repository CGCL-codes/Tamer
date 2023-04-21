    public BlobImpl(InputStream stream, int length) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(length);
        try {
            int ch;
            while ((ch = stream.read()) >= 0) out.write(ch);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.bytes = out.toByteArray();
    }
