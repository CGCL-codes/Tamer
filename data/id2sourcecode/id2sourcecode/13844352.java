    public TrayIconStarter() {
        if (SystemTray.isSupported()) {
            new RESTClient(this);
            debugPanel = new DebugPanel();
            this.logMessage(Logger.INFO, "Application", "Started", "");
            MouseListener mouseListener = new MouseListener() {

                public void mouseClicked(MouseEvent e) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                    switch(e.getButton()) {
                        case 1:
                            {
                                launchHoursPanel();
                            }
                    }
                }

                public void mouseEntered(MouseEvent e) {
                }

                public void mouseExited(MouseEvent e) {
                }

                public void mousePressed(MouseEvent e) {
                }

                public void mouseReleased(MouseEvent e) {
                }
            };
            ActionListener exitListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            };
            ActionListener configListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    launchConfigPanel(true);
                }
            };
            ActionListener browserListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    launchBrowserPanel(true);
                }
            };
            ActionListener debugListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    launchDebugPanel(true);
                }
            };
            ActionListener hoursListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    launchHoursPanel();
                }
            };
            ActionListener loginListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    RESTClient.defaultInstance().restBrowserLogin();
                }
            };
            PopupMenu popup = new PopupMenu();
            MenuItem hoursMenuItem = new MenuItem("Log Hours");
            hoursMenuItem.addActionListener(hoursListener);
            popup.add(hoursMenuItem);
            if (java.awt.Desktop.isDesktopSupported()) {
                MenuItem loginItem = new MenuItem("Login");
                loginItem.addActionListener(loginListener);
                popup.add(loginItem);
            }
            MenuItem browserItem = new MenuItem("Object Browser");
            browserItem.addActionListener(browserListener);
            popup.add(browserItem);
            MenuItem configItem = new MenuItem("Configuration");
            configItem.addActionListener(configListener);
            popup.add(configItem);
            MenuItem debugItem = new MenuItem("Debug");
            debugItem.addActionListener(debugListener);
            popup.add(debugItem);
            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(exitListener);
            popup.add(exitItem);
            ActionListener actionListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    trayIcon.displayMessage("Action Event", "An Action Event Has Been Peformed!", TrayIcon.MessageType.INFO);
                    Image updatedImage = Toolkit.getDefaultToolkit().getImage("images/tray.gif");
                    trayIcon.setImage(updatedImage);
                    trayIcon.setImageAutoSize(true);
                }
            };
            String imagePath = "/org/projectopen/timesheet/po-icon.gif";
            URL fileLocation = getClass().getResource(imagePath);
            Image image = Toolkit.getDefaultToolkit().getImage(fileLocation);
            String imageStr = "null";
            if (image != null) {
                imageStr = image.getSource().toString();
            }
            this.logMessage(Logger.INFO, "Application", "Loading image from " + imagePath, imageStr);
            trayIcon = new TrayIcon(image, "Tray Demo", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);
            trayIcon.addMouseListener(mouseListener);
            SystemTray tray = SystemTray.getSystemTray();
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        } else {
            System.err.println("System tray is currently not supported.");
        }
    }
