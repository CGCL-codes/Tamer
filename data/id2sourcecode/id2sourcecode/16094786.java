    public static boolean isLocked() {
        AppInfo.checkState();
        lockPath = FS.makeConfigPath(AppInfo.internalName + ".lock");
        try {
            fileLock = new FileOutputStream(lockPath).getChannel().tryLock();
        } catch (FileNotFoundException exception) {
            return false;
        } catch (IOException exception) {
            return false;
        }
        return (fileLock == null);
    }
