    private void openWriter(File newActiveLogFile, File activeLogFileDirectory) throws IOException {
        synchronized (WRITER_CHANGE_GATE) {
            synchronized (PRINTERS_SEMAPHORE) {
                if (printerCount != 0) {
                    try {
                        PRINTERS_SEMAPHORE.wait();
                    } catch (InterruptedException e) {
                        throw new IllegalStateException("Interrupted while attempting to configure writer");
                    }
                }
                if (writer != tempWriter) {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            throw new IOException("Failed to close open file: " + e);
                        }
                    }
                    RandomAccessFile newFileOut = new RandomAccessFile(newActiveLogFile, "rws");
                    FileChannel channel = newFileOut.getChannel();
                    long initialChannelSize = channel.size();
                    newFileOut.seek(initialChannelSize);
                    Writer newFileWriter = Channels.newWriter(newFileOut.getChannel(), "UTF-8");
                    storeFileCreationTimeIfNecessary(activeLogFileDirectory, newActiveLogFile);
                    fileOut = newFileOut;
                    writer = newFileWriter;
                }
            }
        }
    }
