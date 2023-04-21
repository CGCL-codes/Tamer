    public void takeSnapshot(BigInteger version) {
        try {
            lock.writeLock();
            File snapshot = getSnapshotFile(version);
            FileUtils.copyFile(fileObj, snapshot);
        } catch (FileNotFoundException ffe) {
            throw new GlooException(ffe);
        } catch (IOException e) {
            throw new GlooException(e);
        } finally {
            lock.writeUnlock();
        }
    }
