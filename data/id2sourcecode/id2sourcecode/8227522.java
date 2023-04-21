    protected static void copy(InputStream input, byte[] buffer, OutputStream output, long begPos, long endPos) throws IOException {
        int read;
        long bytesToRead = endPos - begPos + 1;
        input.skip(begPos);
        read = input.read(buffer);
        while (bytesToRead > 0 && read > 0) {
            if (bytesToRead >= read) {
                output.write(buffer, 0, read);
                bytesToRead -= read;
                output.flush();
            } else {
                output.write(buffer, 0, (int) bytesToRead);
                break;
            }
            read = input.read(buffer);
        }
        output.flush();
    }
