    private synchronized void assertLock(boolean forceNativeReassert) throws LockFailureException {
        if (!lockFile.getParentFile().isDirectory()) {
            logger.log(Level.FINE, "Lock directory does not exist for {0}", lockFile);
            throw new LockUncertainException();
        }
        if (!lockFile.exists()) {
            logger.log(Level.FINE, "Lock file no longer exists: {0}", lockFile);
            throw new NotLockedException();
        }
        try {
            FileChannel oldLockChannel = lockChannel;
            FileChannel newLockChannel = lockChannel = new RandomAccessFile(lockFile, "rw").getChannel();
            LockMetaData metaData = readLockMetaData(false);
            if (metaData == null) {
                logger.log(Level.FINE, "Lock file could not be read: {0}", lockFile);
                closeChannel(oldLockChannel);
                throw new AlreadyLockedException(null);
            }
            if (!lockToken.equals(metaData.token)) {
                logger.log(Level.FINE, "Lock was lost: {0}", lockFile);
                closeChannel(oldLockChannel);
                throw new AlreadyLockedException(metaData.extraInfo);
            }
            this.extraInfo = metaData.extraInfo;
            if (lock == null && approver != null) approver.approveLock(this, extraInfo);
            if (forceNativeReassert) {
                try {
                    if (lock != null) lock.release();
                    closeChannel(oldLockChannel);
                    Thread.sleep(100);
                } catch (Exception e) {
                    logger.log(Level.FINE, "Exception when releasing lock for native reassert", e);
                }
                lock = null;
            }
            if (lock == null) {
                lock = lockChannel.tryLock(0, 1, false);
            } else {
                this.lockChannel = oldLockChannel;
                closeChannel(newLockChannel);
            }
            if (lock == null) {
                logger.log(Level.FINE, "Lock could not be reestablished: {0}", lockFile);
                throw new AlreadyLockedException(null);
            } else {
                logger.log(Level.FINEST, "Lock is valid: {0}", lockFile);
            }
        } catch (LockFailureException fe) {
            throw fe;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Unexpected exception when asserting file lock", e);
            throw new LockFailureException(e);
        }
    }
