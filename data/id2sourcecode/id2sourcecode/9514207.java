    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
        newItem = new JMenuItem("New");
        newItem.setMnemonic(KeyEvent.VK_N);
        newItem.addActionListener(this);
        menu.add(newItem);
        openItem = new JMenuItem("Open...");
        openItem.setMnemonic(KeyEvent.VK_O);
        openItem.addActionListener(this);
        menu.add(openItem);
        saveItem = new JMenuItem("Save");
        saveItem.setMnemonic(KeyEvent.VK_S);
        saveItem.setEnabled(false);
        saveItem.addActionListener(this);
        menu.add(saveItem);
        saveAsItem = new JMenuItem("Save as...");
        saveAsItem.setMnemonic(KeyEvent.VK_A);
        saveAsItem.setDisplayedMnemonicIndex(5);
        saveAsItem.addActionListener(this);
        menu.add(saveAsItem);
        menu.addSeparator();
        importFromClipboardItem = new JMenuItem("Import from Clipboard");
        importFromClipboardItem.setMnemonic(KeyEvent.VK_C);
        importFromClipboardItem.addActionListener(this);
        menu.add(importFromClipboardItem);
        menu.addSeparator();
        recentFileMenu = new JMenu("Recent");
        recentFileMenu.setMnemonic(KeyEvent.VK_R);
        recentFileMenu.setEnabled(false);
        menu.add(recentFileMenu);
        menu.addSeparator();
        exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_X);
        exitItem.addActionListener(this);
        menu.add(exitItem);
        menu = new JMenu("Options");
        menu.setMnemonic(KeyEvent.VK_O);
        menuBar.add(menu);
        JMenu submenu = new JMenu("Look and Feel");
        submenu.setMnemonic(KeyEvent.VK_L);
        menu.add(submenu);
        ActionListener al = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    JRadioButtonMenuItem item = (JRadioButtonMenuItem) e.getSource();
                    String lf = item.getActionCommand();
                    UIManager.setLookAndFeel(lf);
                    preferences.setLookAndFeel(lf);
                    SwingUtilities.updateComponentTreeUI(me);
                    preferences.setLookAndFeel(lf);
                } catch (Exception x) {
                    x.printStackTrace(System.err);
                }
            }
        };
        ButtonGroup group = new ButtonGroup();
        String lookAndFeel = UIManager.getLookAndFeel().getClass().getName();
        UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
        lookAndFeelItems = new JRadioButtonMenuItem[info.length];
        for (int i = 0; i < info.length; i++) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(info[i].getName());
            lookAndFeelItems[i] = item;
            item.setMnemonic(item.getText().charAt(0));
            item.setActionCommand(info[i].getClassName());
            group.add(item);
            submenu.add(item);
            if (item.getActionCommand().equals(lookAndFeel)) {
                item.setSelected(true);
            }
            item.addActionListener(al);
        }
        submenu = new JMenu("Font size");
        submenu.setMnemonic(KeyEvent.VK_F);
        menu.add(submenu);
        al = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JMenuItem button = (JMenuItem) e.getSource();
                float adjustment = Float.parseFloat(button.getActionCommand());
                adjustFontSize(adjustment);
                fontSizeAdjustment += adjustment;
            }
        };
        JMenuItem menuItem = new JMenuItem("Increase");
        menuItem.setMnemonic(KeyEvent.VK_I);
        submenu.add(menuItem);
        menuItem.addActionListener(al);
        menuItem.setActionCommand("1f");
        menuItem = new JMenuItem("Decrease");
        menuItem.setMnemonic(KeyEvent.VK_I);
        submenu.add(menuItem);
        menuItem.addActionListener(al);
        menuItem.setActionCommand("-1f");
        group = new ButtonGroup();
        submenu = new JMenu("Prompt to Save");
        submenu.setMnemonic(KeyEvent.VK_P);
        menu.add(submenu);
        al = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                for (int i = 0; i < promptButtons.length; i++) {
                    if (promptButtons[i] == source) {
                        preferences.setPromptToSave(promptStrings[i]);
                        break;
                    }
                }
            }
        };
        String promptText = preferences.getPromptToSave();
        promptButtons = new JRadioButtonMenuItem[promptStrings.length];
        for (int i = 0; i < promptStrings.length; i++) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(promptStrings[i]);
            item.setMnemonic(promptMnemonics[i]);
            promptButtons[i] = item;
            if (promptStrings[i].equals(promptText)) {
                item.setSelected(true);
            }
            item.addActionListener(al);
            group.add(item);
            submenu.add(item);
        }
        submenu = new JMenu("Remotes");
        submenu.setMnemonic(KeyEvent.VK_R);
        menu.add(submenu);
        group = new ButtonGroup();
        useAllRemotes = new JRadioButtonMenuItem("All");
        useAllRemotes.setMnemonic(KeyEvent.VK_A);
        al = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == useAllRemotes) {
                    editorPanel.setRemotes(RemoteManager.getRemoteManager().getRemotes());
                    preferences.setShowRemotes("All");
                } else if (source == usePreferredRemotes) {
                    editorPanel.setRemotes(preferences.getPreferredRemotes());
                    preferences.setShowRemotes("Preferred");
                } else {
                    editPreferredRemotes();
                }
            }
        };
        useAllRemotes.setSelected(true);
        group.add(useAllRemotes);
        submenu.add(useAllRemotes);
        usePreferredRemotes = new JRadioButtonMenuItem("Preferred");
        usePreferredRemotes.setMnemonic(KeyEvent.VK_P);
        group.add(usePreferredRemotes);
        submenu.add(usePreferredRemotes);
        String temp = preferences.getShowRemotes();
        if (temp.equals("All")) {
            useAllRemotes.setSelected(true);
        } else {
            usePreferredRemotes.setSelected(true);
        }
        if (preferences.getPreferredRemotes().size() == 0) {
            useAllRemotes.setSelected(true);
            usePreferredRemotes.setEnabled(false);
        }
        useAllRemotes.addActionListener(al);
        usePreferredRemotes.addActionListener(al);
        submenu.addSeparator();
        JMenuItem item = new JMenuItem("Edit preferred...");
        item.setMnemonic(KeyEvent.VK_E);
        item.addActionListener(al);
        submenu.add(item);
        submenu = new JMenu("Function names");
        submenu.setMnemonic(KeyEvent.VK_F);
        menu.add(submenu);
        group = new ButtonGroup();
        useDefaultNames = new JRadioButtonMenuItem("Default");
        useDefaultNames.setMnemonic(KeyEvent.VK_D);
        al = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == useDefaultNames) {
                    preferences.setUseCustomNames(false);
                } else if (source == useCustomNames) {
                    preferences.setUseCustomNames(true);
                } else {
                    editCustomNames();
                }
            }
        };
        useDefaultNames.setSelected(true);
        useDefaultNames.addActionListener(al);
        group.add(useDefaultNames);
        submenu.add(useDefaultNames);
        useCustomNames = new JRadioButtonMenuItem("Custom");
        useCustomNames.setMnemonic(KeyEvent.VK_C);
        group.add(useCustomNames);
        useCustomNames.setSelected(preferences.getUseCustomNames());
        useCustomNames.addActionListener(al);
        submenu.add(useCustomNames);
        submenu.addSeparator();
        item = new JMenuItem("Edit custom names...");
        item.setMnemonic(KeyEvent.VK_E);
        item.addActionListener(al);
        submenu.add(item);
        al = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem checkItem = (JCheckBoxMenuItem) e.getSource();
                boolean state = checkItem.getState();
                preferences.setShowRemoteSignature(state);
                editorPanel.setShowRemoteSignature(state);
            }
        };
        JCheckBoxMenuItem checkItem = new JCheckBoxMenuItem("Show remote signature");
        checkItem.setMnemonic(KeyEvent.VK_S);
        checkItem.setState(preferences.getShowRemoteSignature());
        checkItem.addActionListener(al);
        menu.add(checkItem);
        submenu = new JMenu("Folders");
        menu.add(submenu);
        al = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String name = e.getActionCommand();
                String extension = (name == "RDF") ? ".rdf" : ".map";
                File path = properties.getFileProperty(name + "Path");
                RMDirectoryChooser chooser = new RMDirectoryChooser(path, extension, name);
                chooser.setAccessory(new ChoiceArea(chooser));
                chooser.setDialogTitle("Choose the directory containing the " + name + "s");
                int returnVal = chooser.showDialog(me, "OK");
                if (returnVal == RMDirectoryChooser.APPROVE_OPTION) {
                    File newPath = chooser.getSelectedFile();
                    properties.setProperty(name + "Path", newPath);
                    RemoteManager mgr = RemoteManager.getRemoteManager();
                    mgr.reset();
                    mgr.loadRemotes(properties);
                    if (useAllRemotes.isSelected()) {
                        editorPanel.setRemotes(RemoteManager.getRemoteManager().getRemotes());
                    }
                }
            }
        };
        menuItem = new JMenuItem("RDF Folder...");
        menuItem.setMnemonic(KeyEvent.VK_R);
        menuItem.setActionCommand("RDF");
        menuItem.addActionListener(al);
        submenu.add(menuItem);
        menuItem = new JMenuItem("Map Folder...");
        menuItem.setMnemonic(KeyEvent.VK_R);
        menuItem.setActionCommand("Image");
        menuItem.addActionListener(al);
        submenu.add(menuItem);
        enablePreserveSelection = new JCheckBoxMenuItem("Allow Preserve Control");
        enablePreserveSelection.setMnemonic(KeyEvent.VK_A);
        enablePreserveSelection.setSelected(Boolean.parseBoolean(properties.getProperty("enablePreserveSelection", "false")));
        enablePreserveSelection.addActionListener(this);
        enablePreserveSelection.setToolTipText("<html>Allow control of which function data is preserved when changing the protocol used in a device upgrade.<br>Do not use this unless you know what you are doing and why.</html>");
        menu.add(enablePreserveSelection);
        menu = new JMenu("Advanced");
        menu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu);
        editManualItem = new JMenuItem("Edit Protocol...");
        editManualItem.setMnemonic(KeyEvent.VK_E);
        editManualItem.addActionListener(this);
        menu.add(editManualItem);
        newManualItem = new JMenuItem("New Manual Protocol...");
        newManualItem.setMnemonic(KeyEvent.VK_M);
        newManualItem.addActionListener(this);
        menu.add(newManualItem);
        menu.addSeparator();
        rawItem = new JMenuItem("Import Raw Upgrade...");
        rawItem.setMnemonic(KeyEvent.VK_R);
        rawItem.addActionListener(this);
        menu.add(rawItem);
        binaryItem = new JMenuItem("Import Binary Upgrade...");
        binaryItem.setMnemonic(KeyEvent.VK_B);
        binaryItem.addActionListener(this);
        menu.add(binaryItem);
        writeBinaryItem = new JMenuItem("Export Binary Upgrade...");
        writeBinaryItem.setEnabled(false);
        writeBinaryItem.setMnemonic(KeyEvent.VK_X);
        writeBinaryItem.addActionListener(this);
        menu.add(writeBinaryItem);
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(menu);
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            readmeItem = new JMenuItem("Readme", KeyEvent.VK_R);
            readmeItem.addActionListener(this);
            menu.add(readmeItem);
            tutorialItem = new JMenuItem("Tutorial", KeyEvent.VK_T);
            tutorialItem.addActionListener(this);
            menu.add(tutorialItem);
            menu.addSeparator();
            homePageItem = new JMenuItem("Home Page", KeyEvent.VK_H);
            homePageItem.addActionListener(this);
            menu.add(homePageItem);
            forumItem = new JMenuItem("Forums", KeyEvent.VK_F);
            forumItem.addActionListener(this);
            menu.add(forumItem);
            wikiItem = new JMenuItem("Wiki", KeyEvent.VK_W);
            wikiItem.addActionListener(this);
            menu.add(wikiItem);
            menu.addSeparator();
        }
        updateItem = new JMenuItem("Check for updates", KeyEvent.VK_C);
        updateItem.addActionListener(this);
        menu.add(updateItem);
        aboutItem = new JMenuItem("About...");
        aboutItem.setMnemonic(KeyEvent.VK_A);
        aboutItem.addActionListener(this);
        menu.add(aboutItem);
    }
