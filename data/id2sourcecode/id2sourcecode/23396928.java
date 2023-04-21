    public synchronized TaskDataState get(String repositoryUrl, String id) {
        TaskDataState state = null;
        FileInputStream fileInputStream = null;
        FileLock lock = null;
        try {
            File dataFile = getDataFile(URLEncoder.encode(repositoryUrl, ENCODING_UTF_8), id);
            if (dataFile != null && dataFile.exists()) {
                fileInputStream = new FileInputStream(dataFile);
                FileChannel channel = fileInputStream.getChannel();
                lock = channel.tryLock(0L, Long.MAX_VALUE, true);
                if (lock != null) {
                    state = new TaskDataState(repositoryUrl, id);
                    ZipInputStream inputStream = new ZipInputStream(fileInputStream);
                    ZipEntry entry = inputStream.getNextEntry();
                    if (entry != null) {
                        XMLMemento input = XMLMemento.createReadRoot(new InputStreamReader(inputStream, ENCODING_UTF_8));
                        if (input != null) {
                            readData(state, input);
                        }
                    }
                }
            }
        } catch (Exception e) {
            StatusHandler.fail(e, "Error retrieving offline data", false);
        } finally {
            try {
                if (lock != null && lock.isValid()) {
                    lock.release();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                StatusHandler.fail(e, "Error closing offline data input stream", false);
            }
        }
        return state;
    }
