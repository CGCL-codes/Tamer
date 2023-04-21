    public void doBackup(String backupDirectory) throws IOException {
        FileUtils.copyFile(userConfigurationFile, new File(backupDirectory, "user.bak"));
        FileUtils.copyFile(realmsConfigurationFile, new File(backupDirectory, "realms.bak"));
    }
