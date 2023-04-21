    public StreamAttachmentDataSource(InputStream inputStream, String name, String contentType) throws IOException {
        this.outputStream = new ByteArrayOutputStream();
        this.name = name;
        this.contentType = contentType;
        int read;
        byte[] buffer = new byte[256];
        while ((read = inputStream.read(buffer)) != -1) {
            getOutputStream().write(buffer, 0, read);
        }
    }
