    private void criaBackupJogoOriginal() {
        try {
            File original = ApplicationContext.arquivoOriginal;
            File backupFile = new File(original.getParentFile().getPath() + File.separator + PREFIXO_BACKUP + original.getName());
            if (backupFile.exists()) {
                FileUtils.forceDelete(backupFile);
            }
            FileUtils.copyFile(original, backupFile, false);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
