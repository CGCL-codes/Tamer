    private void copyWriteIndexToReadIndex(File readIndexLocation, File writeIndexLocation) {
        IndexManager.getInstance().aquireWriteLock();
        String indexFileName = null;
        File renameFile = null;
        for (File indexFile : writeIndexLocation.listFiles()) {
            if (indexFile.isFile()) {
                indexFileName = indexFile.getName();
                renameFile = new File(readIndexLocation, indexFileName);
                indexFile.renameTo(renameFile);
            }
        }
        IndexManager.getInstance().releaseWriteLock();
    }
