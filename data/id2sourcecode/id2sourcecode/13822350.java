    public boolean lock() {
        lockFile = new File(".lock");
        if (!lockFile.exists()) {
            try {
                boolean created = lockFile.createNewFile();
                if (!created) {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("Lock file is not created");
                    }
                    return false;
                }
            } catch (IOException e) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.throwing(HachikadukiLocker.class.getName(), "lock", e);
                }
                return false;
            }
        }
        try {
            lockChannel = new RandomAccessFile(lockFile, "rw").getChannel();
            lock = lockChannel.tryLock();
            if (lock == null) {
                return false;
            }
            return true;
        } catch (IOException e) {
            if (logger.isLoggable(Level.FINE)) {
                logger.throwing(HachikadukiLocker.class.getName(), "lock", e);
            }
            return false;
        } catch (OverlappingFileLockException e) {
            if (logger.isLoggable(Level.FINE)) {
                logger.throwing(HachikadukiLocker.class.getName(), "lock", e);
            }
            return false;
        }
    }
