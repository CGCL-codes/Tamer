    protected void doExecute() throws Exception {
        System.out.println("\n*************************\tBegin doExecute()\t*************************\n");
        FileUtils.deleteDirectory(new File(deployDirectory, project.getArtifactId()));
        new File(deployDirectory, project.getArtifactId() + ".war").delete();
        FileUtils.copyFileToDirectory(warFile, deployDirectory);
        System.out.println("\n\n*************************\tEnd doExecute()\t*************************\n");
    }
