    public MultipartFileInfo(final String name, final String contentType, final String filename, final InputStream in, final boolean bufferFileUploadsToDisk) throws IOException {
        this(name, contentType, filename, bufferFileUploadsToDisk);
        if (bufferFileUploadsToDisk) {
            this.buffer = TempFileUtils.writeToTempFile(in);
            this.length = this.buffer.length();
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int read = in.read();
            while (read >= 0) {
                out.write((byte) read);
                read = in.read();
            }
            this.data = out.toByteArray();
            this.length = this.data.length;
        }
    }
