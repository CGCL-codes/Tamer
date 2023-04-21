    private static void copy(InputStream in, OutputStream out, byte[] buffer, int length) throws IOException {
        while (length > 0) {
            int readLen = in.read(buffer, 0, Math.min(buffer.length, length));
            if (readLen < 0) {
                throw new InvalidObjectException("input stream data truncated");
            }
            out.write(buffer, 0, readLen);
            length -= readLen;
        }
    }
