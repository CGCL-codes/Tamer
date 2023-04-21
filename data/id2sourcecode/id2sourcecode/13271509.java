    protected static boolean lock() {
        try {
            if (!lockFile.exists()) {
                if (!lockFile.getParentFile().exists()) lockFile.getParentFile().mkdirs();
                lockFile.createNewFile();
                fileChannel = new RandomAccessFile(lockFile, "rw").getChannel();
                fileLock = fileChannel.lock();
            } else {
                fileChannel = new RandomAccessFile(lockFile, "rw").getChannel();
                try {
                    fileLock = fileChannel.tryLock();
                    if (fileLock == null) isLocked = true;
                } catch (Exception e) {
                    isLocked = true;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Error while creating the lock [" + lockFile.getPath() + "]: " + e.getMessage(), e);
        }
        return isLocked;
    }
