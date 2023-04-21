    public FixedRecordFile(final File aFileName) throws IOException {
        if (!aFileName.exists()) {
            if (!aFileName.createNewFile()) {
                throw new IOException("file '" + aFileName.getAbsolutePath() + "' could not be created.");
            }
        }
        setFileName(aFileName);
        long recordCnt = aFileName.length() / getRecordLength();
        if (recordCnt > Integer.MAX_VALUE) {
            throw new IllegalStateException("recordcount too large to be represented as an integer");
        }
        setRecordCount((int) recordCnt);
        myFileChannel = new RandomAccessFile(aFileName, "rw").getChannel();
        try {
            this.memoryMapped = myFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, aFileName.length());
        } catch (Exception e) {
            LOG.log(Level.INFO, "Memory-maping the file failed in " + getClass().getName() + " - using conventional io instead");
        }
    }
