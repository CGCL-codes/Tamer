    public void copyFiles(SetBase setBase, MojoInfo mojoInfo, File archiveTempDir) throws NsisActionExecutionException {
        DependencySet dependencySet = (DependencySet) setBase;
        File[] selectedFiles = resolveDependencies(dependencySet, mojoInfo);
        File destinationDir = new File(archiveTempDir, ((null == dependencySet.getOutputDirectory()) ? "" : dependencySet.getOutputDirectory()));
        if (!destinationDir.exists()) {
            if (!destinationDir.mkdirs()) {
                throw new NsisActionExecutionException("Could not create " + "destination directory: " + destinationDir.getAbsolutePath());
            }
        }
        for (int i = 0; i < selectedFiles.length; i++) {
            File destinationFile = new File(destinationDir, selectedFiles[i].getName());
            mojoInfo.getLog().debug("Copying: " + selectedFiles[i] + " to " + destinationFile.getAbsolutePath());
            try {
                FileUtils.copyFile(selectedFiles[i], destinationFile);
            } catch (IOException e) {
                mojoInfo.getLog().error("Error copying " + selectedFiles[i], e);
                throw new NsisActionExecutionException("Error copying " + selectedFiles[i], e);
            }
        }
    }
