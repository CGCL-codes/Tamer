    public void saveFile(InputStream in, String fileName, String fileKey, ProgressListener listener, int batchNo, long maxBatchSize, boolean isFinalBatch) throws IOException {
        int splitNo = 0;
        int readBytes = 0;
        byte buffer[];
        StringBuffer logMessage = new StringBuffer("Saved Entries:\n");
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        Date start = new Date();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            if (batchNo > 0) {
                int maxEntries = (int) (maxBatchSize / MAX_BLOB_SIZE);
                splitNo = maxEntries * batchNo;
                String query = "select from " + TmpFile.class.getName() + " where " + TmpFile.COL_KEY + " == '" + fileKey + "'" + " && " + TmpFile.COL_SPLIT_NO + ">=" + splitNo + " && " + TmpFile.COL_SPLIT_NO + "<" + (splitNo + maxEntries);
                List<TmpFile> fileList = (List) pm.newQuery(query).execute();
                if (fileList.size() > 0) {
                    pm.deletePersistentAll(fileList);
                    logger.logIt("Removed existing entries from " + splitNo + " to " + (splitNo + fileList.size() - 1));
                }
                listener.resetCurrentSize(splitNo * MAX_BLOB_SIZE);
                logger.logIt("Starting with splitNo:" + splitNo);
            } else {
                logger.logIt("First batch, so Starting with splitNo:" + splitNo);
            }
            do {
                buffer = new byte[MAX_BLOB_SIZE];
                readBytes = in.read(buffer);
                if (readBytes < 0) {
                    if (splitNo == 0) {
                        persist(fileKey, new byte[0], fileName, splitNo, true);
                    }
                    break;
                } else {
                    while (readBytes > -1 && byteStream.size() < MAX_BLOB_SIZE) {
                        byteStream.write(buffer, 0, readBytes);
                        listener.updateStatus(readBytes);
                        buffer = new byte[MAX_BLOB_SIZE - byteStream.size()];
                        readBytes = in.read(buffer);
                    }
                    logMessage.append("SplitNo:").append(splitNo);
                    logMessage.append(", bufferSize:").append(byteStream.size());
                    logMessage.append(", readBytes:").append(readBytes);
                    boolean isFinalPart = (isFinalBatch && readBytes < 0) ? true : false;
                    logMessage.append(", isFinalPart:").append(isFinalPart);
                    logMessage.append(", Start at:").append(new Date());
                    persist(fileKey, byteStream.toByteArray(), fileName, splitNo, isFinalPart);
                    logMessage.append(", End at:").append(new Date()).append("\n");
                    byteStream = new ByteArrayOutputStream();
                    splitNo++;
                }
            } while (readBytes >= 0 && splitNo < MAX_NO_OF_SPLITS);
        } finally {
            if (!pm.isClosed()) {
                pm.close();
            }
            logger.logIt(logMessage.toString());
        }
        Date end = new Date();
        logger.logIt("writeFile - time taken (ms):" + (end.getTime() - start.getTime()));
    }
