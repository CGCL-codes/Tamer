    public FSQueue(String dir, int fileLimitLength) throws Exception {
        this.fileLimitLength = fileLimitLength;
        File fileDir = new File(dir);
        if (fileDir.exists() == false && fileDir.isDirectory() == false) {
            if (fileDir.mkdirs() == false) {
                throw new IOException("create dir error");
            }
        }
        path = fileDir.getAbsolutePath();
        db = new LogIndex(path + fileSeparator + dbName);
        writerIndex = db.getWriterIndex();
        readerIndex = db.getReaderIndex();
        writerHandle = createLogEntity(path + fileSeparator + filePrefix + "data_" + writerIndex + ".idb", db, writerIndex);
        if (readerIndex == writerIndex) {
            readerHandle = writerHandle;
        } else {
            readerHandle = createLogEntity(path + fileSeparator + filePrefix + "data_" + readerIndex + ".idb", db, readerIndex);
        }
        FileRunner deleteFileRunner = new FileRunner(path + fileSeparator + filePrefix + "data_", fileLimitLength);
        executor.execute(deleteFileRunner);
    }
