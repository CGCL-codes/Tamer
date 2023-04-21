    public AsyncTransferrer transferAsync(Reader reader, Writer writer, boolean keepWriterOpen, TransferCallback callback) {
        ReaderTransferrer transferrer = new ReaderTransferrer(reader, writer, keepWriterOpen, callback);
        AsyncTransferrerImpl task = new AsyncTransferrerImpl(transferrer);
        this.executor.execute(task);
        return task;
    }
