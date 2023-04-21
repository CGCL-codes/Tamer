    private void writeStream(byte[] buf, int offset, int len) throws IOException {
        if (stream != null) {
            stream.write(buf, offset, len);
        } else readwrite.write(buf, offset, len);
    }
