    static void initServer() throws IOException {
        server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(settings.getPort());
        server.addConnector(connector);
        File resourceBase = settings.getResourcePath(WEBAPP_DIR);
        File descriptor = settings.getResourcePath(WEBAPP_DIR + "/WEB-INF/web.xml");
        if (!resourceBase.isDirectory()) throw new FileNotFoundException(resourceBase.getAbsolutePath());
        if (!descriptor.isFile()) throw new FileNotFoundException(descriptor.getAbsolutePath());
        File appSettingsSrc = settings.getResourcePath(APP_SETTINGS_FILE_NAME);
        if (appSettingsSrc.isFile()) {
            File appSettingsDest = settings.getResourcePath(APP_SETTINGS_FILE_PATH);
            FileUtils.copyFile(appSettingsSrc, appSettingsDest);
        }
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setResourceBase(resourceBase.getAbsolutePath());
        webapp.setDescriptor(descriptor.getAbsolutePath());
        webapp.setParentLoaderPriority(true);
        server.setHandler(webapp);
    }
