    public static void copy(TrieReader reader, TrieWriter writer) throws IOException {
        long count = reader.readCount();
        writer.writeCount(count);
        long symbol;
        while ((symbol = reader.readSymbol()) != -1L) {
            writer.writeSymbol(symbol);
            copy(reader, writer);
        }
        writer.writeSymbol(-1L);
    }
