    public static long copy(Reader reader, Writer writer) throws IOException {
        Streams.validateReader(reader);
        Streams.validateWriter(writer);
        long count = 0;
        while (true) {
            final int b = reader.read();
            if (b == -1) {
                break;
            }
            writer.write(b);
            ++count;
        }
        return count;
    }
