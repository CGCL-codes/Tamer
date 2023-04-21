    public File backup(final File originalFile, File backupFile) {
        FileHelper.checkFileExistsAndIsReadable(originalFile);
        if (backupFile != null) {
            FileHelper.checkFileExistsAndIsWritable(backupFile);
        }
        if (backupFile == null) {
            final String extensionSeparator = ".";
            final String originalFileName = StringUtils.substringBeforeLast(originalFile.getName(), extensionSeparator);
            final String originalFileExtension = StringUtils.substringAfterLast(originalFile.getName(), extensionSeparator);
            final StringBuilder backupFileName = new StringBuilder(originalFileName).append("_gfm-backup_").append(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())).append(extensionSeparator).append(originalFileExtension);
            backupFile = new File(originalFile.getParentFile(), backupFileName.toString());
        }
        logger.info(new StringBuilder("Sauvegarde (").append(originalFile).append(" -> ").append(backupFile).append(")...").toString());
        try {
            FileUtils.copyFile(originalFile, backupFile);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new ManagerException(new StringBuilder("Erreur lors de la sauvegarde (").append(originalFile).append(" -> ").append(backupFile).append("): ").append(e.getMessage()).toString(), e);
        }
        logger.info("Sauvegarde effectuée.");
        return backupFile;
    }
