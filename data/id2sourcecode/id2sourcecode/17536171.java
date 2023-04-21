    public long transfer(Reader reader, Writer writer, boolean keepWriterOpen) throws RuntimeIoException {
        ReaderTransferrer transferrer = new ReaderTransferrer(reader, writer, keepWriterOpen, null);
        long bytes = transferrer.transfer();
        return bytes;
    }
