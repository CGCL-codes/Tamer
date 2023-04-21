    public KlangEditorMenuBar(KlangEditor parent) {
        super();
        this.parent = parent;
        menuiQuit = new JMenuItem(KlangConstants.KLANGEDITOR_MENU_QUIT, KeyEvent.VK_F4);
        menuiQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        menuiQuit.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                doQuit();
            }
        });
        menuiCloseFile = new JMenuItem(KlangConstants.KLANGEDITOR_MENU_CLOSE, KeyEvent.VK_W);
        menuiCloseFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        menuiCloseFile.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                doClose();
            }
        });
        menuiCloseFile.setEnabled(false);
        menuiOpenFile = new JMenuItem(KlangConstants.KLANGEDITOR_MENU_OPEN, KeyEvent.VK_O);
        menuiOpenFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuiOpenFile.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                doOpen();
            }
        });
        menuiOptionsReadOnly = new JCheckBoxMenuItem(KlangConstants.KLANGEDITOR_MENU_READONLY);
        menuiOptionsReadOnly.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                doReadOnlySelected();
            }
        });
        menuiOptionsDec = new JRadioButtonMenuItem(KlangConstants.KLANGEDITOR_MENU_DEC);
        menuiOptionsDec.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                doHexDecToggle();
            }
        });
        menuiOptionsHex = new JRadioButtonMenuItem(KlangConstants.KLANGEDITOR_MENU_HEX);
        menuiOptionsHex.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                doHexDecToggle();
            }
        });
        ButtonGroup bg = new ButtonGroup();
        bg.add(menuiOptionsDec);
        bg.add(menuiOptionsHex);
        menuiOptionsDec.setSelected(true);
        menuiOptionsReadOnly.setSelected(false);
        doHexDecToggle();
        doReadOnlySelected();
        menuiOptionsAutoText = new JRadioButtonMenuItem(KlangConstants.KLANGEDITOR_MENU_AUTOTEXT);
        menuiOptionsAutoText.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                doTextToggle();
            }
        });
        menuiOptionsManualText = new JRadioButtonMenuItem(KlangConstants.KLANGEDITOR_MENU_MANUALTEXT);
        menuiOptionsManualText.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                doTextToggle();
            }
        });
        ButtonGroup bg2 = new ButtonGroup();
        bg2.add(menuiOptionsAutoText);
        bg2.add(menuiOptionsManualText);
        menuiOptionsAutoText.setSelected(true);
        menuiOptionsManualText.setSelected(false);
        doTextToggle();
        menuiAboutWeb = new JMenuItem(KlangConstants.KLANGEDITOR_MENU_WEB, KeyEvent.VK_V);
        menuiAboutWeb.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
        menuiAboutWeb.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                doAboutWeb();
            }
        });
        menuiAboutWeb.setEnabled(false);
        menuiAboutDonate = new JMenuItem(KlangConstants.KLANGEDITOR_MENU_DONATE, KeyEvent.VK_D);
        menuiAboutDonate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
        menuiAboutDonate.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                doAboutDonate();
            }
        });
        menuiAboutDonate.setEnabled(false);
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                menuiAboutWeb.setEnabled(true);
                menuiAboutDonate.setEnabled(true);
            }
        }
        menuiAboutAbout = new JMenuItem(KlangConstants.KLANGEDITOR_MENU_CREDITS, KeyEvent.VK_B);
        menuiAboutAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
        menuiAboutAbout.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                doAbout();
            }
        });
        menuFile = new JMenu(KlangConstants.KLANGEDITOR_MENU_FILE);
        menuFile.setMnemonic(KeyEvent.VK_F);
        menuFile.add(menuiOpenFile);
        menuFile.add(menuiCloseFile);
        menuFile.addSeparator();
        menuFile.add(menuiQuit);
        menuOptions = new JMenu(KlangConstants.KLANGEDITOR_MENU_OPTIONS);
        menuOptions.add(menuiOptionsReadOnly);
        menuOptions.addSeparator();
        menuOptions.add(menuiOptionsDec);
        menuOptions.add(menuiOptionsHex);
        menuOptions.addSeparator();
        menuOptions.add(menuiOptionsAutoText);
        menuOptions.add(menuiOptionsManualText);
        menuAbout = new JMenu(KlangConstants.KLANGEDITOR_MENU_ABOUT);
        menuAbout.setMnemonic(KeyEvent.VK_A);
        menuAbout.add(menuiAboutWeb);
        menuAbout.add(menuiAboutDonate);
        menuAbout.add(menuiAboutAbout);
        this.add(menuFile);
        this.add(menuOptions);
        this.add(menuAbout);
    }
