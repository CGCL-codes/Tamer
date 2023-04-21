    public void run() throws Exception {
        if (!params.isRevertOriState() && !params.isClearBackupFiles()) {
            parseRules();
            loadNewAbsoluteClassName();
            targetFiles = selectFiles();
            System.out.println("replacing all occurrences...");
            pb = TerminalProgressBar.newInstance(0, targetFiles.size());
            List<String> files = new ArrayList<String>();
            int count = 0;
            File backupFile = null;
            for (File selectedFile : targetFiles) {
                if (params.isBackupSelectedFiles()) {
                    backupFile = createBackupFile(selectedFile);
                    if (!backupFile.exists()) {
                        FileUtils.copyFile(selectedFile, backupFile);
                    }
                    files.add(backupFile.getAbsolutePath());
                }
                File tmpFile = copyAndModifyFile(selectedFile);
                if (tmpFile != null) {
                    FileUtils.copyFile(tmpFile, selectedFile);
                    if (params.isBackupSelectedFiles()) {
                        files.add(tmpFile.getAbsolutePath());
                    } else {
                        FileUtils.deleteQuietly(tmpFile);
                    }
                } else {
                    if (params.isBackupSelectedFiles()) {
                        FileUtils.deleteQuietly(backupFile);
                        files.remove(files.size() - 1);
                    }
                }
                pb.setValue(++count);
            }
            if (files.size() > 0) {
                FileUtils.writeLines(params.getTracedFile(), files);
            }
        } else {
            if (params.isRevertOriState()) {
                params.setClearBackupFiles(true);
                boolean isRevert = true;
                if (params.isInteractive()) {
                    InteractiveInputScanner scanner = new InteractiveInputScannerImpl.Builder().prompt("are you really want to revert ? [Ny]").inputPattern(InteractiveInputScannerImpl.YES_NO_PATTERN).defaultInput("n").build();
                    String userInput = scanner.waitInput();
                    if (!userInput.matches("^[yY]$")) {
                        isRevert = false;
                    }
                }
                if (isRevert) {
                    revert();
                } else {
                    System.err.println("revert operation cancelled");
                    System.exit(0);
                }
                params.setInteractive(false);
            }
            if (params.isClearBackupFiles()) {
                boolean isClear = true;
                if (params.isInteractive()) {
                    InteractiveInputScanner scanner = new InteractiveInputScannerImpl.Builder().prompt("are you really want to clear temporary files ? [Ny]").inputPattern(InteractiveInputScannerImpl.YES_NO_PATTERN).defaultInput("n").build();
                    String userInput = scanner.waitInput();
                    if (!userInput.matches("^[yY]$")) {
                        isClear = false;
                    }
                }
                if (isClear) {
                    clear();
                } else {
                    System.err.println("clear operation cancelled");
                }
            }
        }
    }
