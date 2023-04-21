    public AsyncTransferrer transferAsync(Reader reader, Writer writer, boolean keepWriterOpen) {
        return transferAsync(reader, writer, keepWriterOpen, null);
    }
