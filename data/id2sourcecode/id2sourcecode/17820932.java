    private void stream(BufferdFile file, OutputStream outputStream, long offset, long length) throws IOException {
        byte[] data = new byte[50 * 1024];
        while (length > 0) {
            int read = file.readFile(data, (int) Math.min((long) data.length, length), 0, offset);
            if (read > 0) {
                outputStream.write(data, 0, read);
                length -= read;
                offset += read;
            } else {
                break;
            }
        }
    }
