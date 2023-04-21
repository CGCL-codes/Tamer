    public void waitThreads() {
        try {
            session.getClipboardTransferThread().join();
            readerThread.join();
            writerThread.join();
            reader.dispose();
            writer.dispose();
        } catch (InterruptedException e) {
        }
    }
