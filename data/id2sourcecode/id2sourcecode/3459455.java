    public int transferFully(ReadableByteChannel channel) throws IOException {
        int total = 0, read = 0;
        while ((read = transferFrom(channel)) >= 0) {
            total += read;
        }
        return total;
    }
