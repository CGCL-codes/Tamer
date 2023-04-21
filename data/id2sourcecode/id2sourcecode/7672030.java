    public HermesServer() throws FileNotFoundException, IOException {
        RandomAccessFile randomFile = new RandomAccessFile("lock.lk", "rw");
        FileChannel channel = randomFile.getChannel();
        if (channel.tryLock() != null) {
            initComponents();
            setSize(547, 380);
            SSpliter.setSize(getSize());
            server = new HServer();
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                Image icon = new javax.swing.ImageIcon(getClass().getResource("/hermesserver/com/HImages/hermes.jpg")).getImage();
                PopupMenu popup = new PopupMenu();
                MenuItem startItem = new MenuItem("Start");
                startItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        server.start(14888);
                    }
                });
                popup.add(startItem);
                MenuItem stopItem = new MenuItem("Stop");
                stopItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        server.stop();
                    }
                });
                popup.add(stopItem);
                MenuItem restartItem = new MenuItem("Restart");
                popup.add(restartItem);
                ActionListener showAcction = new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        setState(0);
                    }
                };
                MenuItem closeItem = new MenuItem("Close");
                closeItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        confirmeClosing();
                    }
                });
                popup.add(closeItem);
                trayIcon = new TrayIcon(icon, "Hermes-server", popup);
                trayIcon.setImageAutoSize(true);
                trayIcon.addActionListener(showAcction);
                try {
                    tray.add(trayIcon);
                } catch (AWTException e) {
                    System.err.println("Problem with adding icon to tray.");
                }
            }
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }
