    public void doWeeklyBackup(String backupDirectory) throws IOException {
        FileUtils.copyFile(userConfigurationFile, new File(backupDirectory, "user.wek"));
        FileUtils.copyFile(realmsConfigurationFile, new File(backupDirectory, "realms.wek"));
        FileUtils.copyFile(new File(cm.getSecurityDirectory(), "realms.dat"), new File(backupDirectory, "realmpwd.wek"));
    }
