    private void loadLogFile() throws IOException {
        assert (channel == null && fos == null);
        this.currentLogFileName = createLogFileName();
        File lf = new File(currentLogFileName);
        if (!lf.getParentFile().exists() && !lf.getParentFile().mkdirs()) {
            throw new IOException("could not create parent directory for database log file");
        }
        String openMode = "";
        switch(syncMode) {
            case ASYNC:
            case FSYNC:
            case FDATASYNC:
                {
                    openMode = "rw";
                    break;
                }
            case SYNC_WRITE:
                {
                    openMode = "rwd";
                    break;
                }
            case SYNC_WRITE_METADATA:
                {
                    openMode = "rws";
                    break;
                }
        }
        fos = new RandomAccessFile(lf, openMode);
        fos.setLength(0);
        channel = fos.getChannel();
        fdes = fos.getFD();
    }
