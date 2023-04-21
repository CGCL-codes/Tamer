    public static void deleteFilesWithFilenameFilter(File source, FilenameFilter includeFilenameFilter, FilenameFilter excludeFilenameFilter) throws FileIsNotADirectoryException, IOException, FileIsSecurityRestrictedException {
        if (!source.isDirectory()) {
            throw new FileIsNotADirectoryException("Source file '" + source.getAbsolutePath() + "' is not a directory.");
        }
        File[] includeFilesArray;
        if (null != includeFilenameFilter) {
            includeFilesArray = source.listFiles(includeFilenameFilter);
        } else {
            includeFilesArray = source.listFiles();
        }
        if (null != includeFilesArray) {
            File[] excludeFilesArray = null;
            List<File> excludeFilesList = null;
            if (null != excludeFilenameFilter) {
                excludeFilesArray = source.listFiles(excludeFilenameFilter);
                excludeFilesList = Arrays.asList(excludeFilesArray);
            }
            if (null != excludeFilesList && (!excludeFilesList.isEmpty())) {
                for (int i = 0; i < includeFilesArray.length; i++) {
                    File currentFile = includeFilesArray[i];
                    if (!excludeFilesList.contains(currentFile)) {
                        if (currentFile.isDirectory()) {
                            deleteFilesWithFilenameFilter(currentFile, includeFilenameFilter, excludeFilenameFilter);
                        } else {
                            deleteFile(currentFile);
                        }
                    }
                }
            } else {
                for (int i = 0; i < includeFilesArray.length; i++) {
                    File currentFile = includeFilesArray[i];
                    if (currentFile.isDirectory()) {
                        deleteFilesWithFilenameFilter(currentFile, includeFilenameFilter, excludeFilenameFilter);
                    } else {
                        deleteFile(currentFile);
                    }
                }
            }
        } else {
            throw new FileIsSecurityRestrictedException("File '" + source.getAbsolutePath() + "' is security restricted.");
        }
    }
