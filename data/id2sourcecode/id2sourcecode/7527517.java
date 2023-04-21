    @Override
    public void deleteKeyStore(String username) throws ApsSystemException {
        try {
            String dir = this.getSignaturesFolderPath() + username;
            this.checkAndMakeFolder(dir);
            String filename = this.buildKeystoreFilename(username);
            String filePath = dir + System.getProperty("file.separator") + filename;
            File previous = new File(filePath);
            if (previous.exists()) {
                String backupValue = DateConverter.getFormattedDate(new Date(), "yyyyMMddhhmmss");
                File backup = new File(previous.getAbsolutePath() + "." + backupValue);
                FileUtils.copyFile(previous, backup);
                FileUtils.forceDelete(previous);
            }
            this.getFirmaDigitaleDAO().delete(username);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "deleteKeyStore");
            throw new ApsSystemException("Errore in eliminazione file keystore per utente " + username);
        }
    }
