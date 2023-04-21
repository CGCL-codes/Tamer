    public synchronized void acquireLock(String message, LockMessageHandler listener, String extraInfo) throws SentLockMessageException, AlreadyLockedException, LockFailureException {
        try {
            if (approver != null) approver.approveLock(this, extraInfo);
            lockChannel = new RandomAccessFile(lockFile, "rw").getChannel();
            lock = lockChannel.tryLock(0, 1, false);
            if (lock != null) {
                if (listener != null) messageHandler = new LockWatcher(listener, extraInfo); else writeLockMetaData("", 0, lockToken, extraInfo);
                this.extraInfo = extraInfo;
                registerShutdownHook();
            } else {
                tryToSendMessage(message);
            }
        } catch (LockFailureException fe) {
            throw fe;
        } catch (Exception e) {
            releaseLock();
            throw new CannotCreateLockException(e);
        }
        logger.log(Level.FINE, "Obtained lock for: {0}", lockFile);
    }
