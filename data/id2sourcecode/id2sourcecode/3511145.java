    protected void installHelloWorldApp() throws Exception {
        File webAppLib = new File("./apps/helloworld.war/WEB-INF/lib");
        File webInfLib = new File("./lib/web-inf");
        FileUtils.copyFilesFromDir(webInfLib, webAppLib);
    }
