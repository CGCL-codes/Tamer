    public MessagePart(java.io.InputStream is, java.lang.String mimeType, java.lang.String contentId, java.lang.String contentLocation, java.lang.String enc) throws IOException, SizeExceededException {
        byte[] bytes = {};
        if (is != null) {
            ByteArrayOutputStream accumulator = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int readBytes = 0;
            while ((readBytes = is.read(buffer)) != -1) {
                accumulator.write(buffer, 0, readBytes);
            }
            bytes = accumulator.toByteArray();
        }
        construct(bytes, 0, bytes.length, mimeType, contentId, contentLocation, enc);
    }
