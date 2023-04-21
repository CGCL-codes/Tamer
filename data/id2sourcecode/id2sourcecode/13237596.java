    public void restoreSnapshot(BigInteger version) {
        try {
            lock.writeLock();
            File snapshot = getSnapshotFile(version);
            if (!snapshot.exists()) {
                file.close();
                if (!fileObj.delete()) throw new GlooException(String.format("Cannot delete old %s file. Please remove manually and restart.", fileObj.getName()));
                createStorageFile();
                return;
            }
            FileUtils.copyFile(snapshot, fileObj);
        } catch (FileNotFoundException ffe) {
            throw new GlooException(ffe);
        } catch (IOException e) {
            throw new GlooException(e);
        } finally {
            lock.writeUnlock();
        }
    }
