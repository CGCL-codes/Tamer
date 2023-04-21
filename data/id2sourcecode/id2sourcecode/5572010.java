    private boolean initializeLockFile(final Language language) {
        FileAccess.writeFile("frost-lock", runLockFile);
        try {
            lockChannel = new RandomAccessFile(runLockFile, "rw").getChannel();
            fileLock = null;
            try {
                fileLock = lockChannel.tryLock();
            } catch (final OverlappingFileLockException e) {
            }
        } catch (final Exception e) {
        }
        if (fileLock == null) {
            MiscToolkit.showMessage(language.getString("Frost.lockFileFound") + "'" + runLockFile.getAbsolutePath() + "'", JOptionPane.ERROR_MESSAGE, "ERROR: Found Frost lock file 'frost.lock'.");
            return false;
        }
        return true;
    }
