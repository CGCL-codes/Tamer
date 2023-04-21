    public MultipartFileInfo(String name, String contentType, String filename, InputStream in) throws IOException {
        this(name, contentType, filename);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = in.read();
        while (read >= 0) {
            out.write((byte) read);
            read = in.read();
        }
        this.data = out.toByteArray();
    }
