    public static void makeBackupCopy(String sourceFileName, String targetDirectory) throws IOException {
        String td = FilenameUtils.normalizeNoEndSeparator(targetDirectory);
        String targetFileName = Q3cFileUtil.backupFileName(sourceFileName);
        File targetPath = new File(td + File.separator + targetFileName);
        if (logger.isInfoEnabled()) {
            logger.info("backup file is " + targetFileName);
        }
        FileUtils.copyFile(new File(sourceFileName), targetPath);
    }
