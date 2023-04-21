    @Override
    public void moveFiles(FileWrapper fromDir, String filePattern, boolean matchPattern, FileWrapper toDir) throws FileNotFoundException, IOException {
        if (StringUtilities.isEmpty(filePattern)) {
            throw new IllegalArgumentException("filePattern arg cannot be empty or null");
        }
        if (!fromDir.isDirectory()) {
            throw new IllegalArgumentException("Expected fromDir(" + fromDir.getAbsolutePath() + ") to be a directory.");
        }
        if (!toDir.isDirectory()) {
            throw new IllegalArgumentException("Expected toDir(" + toDir.getAbsolutePath() + ") to be a directory.");
        }
        List<FileWrapper> filesToMove = getFilterFileList(fromDir, filePattern, matchPattern);
        for (FileWrapper file : filesToMove) {
            copyFile(file, toDir);
            if (s_log.isDebugEnabled()) {
                s_log.debug("moveFiles: Attempting to delete file " + file.getAbsolutePath());
            }
            if (file.delete()) {
                s_log.error("moveFiles: Unable to delete file " + file.getAbsolutePath());
            }
        }
    }
