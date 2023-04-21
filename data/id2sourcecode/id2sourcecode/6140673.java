    public static synchronized boolean isLocked() {
        lockFile = FS.makeConfigFile(getInternalName() + ".lock");
        try {
            fileLock = new FileOutputStream(lockFile).getChannel().tryLock();
        } catch (IOException exception) {
            return false;
        }
        return (fileLock == null);
    }
