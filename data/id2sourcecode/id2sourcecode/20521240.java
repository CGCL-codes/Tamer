    private ContainerLocation rawPersist(byte[] rawData) {
        checkReadOnly("cannot write on read only store; this should never happen");
        int totalSize = rawData.length + DataFileManager.getDataHeaderSize();
        ContainerLocation result;
        FreeSpace freeSpace;
        synchronized (getDataFileManager()) {
            freeSpace = getFreeSpaceManager().findFreeSpace(totalSize);
            if (freeSpace == null) {
                if (log.isLoggable(Level.FINEST)) log.finest("no free space: creating new data file");
                int dataFileId = getDataFileManager().createDataFile();
                freeSpace = new FreeSpace(dataFileId, 0, getDataFileManager().getMaxDataFileSize());
            }
            FreeSpace newFreeSpace = getFreeSpaceManager().divide(freeSpace, totalSize);
            if (newFreeSpace != null) {
                if (log.isLoggable(Level.FINEST)) log.finest("free space was split into " + freeSpace + " and " + newFreeSpace);
                ContainerLocation loc = new ContainerLocation(newFreeSpace.getDataFileId(), newFreeSpace.getLocation());
                getDataFileManager().wipeData(loc, newFreeSpace.getSize());
                getFreeSpaceManager().registerFreeSpace(newFreeSpace);
            }
        }
        result = new ContainerLocation(freeSpace.getDataFileId(), freeSpace.getLocation(), freeSpace.getSize());
        if (log.isLoggable(Level.FINEST)) log.finest("storing data in " + freeSpace);
        getDataFileManager().storeData(result, rawData, freeSpace.getSize());
        return result;
    }
