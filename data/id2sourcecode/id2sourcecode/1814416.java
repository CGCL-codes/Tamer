    public void waitThreads() {
        try {
            readerThread.join();
            writerThread.join();
            fileTransferThread.join();
            graphicsThread.join();
            clipboardTransferThread.join();
            zipFileCreateThread.join();
            zipFileStoreThread.join();
            zipFileExtractThread.join();
        } catch (Exception e) {
        }
    }
