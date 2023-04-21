    private void copyRSL() throws MojoExecutionException {
        File rslLibFile = new File(FlexMojoUtils.getResourcesDirectory(outputDirectory), "rsl/library.swf");
        if (!rslLibFile.exists()) {
            throw new MojoExecutionException("Could not find generated RSL");
        }
        try {
            FileUtils.copyFile(rslLibFile, getOutputArtifactFile("rsl", FlexMojoUtils.getRSLExtension(false)));
        } catch (Exception e) {
            throw new MojoExecutionException("Could not copy generated RSL file to target directory.");
        }
    }
