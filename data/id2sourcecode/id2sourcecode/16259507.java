    public ConcurrencyLock(File lockFile, String message, Listener listener) throws SentMessageException, AlreadyLockedException, FailureException {
        try {
            this.lockFile = lockFile.getCanonicalFile();
            lockChannel = new RandomAccessFile(lockFile, "rw").getChannel();
            lock = lockChannel.tryLock(0, 1, false);
            if (lock != null) {
                if (listener != null) messageHandler = new MessageHandler(listener);
                registerShutdownHook();
            } else {
                tryToSendMessage(message);
            }
        } catch (FailureException fe) {
            throw fe;
        } catch (Exception e) {
            unlock();
            FailureException fe = new FailureException();
            fe.initCause(e);
            throw fe;
        }
    }
