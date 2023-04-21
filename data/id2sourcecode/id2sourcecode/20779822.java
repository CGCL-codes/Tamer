    public void copyFiles(SetBase setBase, MojoInfo mojoInfo, File archiveTempDir) throws NsisActionExecutionException {
        FileItem fileItem = (FileItem) setBase;
        File sourceFile = getFile(fileItem, mojoInfo);
        File destinationDir = new File(archiveTempDir, ((null == fileItem.getOutputDirectory()) ? "" : fileItem.getOutputDirectory()));
        if (!destinationDir.exists()) {
            if (!destinationDir.mkdirs()) {
                throw new NsisActionExecutionException("Could not create " + "destination directory: " + destinationDir.getAbsolutePath());
            }
        }
        File destinationFile = new File(destinationDir, sourceFile.getName());
        try {
            FileUtils.copyFile(sourceFile, destinationFile);
        } catch (IOException e) {
            mojoInfo.getLog().error("Error copying " + sourceFile.getAbsolutePath(), e);
            throw new NsisActionExecutionException("Error copying " + sourceFile.getAbsolutePath(), e);
        }
    }
