    protected void lockProject() {
        if (!RuntimePreferences.isProjectLockingEnabled()) {
            return;
        }
        try {
            File lockFile = new File(m_config.getProjectRoot(), OConsts.FILE_PROJECT);
            lockChannel = new RandomAccessFile(lockFile, "rw").getChannel();
            lock = lockChannel.lock();
        } catch (Exception ex) {
            Log.log(ex);
        }
    }
