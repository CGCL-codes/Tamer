    public static void deleteFilesWithFileFilter(File source, FileFilter includeFileFilter, FileFilter excludeFileFilter) throws FileIsNotADirectoryException, IOException, FileIsSecurityRestrictedException {
        if (!source.isDirectory()) {
            throw new FileIsNotADirectoryException("Source file '" + source.getAbsolutePath() + "' is not a directory.");
        }
        File[] includeFilesArray;
        if (null != includeFileFilter) {
            includeFilesArray = source.listFiles(includeFileFilter);
        } else {
            includeFilesArray = source.listFiles();
        }
        if (null != includeFilesArray) {
            File[] excludeFilesArray = null;
            List<File> excludeFilesList = null;
            if (null != excludeFileFilter) {
                excludeFilesArray = source.listFiles(excludeFileFilter);
                excludeFilesList = Arrays.asList(excludeFilesArray);
            }
            if (null != excludeFilesList && (!excludeFilesList.isEmpty())) {
                for (int i = 0; i < includeFilesArray.length; i++) {
                    File currentFile = includeFilesArray[i];
                    if (!excludeFilesList.contains(currentFile)) {
                        if (currentFile.isDirectory()) {
                            deleteFilesWithFileFilter(currentFile, includeFileFilter, excludeFileFilter);
                        } else {
                            deleteFile(currentFile);
                        }
                    }
                }
            } else {
                for (int i = 0; i < includeFilesArray.length; i++) {
                    File currentFile = includeFilesArray[i];
                    if (currentFile.isDirectory()) {
                        deleteFilesWithFileFilter(currentFile, includeFileFilter, excludeFileFilter);
                    } else {
                        deleteFile(currentFile);
                    }
                }
            }
        } else {
            throw new FileIsSecurityRestrictedException("File '" + source.getAbsolutePath() + "' is security restricted.");
        }
    }
