    public void upgrade() {
        File newSaveBackup = newSettings.getBackupFile();
        File newSave = newSettings.getSaveFile();
        if (!newSaveBackup.exists() && !newSave.exists()) {
            try {
                List<DownloadMemento> mementos = readAndConvertOldFormat();
                if (downloadSerializer.writeToDisk(mementos)) {
                    oldDownloadSettings.getSaveFile().delete();
                    oldDownloadSettings.getBackupFile().delete();
                }
            } catch (IOException iox) {
                LOG.warn("Unable to read old file or write to backup!", iox);
            }
        }
    }
