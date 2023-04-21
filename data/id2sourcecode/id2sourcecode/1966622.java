    private void replaceReadOnlyIndex(File readIndexLocation, File writeIndexLocation) {
        IndexManager.getInstance().aquireWriteLock();
        for (File indexFile : readIndexLocation.listFiles()) {
            if (indexFile.isFile()) {
                indexFile.delete();
            }
        }
        copyWriteIndexToReadIndex(readIndexLocation, writeIndexLocation);
        IndexManager.getInstance().releaseWriteLock();
    }
