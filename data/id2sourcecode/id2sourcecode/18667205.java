    public Transferrer createTransferrer(TransferredFile file, long startedAt, long toDownload) {
        return new FileSender(controller, file.getChannel(), startedAt, toDownload);
    }
