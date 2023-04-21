    public XPlanetSystemTray(MainFrame caller) {
        this.logger = Logger.getLogger(this.getClass().getName());
        this.caller = caller;
        if (!SystemTray.isSupported()) {
            this.logger.config("System Tray is not supported");
            return;
        }
        PopupMenu popup = new PopupMenu();
        this.trayIcon = new TrayIcon(createImage("/xplanetconfigurator/gui/resources/img/mgs.jpg", "tray icon"));
        this.trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("XPlanet");
        this.tray = SystemTray.getSystemTray();
        MenuItem menuItemShow = new MenuItem("Show Window...");
        this.checkboxMenuItemRun = new CheckboxMenuItem("Run XPlanet");
        MenuItem menuItemExit = new MenuItem("Exit");
        MenuItem menuItemWebsite = null;
        MenuItem menuItemFeedback = null;
        popup.add(menuItemShow);
        popup.add(this.checkboxMenuItemRun);
        popup.addSeparator();
        if (!Desktop.isDesktopSupported()) {
            this.logger.config("Desktop is not supported");
            return;
        } else {
            this.desktop = Desktop.getDesktop();
            menuItemWebsite = new MenuItem("Website...");
            menuItemFeedback = new MenuItem("Feedback...");
            popup.add(menuItemWebsite);
            popup.add(menuItemFeedback);
            popup.addSeparator();
            menuItemWebsite.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    openWebsite();
                }
            });
            menuItemFeedback.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    openFeedback();
                }
            });
        }
        popup.add(menuItemExit);
        trayIcon.setPopupMenu(popup);
        try {
            this.tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }
        trayIcon.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showGUI();
            }
        });
        menuItemShow.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showGUI();
            }
        });
        this.checkboxMenuItemRun.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                int cb1Id = e.getStateChange();
                if (cb1Id == ItemEvent.SELECTED) {
                    setXPlanetRunning(true);
                } else {
                    setXPlanetRunning(false);
                }
            }
        });
        menuItemExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                cleanUpCaller();
            }
        });
    }
