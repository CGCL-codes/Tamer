    private void rotateNextLogWriter() throws IOException, FileFormatException {
        writerIndex = writerIndex + 1;
        writerHandle.putNextFile(writerIndex);
        if (readerHandle != writerHandle) {
            writerHandle.close();
        }
        db.putWriterIndex(writerIndex);
        writerHandle = createLogEntity(path + fileSeparator + filePrefix + "data_" + writerIndex + ".idb", db, writerIndex);
    }
