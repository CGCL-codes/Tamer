    public File getPreviewFile() {
        if (isFileCompletedOrMoved()) {
            return getDestinationFile();
        }
        if (!memoryFile.isFileBeginningAvailable()) {
            return null;
        }
        long previewLength = memoryFile.getFileBeginningScopeLength();
        StringBuffer fullFileNameBuf = new StringBuffer();
        fullFileNameBuf.append(DownloadPrefs.IncompleteDirectory.get());
        fullFileNameBuf.append(File.separatorChar);
        fullFileNameBuf.append("PREVIEW");
        fullFileNameBuf.append("-");
        fullFileNameBuf.append(fileName);
        File previewFile = new File(fullFileNameBuf.toString());
        previewFile.deleteOnExit();
        try {
            FileUtils.copyFile(incompleteManagedFile.getFile(), previewFile, previewLength);
            return previewFile;
        } catch (IOException exp) {
            return null;
        }
    }
