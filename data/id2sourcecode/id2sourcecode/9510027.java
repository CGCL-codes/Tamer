    @Override
    Reader doGet() throws IOException {
        try {
            final IndexReader reader = IndexReader.open(writer, true);
            return Reader.of(reader, true);
        } catch (IndexNotFoundException e) {
            return ReaderSuppliers.empty().get();
        }
    }
