    public void acquireLock(String pipeName) throws FileIOException {
        try {
            raf = new RandomAccessFile(new File(pipeName), "rwd");
            fChannel = raf.getChannel();
        } catch (FileNotFoundException e) {
            log.error(resBundle.getString(MessageConstants.JPipeNotFound.getMessageKey()), e);
            throw new FileIOException(MessageConstants.JPipeNotFound);
        }
        try {
            fLock = fChannel.tryLock();
        } catch (IOException e1) {
        }
        int tryOut = 0;
        while (fLock == null && tryOut < jPipeConstants.TIME_OUT) {
            try {
                fLock = fChannel.tryLock();
            } catch (IOException e) {
                log.error("Exception in acquiring lock", e);
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            tryOut++;
        }
        if (fLock != null) {
            log.info(resBundle.getString(MessageConstants.JPipeLockAcquired.getMessageKey()) + pipeName);
            return;
        }
        throw new FileIOException(MessageConstants.JPipeAcquireLockFail);
    }
