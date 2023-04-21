        public ChatService(String username, String password) {
            this.username = username;
            this.password = password;
            this.connection = null;
            readWriteLock = new java.util.concurrent.locks.ReentrantReadWriteLock();
            readerLock = readWriteLock.readLock();
            writerLock = readWriteLock.writeLock();
        }
