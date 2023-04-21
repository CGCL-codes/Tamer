    protected void moveImportFilesToArchive(final Set<String> importedFiles) {
        if (!importedFiles.isEmpty()) {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd-hh-mm-ss");
            final String fullPathToArchiveFolder = pathToArchiveFolder;
            File dir = new File(fullPathToArchiveFolder + File.separator + dateFormat.format(new Date()) + File.separator);
            dir.mkdirs();
            for (String importFileName : importedFiles) {
                try {
                    File importFile = new File(importFileName);
                    FileUtils.copyFileToDirectory(importFile, dir, true);
                    importFile.delete();
                } catch (IOException e) {
                    LOG.error(MessageFormat.format("Cant move file {0} to folder {1}", importFileName, dir.getAbsolutePath()), e);
                }
            }
        }
    }
