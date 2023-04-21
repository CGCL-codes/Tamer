    public void start(RvConnection transfer, StateController last) {
        try {
            Map<TransferredFile, Long> checksums = new HashMap<TransferredFile, Long>();
            if (transfer instanceof OutgoingFileTransfer) {
                OutgoingFileTransfer otransfer = (OutgoingFileTransfer) transfer;
                List<TransferredFile> files = otransfer.getFiles();
                for (TransferredFile tfile : files) {
                    File file = tfile.getRealFile();
                    RandomAccessFile raf = new RandomAccessFile(file, "r");
                    Checksummer summer = new ChecksummerImpl(raf.getChannel(), raf.length());
                    otransfer.getEventPost().fireEvent(new ChecksummingEvent(tfile, summer));
                    checksums.put(tfile, summer.compute());
                }
            }
            fireSucceeded(new ComputedChecksumsInfo(checksums));
        } catch (IOException e) {
            fireFailed(e);
        }
    }
