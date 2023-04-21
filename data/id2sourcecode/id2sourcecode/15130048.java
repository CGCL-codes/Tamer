    private void revert() throws IOException {
        List<String> filePaths = FileUtils.readLines(params.getTracedFile());
        filePaths = FILTER.transform(filePaths);
        System.out.println("reverting original files...");
        pb = TerminalProgressBar.newInstance(0, filePaths.size());
        int count = 0;
        for (String filePath : filePaths) {
            File backupFile = new File(filePath);
            if (backupFile.exists()) {
                FileUtils.copyFile(backupFile, revertFromBackupFile(backupFile));
                FileUtils.deleteQuietly(backupFile);
            } else {
                System.err.println(filePath + ": file not found");
            }
            pb.setValue(++count);
        }
    }
