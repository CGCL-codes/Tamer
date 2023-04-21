    public void copyFiles(SetBase setBase, MojoInfo mojoInfo, File archiveTempDir) throws NsisActionExecutionException {
        FileSet fileSet = (FileSet) setBase;
        File projectDir = mojoInfo.getProject().getBasedir();
        String[] selectedFiles = getFiles(fileSet, mojoInfo);
        File basedir = new File(projectDir, ((null == fileSet.getDirectory()) ? "" : fileSet.getDirectory()));
        File destinationDir = new File(archiveTempDir, ((null == fileSet.getOutputDirectory()) ? "" : fileSet.getOutputDirectory()));
        if (!destinationDir.exists()) {
            if (!destinationDir.mkdirs()) {
                throw new NsisActionExecutionException("Could not create " + "destination directory: " + destinationDir.getAbsolutePath());
            }
        }
        for (int i = 0; i < selectedFiles.length; i++) {
            File sourceFile = new File(basedir, selectedFiles[i]);
            File destinationFile = new File(destinationDir, selectedFiles[i]);
            mojoInfo.getLog().debug("Copying: " + selectedFiles[i] + " to " + destinationFile.getAbsolutePath());
            try {
                FileUtils.copyFile(sourceFile, destinationFile);
            } catch (IOException e) {
                mojoInfo.getLog().error("Error copying " + selectedFiles[i], e);
                throw new NsisActionExecutionException("Error copying " + selectedFiles[i], e);
            }
        }
    }
