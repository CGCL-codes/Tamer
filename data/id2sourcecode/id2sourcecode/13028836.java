    private void createFoldersAndPrepare(String[] filePaths) throws IOException {
        log.debug("filePaths = " + Arrays.toString(filePaths));
        if (StringUtils.isEmpty(outputFileName)) outputFileName = settings.getMergeFileName();
        if (StringUtils.isEmpty(outputFileName)) outputFileName = "merged.pdf";
        outputFolder = Utils.getStringFromTemplate(settings.getOutputFolder(), variables, "");
        backupFolder = Utils.getStringFromTemplate(settings.getBackupFolder(), variables, "") + "/merge/files";
        File outputDir = new File(outputFolder);
        if (!outputDir.exists()) FileUtils.forceMkdir(outputDir);
        File backupDir = new File(backupFolder);
        if (!backupDir.exists()) FileUtils.forceMkdir(backupDir);
        for (String filePath : filePaths) {
            String fileName = FilenameUtils.getName(filePath);
            File file = new File(backupFolder + "/" + fileName);
            FileUtils.copyFile(new File(filePath), file);
            streamOfPDFFiles.add(new FileInputStream(file));
        }
    }
