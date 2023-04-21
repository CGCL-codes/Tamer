    private void createBackupIfRequested() throws IOException {
        ApplicationProperties applicationProperties = ApplicationProperties.getInstance();
        if (applicationProperties.createBackup()) {
            File inputFile = new File(m_inputGrisbiFilePath);
            File backupFile = new File(m_backupGrisbiFilePath);
            try {
                FileUtils.copyFile(inputFile, backupFile);
                ApplicationLogger.getLogger().log(Level.INFO, "Backup created \"" + m_backupGrisbiFilePath + "\"");
            } catch (IOException e) {
                ApplicationLogger.getLogger().log(Level.SEVERE, "Unable to create a backup of the file \"" + m_inputGrisbiFilePath + "\"");
                throw e;
            }
        }
    }
