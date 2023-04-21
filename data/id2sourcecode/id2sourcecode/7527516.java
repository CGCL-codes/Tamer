    @Override
    public void putKeyStore(FirmaDigitale firmaDigitale) throws ApsSystemException {
        String username = firmaDigitale.getUsername();
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
            FileUtils.copyFile(firmaDigitale.getKeyStore(), previous);
            this.getFirmaDigitaleDAO().insert(firmaDigitale);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "putKeyStore");
            throw new ApsSystemException("Errore in salvataggio file keystore per utente " + username);
        }
    }
