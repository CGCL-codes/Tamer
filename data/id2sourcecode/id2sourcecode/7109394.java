    public static boolean isLocked() {
        AppInfo.checkState();
        file = FS.makeConfigPath(AppInfo.internalName + ".lock");
        try {
            lock = new FileOutputStream(file).getChannel().tryLock();
        } catch (FileNotFoundException exception) {
            return false;
        } catch (IOException exception) {
            return false;
        }
        return (lock == null);
    }
