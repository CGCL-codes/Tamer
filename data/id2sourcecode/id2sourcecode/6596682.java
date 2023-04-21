    public static long copy(Reader reader, Writer writer, int bufferLength) throws IOException {
        Streams.validateReader(reader);
        Streams.validateWriter(writer);
        Streams.validateBufferLength(bufferLength);
        final char[] buffer = new char[bufferLength];
        int totalCount = 0;
        while (true) {
            final int count = reader.read(buffer);
            if (count == -1) {
                break;
            }
            writer.write(buffer, 0, count);
            totalCount += count;
        }
        return totalCount;
    }
