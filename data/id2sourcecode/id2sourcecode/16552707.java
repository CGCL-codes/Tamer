    public boolean getLock() {
        int tryCount = 0;
        if (!lockFile.exists()) {
            try {
                lockFile.createNewFile();
            } catch (Exception e) {
                logger.info(e.toString());
            }
        }
        try {
            fileChannel = new RandomAccessFile(lockFile, "rw").getChannel();
            while (tryCount < 5) {
                fileLock = fileChannel.tryLock();
                if (fileLock != null) return true;
                if (random == null) random = new Random();
                Thread.sleep(random.nextInt(20));
                tryCount++;
            }
            if (lockFile.exists()) {
                lockFile.delete();
                return getLock();
            }
        } catch (Exception ignore) {
        }
        return false;
    }
