    public int read() throws IOException {
        if (readCursor >= writeCursor) {
            int count = 0;
            while (readCursor >= writeCursor) {
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                }
                count++;
                if (count > 1000) {
                    throw new IOException("HttpPostInputStreamBuffer.read timed out.");
                }
            }
        }
        int out = store[readCursor % 5000];
        readCursor++;
        return out;
    }
