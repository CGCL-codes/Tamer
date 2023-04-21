    private File touchCachedFile(File oldFile) {
        try {
            File newFile = TempFileFactory.get().createTempFile("tempImportedTimeLog", ".xml");
            FileUtils.copyFile(oldFile, newFile);
            return newFile;
        } catch (IOException e) {
            return null;
        }
    }
