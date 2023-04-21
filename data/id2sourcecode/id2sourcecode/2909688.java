    public boolean lockEnvironment(boolean rdOnly, boolean exclusive) {
        try {
            if (checkEnvHomePermissions(rdOnly)) {
                return true;
            }
            if (lockFile == null) {
                lockFile = new RandomAccessFile(new File(dbEnvHome, LOCK_FILE), FileMode.READWRITE_MODE.getModeValue());
            }
            channel = lockFile.getChannel();
            try {
                if (exclusive) {
                    exclLock = channel.tryLock(1, 1, false);
                    if (exclLock == null) {
                        return false;
                    }
                    return true;
                }
                if (rdOnly) {
                    envLock = channel.tryLock(1, 1, true);
                } else {
                    envLock = channel.tryLock(0, 1, false);
                }
                if (envLock == null) {
                    return false;
                }
                return true;
            } catch (OverlappingFileLockException e) {
                return false;
            }
        } catch (IOException e) {
            throw new EnvironmentFailureException(envImpl, EnvironmentFailureReason.LOG_INTEGRITY, e);
        }
    }
