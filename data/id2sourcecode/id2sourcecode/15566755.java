    private void createWebRootFiles() throws IOException {
        FileUtils.copyFilesFromDir(new File(options.getLibDir(), "web-inf"), getWebInfLibDir());
        String pageName = this.options.getHostedModeStartupURL();
        if (pageName == null || pageName.length() == 0) {
            pageName = "index.crux.xml";
        } else if (pageName.endsWith(".html")) {
            pageName = pageName.substring(0, pageName.length() - 5) + ".crux.xml";
        }
        if (this.options.getProjectLayout().equals(ProjectLayout.MODULE_APP)) {
            FileUtils.copyFilesFromDir(new File(options.getLibDir(), "modules/web-inf"), getWebInfLibDir());
            createFile(getWebInfLibDir().getParentFile(), "web.xml", "modules/web.xml");
            createFile(getModulePublicDir(), pageName, "modules/index.crux.xml");
        } else {
            if (this.options.getProjectLayout().equals(ProjectLayout.MODULE_CONTAINER_APP)) {
                FileUtils.copyFilesFromDir(new File(options.getLibDir(), "modules/web-inf"), getBuildLibDir());
            }
            createFile(getWebInfLibDir().getParentFile(), "web.xml", "web.xml");
            createFile(getWarDir(), pageName, "index.crux.xml");
        }
    }
