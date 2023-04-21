    public Launcher() {
        Locale.setDefault(Locale.ENGLISH);
        try {
            ConfigureLog4j.defaultLogging(true);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Object menuBarUI = UIManager.get("MenuBarUI");
            Object menuItemUI = UIManager.get("MenuItemUI");
            Object chekBoxMenuItemUI = UIManager.get("CheckBoxMenuItemUI");
            Object radioButtonMenuItemUI = UIManager.get("RadioButtonMenuItemUI");
            Object popupMenuUI = UIManager.get("PopupMenuUI");
            if (SyntheticaRootPaneUI.EVAL_COPY) UIManager.put("Synthetica.window.decoration", Boolean.FALSE);
            UIManager.setLookAndFeel(new SyntheticaGreenDreamLookAndFeel());
            if (System.getProperty("os.name").startsWith("Mac")) {
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                UIManager.put("MenuBarUI", menuBarUI);
                UIManager.put("MenuItemUI", menuItemUI);
                UIManager.put("CheckBoxMenuItemUI", chekBoxMenuItemUI);
                UIManager.put("RadioButtonMenuItemUI", radioButtonMenuItemUI);
                UIManager.put("PopupMenuUI", popupMenuUI);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new SettingsReader();
        } catch (Exception e) {
            Log.writeErrorLog(this.getClass(), "Can not read settings.", e);
        }
        new BioDWHSystemTray();
        ParserLibrary.init();
        new Thread(new DownloadQueue()).start();
        new Thread(new UncompressQueue()).start();
        new Thread(new ProcessQueue()).start();
        new Thread(new ParserProgressMonitor()).start();
        new Thread(new DownloadProgressMonitor()).start();
        new Thread(new UncompressProgressMonitor()).start();
        new Thread(new IntegrationProgressMonitor()).start();
        new Thread(new Manager()).start();
        if (BioDWHSettings.isLogDatabase()) ConfigureLog4j.enableDatabaseLogging(true);
    }
