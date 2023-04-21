        public void acquire(File file) {
            this.file = file;
            try {
                lockingChannel = new RandomAccessFile(file, "rw").getChannel();
            } catch (FileNotFoundException e) {
                throw new CouldNotGetLockException("Could not get lock for file " + file.getPath() + " because it does not exist.");
            }
            try {
                javalock = lockingChannel.tryLock(Long.MAX_VALUE - 1, 1, false);
            } catch (IOException e) {
                throw new CouldNotGetLockException("Could not get lock for file " + file.getPath() + " because " + e.getMessage() + "\n\nTry restarting GmanDA and make sure that you only have a single instance of GmanDA running.");
            }
            if (javalock == null || !javalock.isValid()) {
                throw new CouldNotGetLockException("Could not get lock for file " + file.getPath() + ".\n\nTry restarting GmanDA and make sure that you only have a single instance of GmanDA running.");
            }
            locksByFile.put(file, this);
        }
