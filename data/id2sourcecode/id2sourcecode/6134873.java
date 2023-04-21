    private void createTempBackup(File[] files, String backupFolderPath, File backupFolder) throws IOException {
        backupFolder.mkdir();
        for (File f : files) FileUtils.copyFile(f, new File(backupFolderPath + File.separator + f.getName()));
    }
