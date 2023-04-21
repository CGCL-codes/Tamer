    public void doBackup(String backupDirectory) throws IOException {
        try {
            FileUtils.copyFile(new File(backupDirectory, "user.bak"), new File(backupDirectory, "user.ba2"));
            FileUtils.copyFile(new File(backupDirectory, "realms.bak"), new File(backupDirectory, "realms.ba2"));
            FileUtils.copyFile(new File(backupDirectory, "realmpwd.bak"), new File(backupDirectory, "realmpwd.ba2"));
        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage());
            }
        }
        FileUtils.copyFile(userConfigurationFile, new File(backupDirectory, "user.bak"));
        FileUtils.copyFile(realmsConfigurationFile, new File(backupDirectory, "realms.bak"));
        FileUtils.copyFile(new File(cm.getSecurityDirectory(), "realms.dat"), new File(backupDirectory, "realmpwd.bak"));
    }
