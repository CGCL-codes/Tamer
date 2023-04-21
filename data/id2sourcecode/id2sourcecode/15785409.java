    private void stream(ReadableFile file, OutputStream outputStream, long offset, long length) throws IOException {
        byte[] data = new byte[512 * 1024];
        file.seek(offset);
        while (length > 0) {
            int read = file.read(data, 0, (int) Math.min(data.length, length));
            if (read > 0) {
                outputStream.write(data, 0, read);
                length -= read;
            } else {
                break;
            }
        }
    }
