    private void revertBackup(File backupFile, File origFile) {
        if (backupFile.exists()) {
            try {
                FileUtils.copyFile(backupFile, origFile);
                backupFile.deleteOnExit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
