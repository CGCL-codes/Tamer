    public SerializationStreamReader createReader() {
        reader = new ReaderImpl(writer.getByteArray());
        return reader;
    }
