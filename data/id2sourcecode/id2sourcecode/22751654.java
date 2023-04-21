    public synchronized void close() throws IOException {
        flush();
        if (next == null) {
            if (openedStream) {
                if (stream != null) stream.close();
                if (readwrite != null) readwrite.close();
            }
        } else next.close();
    }
