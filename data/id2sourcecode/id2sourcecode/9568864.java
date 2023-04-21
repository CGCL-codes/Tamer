    private void jbInit() throws Exception {
        contentPane = (JPanel) this.getContentPane();
        component1 = Box.createHorizontalStrut(8);
        component2 = Box.createHorizontalStrut(8);
        contentPane.setLayout(borderLayout1);
        if (window != null) {
            window.setTitle("LogicSim");
        }
        statusBar.setText(" ");
        jMenuFile.setText(I18N.getString("MENU_FILE"));
        jMenuFileExit.setText(I18N.getString("MENU_EXIT"));
        jMenuFileExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(88, java.awt.event.KeyEvent.CTRL_MASK, false));
        jMenuFileExit.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jMenuFileExit_actionPerformed(e);
            }
        });
        jMenuHelp.setText(I18N.getString("MENU_HELP"));
        jMenuHelpAbout.setText(I18N.getString("MENU_ABOUT"));
        jMenuHelpAbout.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jMenuHelpAbout_actionPerformed(e);
            }
        });
        jScrollPane_lspanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane_lspanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jPanel1.setLayout(borderLayout3);
        jButton_open.setIcon(new ImageIcon(logicsim.LSFrame.class.getResource("images/open.gif")));
        jButton_open.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jButton_open_actionPerformed(e);
            }
        });
        jButton_open.setToolTipText(I18N.getString("MENU_OPEN"));
        jButton_new.setIcon(new ImageIcon(logicsim.LSFrame.class.getResource("images/new.gif")));
        jButton_new.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jButton_new_actionPerformed(e);
            }
        });
        jButton_save.setIcon(new ImageIcon(logicsim.LSFrame.class.getResource("images/save.gif")));
        jButton_save.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jButton_save_actionPerformed(e);
            }
        });
        jButton_save.setToolTipText(I18N.getString("MENU_SAVE"));
        jToggleButton_simulate.setText(I18N.getString("BUTTON_SIMULATE"));
        jToggleButton_simulate.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jToggleButton_simulate_actionPerformed(e);
            }
        });
        jButton_reset.setText(I18N.getString("BUTTON_RESET"));
        jButton_reset.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jButton_reset_actionPerformed(e);
            }
        });
        jMenuModule.setText(I18N.getString("MENU_MODULE"));
        jMenuItem_createmod.setText(I18N.getString("MENU_CREATEMODULE"));
        jMenuItem_createmod.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jMenuItem_createmod_actionPerformed(e);
            }
        });
        jMenuItem_modproperties.setText(I18N.getString("MENU_MODULEPROPERTIES"));
        jMenuItem_modproperties.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jMenuItem_modproperties_actionPerformed(e);
            }
        });
        jMenuItem_exportimage.setText(I18N.getString("MENU_EXPORT"));
        jMenuItem_exportimage.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                exportImage();
            }
        });
        jMenuItem_print.setText(I18N.getString("MENU_PRINT"));
        jMenuItem_print.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jMenuItem_print_actionPerformed(e);
            }
        });
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setDividerLocation(170);
        jPanel_gates.setLayout(borderLayout2);
        jList_gates.addMouseListener(new LSFrame_jList_gates_mouseAdapter(this));
        jPanel_gates.setPreferredSize(new Dimension(120, 200));
        jPanel_gates.setMinimumSize(new Dimension(80, 200));
        jButton_addpoint.setToolTipText(I18N.getString("TOOLTIP_ADDPOINT"));
        jButton_addpoint.setIcon(new ImageIcon(logicsim.LSFrame.class.getResource("images/addpoint.gif")));
        jButton_addpoint.addActionListener(new LSFrame_jButton_addpoint_actionAdapter(this));
        jMenuItem_new.setText(I18N.getString("MENU_NEW"));
        jMenuItem_new.setAccelerator(javax.swing.KeyStroke.getKeyStroke(78, java.awt.event.KeyEvent.CTRL_MASK, false));
        jMenuItem_new.addActionListener(new LSFrame_jMenuItem_new_actionAdapter(this));
        jMenuItem_open.setText(I18N.getString("MENU_OPEN"));
        jMenuItem_open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(79, java.awt.event.KeyEvent.CTRL_MASK, false));
        jMenuItem_open.addActionListener(new LSFrame_jMenuItem_open_actionAdapter(this));
        jMenuItem_save.setText(I18N.getString("MENU_SAVE"));
        jMenuItem_save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(87, java.awt.event.KeyEvent.CTRL_MASK, false));
        jMenuItem_save.addActionListener(new LSFrame_jMenuItem_save_actionAdapter(this));
        jMenuItem_saveas.setText(I18N.getString("MENU_SAVEAS"));
        jMenuItem_saveas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(83, java.awt.event.KeyEvent.CTRL_MASK, false));
        jMenuItem_saveas.addActionListener(new LSFrame_jMenuItem_saveas_actionAdapter(this));
        jMenuItem_help.setText(I18N.getString("MENU_HELP"));
        jMenuItem_help.addActionListener(new LSFrame_jMenuItem_help_actionAdapter(this));
        jButton_delpoint.setToolTipText(I18N.getString("BUTTON_REMOVE_WIRE_POINT"));
        jButton_delpoint.setIcon(new ImageIcon(logicsim.LSFrame.class.getResource("images/delpoint.gif")));
        jButton_delpoint.addActionListener(new LSFrame_jButton_delpoint_actionAdapter(this));
        jMenuSettings.setText(I18N.getString("MENU_SETTINGS"));
        jCheckBoxMenuItem_paintGrid.setText(I18N.getString("MENU_PAINTGRID"));
        jCheckBoxMenuItem_paintGrid.setSelected(true);
        jCheckBoxMenuItem_paintGrid.addActionListener(new LSFrame_jCheckBoxMenuItem_paintGrid_actionAdapter(this));
        jMenuFile.add(jMenuItem_new);
        jMenuFile.add(jMenuItem_open);
        jMenuFile.add(jMenuItem_save);
        jMenuFile.add(jMenuItem_saveas);
        jMenuFile.add(jMenuItem_exportimage);
        jMenuFile.add(jMenuItem_print);
        jMenuFile.add(jMenuFileExit);
        jMenuHelp.add(jMenuHelpAbout);
        jMenuHelp.add(jMenuItem_help);
        jMenuBar1.add(jMenuFile);
        jMenuBar1.add(jMenuModule);
        jMenuBar1.add(jMenuSettings);
        jMenuBar1.add(jMenuHelp);
        this.setJMenuBar(jMenuBar1);
        contentPane.add(statusBar, BorderLayout.SOUTH);
        lspanel.setBackground(Color.white);
        jPanel_gates.add(jScrollPane_gates, BorderLayout.CENTER);
        jPanel_gates.add(jComboBox_numinput, BorderLayout.SOUTH);
        jSplitPane.add(jPanel_gates, JSplitPane.LEFT);
        jSplitPane.add(jScrollPane_lspanel, JSplitPane.RIGHT);
        contentPane.add(jSplitPane, BorderLayout.CENTER);
        jScrollPane_gates.getViewport().add(jList_gates, null);
        jToolBar.add(jButton_new, null);
        jToolBar.add(jButton_open);
        jToolBar.add(jButton_save);
        jToolBar.add(component1, null);
        jToolBar.add(jButton_addpoint, null);
        jToolBar.add(jButton_delpoint, null);
        jToolBar.add(component2, null);
        jToolBar.add(jToggleButton_simulate, null);
        jToolBar.add(jButton_reset, null);
        jPanel1.add(jToolBar, BorderLayout.CENTER);
        contentPane.add(jPanel1, BorderLayout.NORTH);
        jMenuModule.add(jMenuItem_createmod);
        jMenuModule.add(jMenuItem_modproperties);
        jMenu_gatedesign.setText(I18N.getString("MENU_GATEDESIGN"));
        buttongroup_gatedesign.add(jMenuItem_gatedesign_din);
        buttongroup_gatedesign.add(jMenuItem_gatedesign_iso);
        jMenuItem_gatedesign_din.setText(I18N.getString("MENU_GATEDESIGN_DIN"));
        jMenuItem_gatedesign_iso.setText(I18N.getString("MENU_GATEDESIGN_ISO"));
        jMenu_gatedesign.add(jMenuItem_gatedesign_din);
        jMenu_gatedesign.add(jMenuItem_gatedesign_iso);
        jMenuItem_gatedesign_din.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jMenuItem_gatedesign_actionPerformed(e);
            }
        });
        jMenuItem_gatedesign_iso.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jMenuItem_gatedesign_actionPerformed(e);
            }
        });
        boolean paintgrid = true;
        jMenuItem_gatedesign_din.setSelected(true);
        use_language = "en";
        try {
            if (this.isApplet == true) {
                java.net.URL url = new java.net.URL(this.applet.getCodeBase() + "logicsim.cfg");
                userProperties.load(url.openStream());
            } else {
                userProperties.load(new FileInputStream("logicsim.cfg"));
            }
            if (userProperties.containsKey("paint_grid")) paintgrid = userProperties.getProperty("paint_grid").equals("true");
            String s = userProperties.getProperty("gatedesign");
            if (s != null && s.equals("iso")) {
                jMenuItem_gatedesign_iso.setSelected(true);
                LSFrame.gatedesign = "iso";
            }
            if (userProperties.containsKey("language")) use_language = userProperties.getProperty("language");
        } catch (Exception ex) {
        }
        jMenu_language.setText(I18N.getString("MENU_LANGUAGE"));
        create_language_menu(jMenu_language, use_language);
        jCheckBoxMenuItem_paintGrid.setSelected(paintgrid);
        lspanel.setPaintGrid(paintgrid);
        jMenuSettings.add(jCheckBoxMenuItem_paintGrid);
        jMenuSettings.add(jMenu_gatedesign);
        jMenuSettings.add(jMenu_language);
        popup = new JPopupMenu();
        menuItem_remove = new JMenuItem(I18N.getString("MENU_REMOVEGATE"));
        menuItem_remove.addActionListener(this);
        popup.add(menuItem_remove);
        menuItem_properties = new JMenuItem(I18N.getString("MENU_PROPERTIES"));
        menuItem_properties.addActionListener(this);
        popup.add(menuItem_properties);
        lspanel.addMouseListener(new PopupListener());
        popup_list = new JPopupMenu();
        menuItem_list_delmod = new JMenuItem(I18N.getString("MENU_DELETEMODULE"));
        menuItem_list_delmod.addActionListener(this);
        popup_list.add(menuItem_list_delmod);
        jList_gates.addMouseListener(new PopupListener());
        fillGateList();
        this.requestFocus();
    }
