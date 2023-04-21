    public JSETIMon() {
        super("Java SETI Monitor");
        JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        getContentPane().add(splitter, BorderLayout.CENTER);
        JPanel topPane = new JPanel();
        topPane.setBackground(Color.black);
        topPane.setForeground(Color.white);
        splitter.setTopComponent(topPane);
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        getContentPane().add(toolbar, BorderLayout.NORTH);
        c_oSetUser.setToolTipText("Set UserID");
        c_oSetUser.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                UserIDDialog dlg = new UserIDDialog(getBounds());
                dlg.setUserID(c_tUserID);
                dlg.show();
                if (!(dlg.getUserID().equals(""))) {
                    try {
                        updateUserStatistics(dlg.getUserID());
                        c_tUserID = dlg.getUserID();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "That was not a valid username. " + " Keeping old username.", "Bad Username", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        toolbar.add(c_oSetUser);
        toolbar.addSeparator();
        c_oAddUnit.setToolTipText("Add Work Unit");
        c_oAddUnit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

                    public boolean accept(File f) {
                        if (f.isDirectory()) return true;
                        return f.getName().equals("state.sah");
                    }

                    public String getDescription() {
                        return "State Files (state.sah)";
                    }
                });
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    c_vWorkUnits.add(chooser.getSelectedFile());
                    JProgressBar bar = new JProgressBar(0, 100000000);
                    bar.setStringPainted(true);
                    bar.setString("0%");
                    Vector row = new Vector();
                    try {
                        row.add(chooser.getSelectedFile().getCanonicalPath());
                    } catch (Exception err) {
                        row.add("state.sah");
                    }
                    row.add(bar);
                    row.add("???");
                    row.add("???");
                    c_oTabledata.addRow(row);
                    c_oTable.setRowHeight(c_vWorkUnits.size() - 1, bar.getPreferredSize().height);
                    repack();
                    refresh();
                }
            }
        });
        toolbar.add(c_oAddUnit);
        c_oRemoveUnit.setToolTipText("Remove Work Unit");
        c_oRemoveUnit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                int[] rows = c_oTable.getSelectedRows();
                for (int i = rows.length; i-- != 0; ) {
                    c_vWorkUnits.removeElementAt(rows[i]);
                    c_oTabledata.removeRow(rows[i]);
                }
                repack();
                refresh();
            }
        });
        toolbar.add(c_oRemoveUnit);
        toolbar.addSeparator();
        c_oUpdateRate.setToolTipText("Set Update Rate");
        c_oUpdateRate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                UpdateRateDialog dlg = new UpdateRateDialog();
                dlg.show();
            }
        });
        toolbar.add(c_oUpdateRate);
        c_oSound.setToolTipText("Set Sound Preferences");
        c_oSound.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                SoundPreferencesDialog dlg = new SoundPreferencesDialog("Configure the sounds to be played when a " + "event occurs.", c_fSoundsEnabled, c_atSoundDescriptions, c_afSoundEnablers, c_aoSoundFiles);
                dlg.show();
                if (dlg.apply()) {
                    c_fSoundsEnabled = dlg.areSoundsEnabled();
                    c_aoSoundFiles = dlg.getConfiguredSounds();
                    for (int i = c_aoSoundFiles.length; i-- != 0; ) {
                        c_afSoundEnablers[i] = dlg.isSoundEnabled(i);
                        if (c_aoSoundFiles[i] != null) {
                            c_aoSoundClips[i] = Applet.newAudioClip(c_aoSoundFiles[i]);
                        } else {
                            c_afSoundEnablers[i] = false;
                        }
                    }
                }
            }
        });
        toolbar.add(c_oSound);
        toolbar.addSeparator();
        c_oAbout.setToolTipText("About JSETIMon");
        c_oAbout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                String revision = "???";
                if (REVISION.indexOf(" ") != -1 && REVISION.lastIndexOf(" ") != -1) {
                    revision = REVISION.substring(REVISION.indexOf(" "), REVISION.lastIndexOf(" "));
                }
                JOptionPane.showMessageDialog(null, "JSETIMon version " + revision + "\n" + "Java SETI@Home Monitor\n" + "\n" + "Copyright (C) 2001 - 2002 Adam C Jones\n" + "http://jsetimon.sourceforge.net/\n" + "\n" + "Some contributions Copyright (C) 2002 Kent C. Johnson\n" + "\n" + "Icons Copyright (C) 1998 Dean S Jones\n" + "dean@gallant.com www.gallant.com/icons.htm\n" + "\n" + "JSETIMon is free software; you can redistribute it and/or modify\n" + "it under the terms of the GNU General Public License as published by\n" + "the Free Software Foundation; either version 2 of the License, or\n" + "(at your option) any later version.\n", "About JSETIMon", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        toolbar.add(c_oAbout);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent event) {
                try {
                    FileOutputStream fo = new FileOutputStream(JSETIMON_OPTIONS_FILE);
                    fo.write(Integer.toString(c_iWorkUnitUpdateRate).getBytes());
                    fo.write('\n');
                    fo.write(Integer.toString(c_iUserUpdateRate).getBytes());
                    fo.write('\n');
                    fo.write(c_tUserID.getBytes());
                    fo.write('\n');
                    for (int i = 0; i < c_vWorkUnits.size(); i++) {
                        fo.write(((File) c_vWorkUnits.elementAt(i)).getCanonicalPath().getBytes());
                        fo.write('\n');
                    }
                    saveAudioPreferences();
                } catch (Exception e) {
                }
                System.exit(0);
            }
        });
        c_oUsername.setHorizontalAlignment(JTextField.RIGHT);
        c_oRank.setHorizontalAlignment(JTextField.RIGHT);
        c_oWorkUnits.setHorizontalAlignment(JTextField.RIGHT);
        c_oTied.setHorizontalAlignment(JTextField.RIGHT);
        c_oPercentile.setHorizontalAlignment(JTextField.RIGHT);
        c_oTotalUsers.setHorizontalAlignment(JTextField.RIGHT);
        c_oCPUTotal.setHorizontalAlignment(JTextField.RIGHT);
        c_oCPUAverage.setHorizontalAlignment(JTextField.RIGHT);
        c_oUsername.setEditable(false);
        c_oRank.setEditable(false);
        c_oWorkUnits.setEditable(false);
        c_oTied.setEditable(false);
        c_oPercentile.setEditable(false);
        c_oTotalUsers.setEditable(false);
        c_oCPUTotal.setEditable(false);
        c_oCPUAverage.setEditable(false);
        c_oUsername.setBackground(Color.black);
        c_oRank.setBackground(Color.black);
        c_oWorkUnits.setBackground(Color.black);
        c_oTied.setBackground(Color.black);
        c_oPercentile.setBackground(Color.black);
        c_oTotalUsers.setBackground(Color.black);
        c_oCPUTotal.setBackground(Color.black);
        c_oCPUAverage.setBackground(Color.black);
        c_oUsername.setForeground(Color.white);
        c_oRank.setForeground(Color.white);
        c_oWorkUnits.setForeground(Color.white);
        c_oTied.setForeground(Color.white);
        c_oPercentile.setForeground(Color.white);
        c_oTotalUsers.setForeground(Color.white);
        c_oCPUTotal.setForeground(Color.white);
        c_oCPUAverage.setForeground(Color.white);
        c_oUsername.setBorder(null);
        c_oRank.setBorder(null);
        c_oWorkUnits.setBorder(null);
        c_oTied.setBorder(null);
        c_oPercentile.setBorder(null);
        c_oTotalUsers.setBorder(null);
        c_oCPUTotal.setBorder(null);
        c_oCPUAverage.setBorder(null);
        Color purple = new Color(0x0CC, 0x099, 0x0FF);
        USERNAME.setForeground(purple);
        RANK.setForeground(purple);
        WORKUNITS.setForeground(purple);
        TIED.setForeground(purple);
        PERCENTILE.setForeground(purple);
        TOTALUSERS.setForeground(purple);
        CPUTOTAL.setForeground(purple);
        CPUAVERAGE.setForeground(purple);
        topPane.setLayout(new GridLayout(4, 4));
        topPane.add(USERNAME);
        topPane.add(c_oUsername);
        topPane.add(PERCENTILE);
        topPane.add(c_oPercentile);
        topPane.add(RANK);
        topPane.add(c_oRank);
        topPane.add(TOTALUSERS);
        topPane.add(c_oTotalUsers);
        topPane.add(WORKUNITS);
        topPane.add(c_oWorkUnits);
        topPane.add(CPUTOTAL);
        topPane.add(c_oCPUTotal);
        topPane.add(TIED);
        topPane.add(c_oTied);
        topPane.add(CPUAVERAGE);
        topPane.add(c_oCPUAverage);
        c_oTabledata = new DefaultTableModel() {

            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        c_oTabledata.addColumn("Description");
        c_oTabledata.addColumn("% Complete");
        c_oTabledata.addColumn("Elapsed Time");
        c_oTabledata.addColumn("EToC");
        c_oTable = new JTable(c_oTabledata);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer headerrenderer = new DefaultTableCellRenderer();
        renderer.setBackground(Color.black);
        renderer.setForeground(Color.white);
        headerrenderer.setBackground(Color.black);
        headerrenderer.setForeground(purple);
        c_oTable.setGridColor(purple);
        JScrollPane scroller = new JScrollPane(c_oTable);
        splitter.setBottomComponent(scroller);
        c_oTable.getColumn("Description").setCellRenderer(renderer);
        c_oTable.getColumn("Elapsed Time").setCellRenderer(renderer);
        c_oTable.getColumn("EToC").setCellRenderer(renderer);
        c_oTable.getColumn("Description").setHeaderRenderer(headerrenderer);
        c_oTable.getColumn("Elapsed Time").setHeaderRenderer(headerrenderer);
        c_oTable.getColumn("EToC").setHeaderRenderer(headerrenderer);
        c_oTable.getColumn("% Complete").setHeaderRenderer(headerrenderer);
        c_oTable.getColumn("% Complete").setCellRenderer(new TableCellComponentRenderer());
        try {
            String nextline = null;
            JProgressBar bar = null;
            Vector row = null;
            File workunit = null;
            BufferedReader reader = new BufferedReader(new FileReader(JSETIMON_OPTIONS_FILE));
            c_iWorkUnitUpdateRate = Integer.parseInt(reader.readLine());
            c_iUserUpdateRate = Integer.parseInt(reader.readLine());
            c_tUserID = reader.readLine();
            while ((nextline = reader.readLine()) != null) {
                workunit = new File(nextline);
                c_vWorkUnits.add(workunit);
                bar = new JProgressBar(0, 100000000);
                bar.setStringPainted(true);
                bar.setString("0%");
                row = new Vector();
                row.add(nextline);
                row.add(bar);
                row.add("???");
                row.add("???");
                c_oTabledata.addRow(row);
                c_oTable.setRowHeight(c_vWorkUnits.size() - 1, bar.getPreferredSize().height);
            }
            if (c_tUserID == "") {
                UserIDDialog dlg = new UserIDDialog();
                dlg.show();
                try {
                    updateUserStatistics(dlg.getUserID());
                    c_tUserID = dlg.getUserID();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "That was not a valid username.  Keeping old username.", "Bad Username", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
        }
        loadAudioPreferences();
        c_oMonitorThread = new Thread(this);
        c_oMonitorThread.start();
        repack();
        Dimension desktopDimensions = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameDimensions = getSize();
        setLocation((desktopDimensions.width - frameDimensions.width) / 2, (desktopDimensions.height - frameDimensions.height) / 2);
        setVisible(true);
    }
