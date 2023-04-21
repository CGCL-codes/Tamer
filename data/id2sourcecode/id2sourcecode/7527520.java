    @Override
    public void putCert(String username, File file) throws ApsSystemException {
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
            FileUtils.copyFile(file, previous);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "putCert");
            throw new ApsSystemException("Errore in salvataggio file cert per utente " + username);
        }
    }
