    private void restore() {
        try {
            restore(cm.getBackupDirectory());
            FileUtils.copyFile(new File(cm.getBackupDirectory(), "realmpwd.bak"), new File(cm.getSecurityDirectory(), "realms.dat"));
        } catch (IOException e) {
        }
    }
