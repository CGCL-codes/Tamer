    private String read(final BufferedReader reader, final int chunkSize) throws IOException {
        char chunk[] = new char[chunkSize];
        StringBuffer buffer = new StringBuffer();
        int read = 0;
        while (read != chunkSize) {
            int start = read;
            read += reader.read(chunk, 0, chunkSize - read);
            int end = read - start;
            buffer.append(chunk, 0, end);
        }
        return buffer.toString();
    }
