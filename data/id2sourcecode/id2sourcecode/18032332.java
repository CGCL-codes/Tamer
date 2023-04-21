    public void restore(String backupDirectory) throws IOException {
        FileUtils.copyFile(new File(backupDirectory, "user.bak"), userConfigurationFile);
        FileUtils.copyFile(new File(backupDirectory, "realms.bak"), realmsConfigurationFile);
        FileUtils.copyFile(new File(backupDirectory, "realmpwd.bak"), new File(cm.getSecurityDirectory(), "realms.dat"));
    }
