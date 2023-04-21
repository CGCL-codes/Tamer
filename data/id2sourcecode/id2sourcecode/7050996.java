    public Application() {
        instance = this;
        openSplashWindow();
        shutdowning = false;
        Runtime.getRuntime().addShutdownHook(new ApplicationShutdown());
        IconManager.createInstance();
        DatabaseManager.createInstance();
        TxManager.createInstance();
        QueryManager.createInstance();
        CommandOutputConsumer.getInstance();
        packFrame = false;
        loadConfig();
        setLookAndFeel(getConfigItem("oexplorer.view.look_and_feel", "Metal"));
        mainFrame = MainFrame.createInstance();
        if (packFrame) mainFrame.pack(); else mainFrame.validate();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = mainFrame.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        mainFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        mainFrame.setVisible(true);
        splashScreen.close();
        splashScreen = null;
        cfgTool = new DbConfig();
        Application.getInstance().writeLog("Application ready.");
    }
