    private void createdBuildFiles() throws IOException {
        File buildLibDir = getBuildLibDir();
        FileUtils.copyFilesFromDir(new File(options.getLibDir(), "build"), buildLibDir);
        if (this.options.getProjectLayout().equals(ProjectLayout.MODULE_APP)) {
            FileUtils.copyFilesFromDir(new File(options.getLibDir(), "modules/build"), buildLibDir);
            createFile(buildLibDir.getParentFile(), "build.xml", "modules/build.xml");
        } else if (this.options.getProjectLayout().equals(ProjectLayout.MODULE_CONTAINER_APP)) {
            FileUtils.copyFilesFromDir(new File(options.getLibDir(), "modules/build"), buildLibDir);
            createFile(buildLibDir.getParentFile(), "build.xml", "modules-container/build.xml");
        } else {
            createFile(buildLibDir.getParentFile(), "build.xml", "build.xml");
        }
    }
