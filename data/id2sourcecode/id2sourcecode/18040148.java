    public byte[] readNextAndRemove() throws IOException, FileFormatException {
        byte[] b = null;
        try {
            b = readerHandle.readNextAndRemove();
        } catch (FileEOFException e) {
            int deleteNum = readerHandle.getCurrentFileNumber();
            int nextfile = readerHandle.getNextFile();
            readerHandle.close();
            FileRunner.addDeleteFile(path + fileSeparator + filePrefix + "data_" + deleteNum + ".idb");
            db.putReaderPosition(LogEntity.messageStartPosition);
            db.putReaderIndex(nextfile);
            if (writerHandle.getCurrentFileNumber() == nextfile) {
                readerHandle = writerHandle;
            } else {
                readerHandle = createLogEntity(path + fileSeparator + filePrefix + "data_" + nextfile + ".idb", db, nextfile);
            }
            try {
                b = readerHandle.readNextAndRemove();
            } catch (FileEOFException e1) {
                log.error("read new log file FileEOFException error occurred", e1);
            }
        }
        if (b != null) {
            db.decrementSize();
        }
        return b;
    }
