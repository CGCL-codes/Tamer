    private void copyFiles() {
        Iterator it = filesToCopy.iterator();
        while (it.hasNext()) {
            CopyInfo ci = (CopyInfo) it.next();
            try {
                FileUtils.copyFile(ci.fromFile, ci.toFile);
                logger.info("File copied: " + ci.fromFile.getAbsolutePath() + " ==> " + ci.toFile.getAbsolutePath());
            } catch (IOException e) {
                errorReporter.warning(ErrorReporter.FILES_COPIER_ORIGIN, messages.format("failedToCopyFile", ci.fromFile.getAbsolutePath(), ci.toFile.getAbsolutePath(), e.getMessage()));
            }
        }
    }
