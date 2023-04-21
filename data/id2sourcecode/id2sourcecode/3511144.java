    protected void installQuickStartApp() throws Exception {
        File webAppLib = new File("./apps/quickstart.war/WEB-INF/lib");
        File buildLib = new File("./lib/build");
        File webInfLib = new File("./lib/web-inf");
        File modulesBuildLib = new File("./lib/modules/build");
        File modulesWebInfLib = new File("./lib/modules/web-inf");
        FileUtils.copyFilesFromDir(buildLib, webAppLib);
        FileUtils.copyFilesFromDir(webInfLib, webAppLib);
        FileUtils.copyFilesFromDir(modulesBuildLib, webAppLib);
        FileUtils.copyFilesFromDir(modulesWebInfLib, webAppLib);
    }
