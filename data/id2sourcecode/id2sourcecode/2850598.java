    public boolean lockEnvironment(boolean readOnly, boolean exclusive) throws DatabaseException {
        try {
            if (checkEnvHomePermissions(readOnly)) {
                return true;
            }
            if (lockFile == null) {
                lockFile = new RandomAccessFile(new File(dbEnvHome, LOCK_FILE), FileMode.READWRITE_MODE.getModeValue());
            }
            channel = lockFile.getChannel();
            boolean throwIt = false;
            try {
                if (exclusive) {
                    exclLock = channel.tryLock(1, 1, false);
                    if (exclLock == null) {
                        return false;
                    }
                    return true;
                } else {
                    if (readOnly) {
                        envLock = channel.tryLock(1, 1, true);
                    } else {
                        envLock = channel.tryLock(0, 1, false);
                    }
                    if (envLock == null) {
                        throwIt = true;
                    }
                }
            } catch (OverlappingFileLockException e) {
                throwIt = true;
            }
            if (throwIt) {
                throw new LogException("A " + LOCK_FILE + " file exists in " + dbEnvHome.getAbsolutePath() + " The environment can not be locked for " + (readOnly ? "shared" : "single writer") + " access.");
            }
        } catch (IOException IOE) {
            throw new LogException(IOE.toString());
        }
        return true;
    }
