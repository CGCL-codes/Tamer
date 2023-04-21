    private void acquireLock() throws ErrorMessage {
        try {
            m_lockFile.createNewFile();
            m_lockFileChannel = new RandomAccessFile(m_lockFile, "rw").getChannel();
            FileLock lock = m_lockFileChannel.tryLock();
            if (lock == null) throw new ErrorMessage("Could not get lock on file '" + m_lockFile + "': already used by another instance of TwoGtp");
        } catch (IOException e) {
            throw new ErrorMessage("Could not lock file '" + m_lockFile + "': " + e.getMessage());
        }
    }
