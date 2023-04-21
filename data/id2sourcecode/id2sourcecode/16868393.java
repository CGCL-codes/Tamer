    public static void main(String[] args) {
        URL url = DIYLCStarter.class.getResource("log4j.properties");
        Properties properties = new Properties();
        try {
            properties.load(url.openStream());
            PropertyConfigurator.configure(properties);
        } catch (Exception e) {
            LOG.error("Could not initialize log4j configuration", e);
        }
        LOG.debug("Java version: " + System.getProperty("java.runtime.version") + " by " + System.getProperty("java.vm.vendor"));
        LOG.debug("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        LOG.info("Starting DIYLC with working directory " + System.getProperty("user.dir"));
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOG.error("Could not set Look&Feel", e);
        }
        MainFrame mainFrame = new MainFrame();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        boolean showTemplates = ConfigurationManager.getInstance().readBoolean(TemplateDialog.SHOW_TEMPLATES_KEY, true);
        if (showTemplates) {
            TemplateDialog templateDialog = new TemplateDialog(mainFrame, mainFrame.getPresenter());
            if (!templateDialog.getFiles().isEmpty()) {
                templateDialog.setVisible(true);
            }
        }
        url = DIYLCStarter.class.getResource("/config.properties");
        properties = new Properties();
        try {
            LOG.info("Injecting default properties.");
            properties.load(new FileInputStream("config.properties"));
            PropertyInjector.injectProperties(properties);
        } catch (Exception e) {
            LOG.error("Could not read config.properties file", e);
        }
    }
