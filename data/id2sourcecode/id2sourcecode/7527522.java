    @Override
    public void deleteCert(String username) throws ApsSystemException {
        try {
            String dir = this.getSignaturesFolderPath() + username;
            this.checkAndMakeFolder(dir);
            String filename = this.buildCertFilename(username);
            String filePath = dir + System.getProperty("file.separator") + filename;
            File previous = new File(filePath);
            if (previous.exists()) {
                String backupValue = DateConverter.getFormattedDate(new Date(), "yyyyMMddhhmmss");
                File backup = new File(previous.getAbsolutePath() + "." + backupValue);
                FileUtils.copyFile(previous, backup);
                FileUtils.forceDelete(previous);
            }
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "deleteCert");
            throw new ApsSystemException("Errore in eliminazione file cert per utente " + username);
        }
    }
