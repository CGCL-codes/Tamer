    private void readObject(ObjectInputStream inputStream) {
        diskCache = DiskCache.INSTANCE;
        token = diskCache.transferFromStream(inputStream);
    }
