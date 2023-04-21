    private void initActions() {
        fileAction = new DataViewerAction("File", "File Menu", KeyEvent.VK_F);
        connectAction = new DataViewerAction("Connect", "Connect to RBNB server", KeyEvent.VK_C, KeyStroke.getKeyStroke(KeyEvent.VK_C, menuShortcutKeyMask | ActionEvent.SHIFT_MASK)) {

            /** serialization version identifier */
            private static final long serialVersionUID = 5038790506859429244L;

            public void actionPerformed(ActionEvent ae) {
                if (rbnbConnectionDialog == null) {
                    rbnbConnectionDialog = new RBNBConnectionDialog(frame, rbnb, dataPanelManager);
                } else {
                    rbnbConnectionDialog.setVisible(true);
                }
            }
        };
        disconnectAction = new DataViewerAction("Disconnect", "Disconnect from RBNB server", KeyEvent.VK_D, KeyStroke.getKeyStroke(KeyEvent.VK_D, menuShortcutKeyMask | ActionEvent.SHIFT_MASK)) {

            /** serialization version identifier */
            private static final long serialVersionUID = -1871076535376405181L;

            public void actionPerformed(ActionEvent ae) {
                dataPanelManager.closeAllDataPanels();
                rbnb.disconnect();
            }
        };
        loginAction = new DataViewerAction("Login", "Login as a NEES user") {

            /** serialization version identifier */
            private static final long serialVersionUID = 6105503896620555072L;

            public void actionPerformed(ActionEvent ae) {
                if (loginDialog == null) {
                    loginDialog = new LoginDialog(frame);
                } else {
                    loginDialog.setVisible(true);
                }
            }
        };
        logoutAction = new DataViewerAction("Logout", "Logout as a NEES user") {

            /** serialization version identifier */
            private static final long serialVersionUID = -2517567766044673777L;

            public void actionPerformed(ActionEvent ae) {
                AuthenticationManager.getInstance().setAuthentication(null);
            }
        };
        loadAction = new DataViewerAction("Load Setup", "Load data viewer setup from file") {

            /** serialization version identifier */
            private static final long serialVersionUID = 7197815395398039821L;

            public void actionPerformed(ActionEvent ae) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new RDVFileFilter());
                chooser.setApproveButtonText("Load");
                chooser.setApproveButtonToolTipText("Load selected file");
                int returnVal = chooser.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File configFile = chooser.getSelectedFile();
                    try {
                        URL configURL = configFile.toURI().toURL();
                        ConfigurationManager.loadConfiguration(configURL);
                    } catch (MalformedURLException e) {
                        DataViewer.alertError("\"" + configFile + "\" is not a valid configuration file URL.");
                    }
                }
            }
        };
        saveAction = new DataViewerAction("Save Setup", "Save data viewer setup to file") {

            /** serialization version identifier */
            private static final long serialVersionUID = -8259994975940624038L;

            public void actionPerformed(ActionEvent ae) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new RDVFileFilter());
                int returnVal = chooser.showSaveDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    if (file.getName().indexOf(".") == -1) {
                        file = new File(file.getAbsolutePath() + ".rdv");
                    }
                    if (file.exists()) {
                        int overwriteReturn = JOptionPane.showConfirmDialog(null, file.getName() + " already exists. Do you want to overwrite it?", "Overwrite file?", JOptionPane.YES_NO_OPTION);
                        if (overwriteReturn == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }
                    ConfigurationManager.saveConfiguration(file);
                }
            }
        };
        importAction = new DataViewerAction("Import", "Import Menu", KeyEvent.VK_I, "icons/import.gif");
        exportAction = new DataViewerAction("Export", "Export Menu", KeyEvent.VK_E, "icons/export.gif");
        exportVideoAction = new DataViewerAction("Export video channels", "Export video on the server to the local computer") {

            /** serialization version identifier */
            private static final long serialVersionUID = -6420430928972633313L;

            public void actionPerformed(ActionEvent ae) {
                showExportVideoDialog();
            }
        };
        exitAction = new DataViewerAction("Exit", "Exit RDV", KeyEvent.VK_X) {

            /** serialization version identifier */
            private static final long serialVersionUID = 3137490972014710133L;

            public void actionPerformed(ActionEvent ae) {
                Application.getInstance().exit(ae);
            }
        };
        controlAction = new DataViewerAction("Control", "Control Menu", KeyEvent.VK_C);
        realTimeAction = new DataViewerAction("Real Time", "View data in real time", KeyEvent.VK_R, KeyStroke.getKeyStroke(KeyEvent.VK_R, menuShortcutKeyMask), "icons/rt.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = -7564783609370910512L;

            public void actionPerformed(ActionEvent ae) {
                rbnb.monitor();
            }
        };
        playAction = new DataViewerAction("Play", "Playback data", KeyEvent.VK_P, KeyStroke.getKeyStroke(KeyEvent.VK_P, menuShortcutKeyMask), "icons/play.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = 5974457444931142938L;

            public void actionPerformed(ActionEvent ae) {
                rbnb.play();
            }
        };
        pauseAction = new DataViewerAction("Pause", "Pause data display", KeyEvent.VK_A, KeyStroke.getKeyStroke(KeyEvent.VK_S, menuShortcutKeyMask), "icons/pause.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = -5297742186923194460L;

            public void actionPerformed(ActionEvent ae) {
                rbnb.pause();
            }
        };
        beginningAction = new DataViewerAction("Go to beginning", "Move the location to the start of the data", KeyEvent.VK_B, KeyStroke.getKeyStroke(KeyEvent.VK_B, menuShortcutKeyMask), "icons/begin.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = 9171304956895497898L;

            public void actionPerformed(ActionEvent ae) {
                controlPanel.setLocationBegin();
            }
        };
        endAction = new DataViewerAction("Go to end", "Move the location to the end of the data", KeyEvent.VK_E, KeyStroke.getKeyStroke(KeyEvent.VK_E, menuShortcutKeyMask), "icons/end.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = 1798579248452726211L;

            public void actionPerformed(ActionEvent ae) {
                controlPanel.setLocationEnd();
            }
        };
        gotoTimeAction = new DataViewerAction("Go to time", "Move the location to specific date time of the data", KeyEvent.VK_T, KeyStroke.getKeyStroke(KeyEvent.VK_T, menuShortcutKeyMask)) {

            /** serialization version identifier */
            private static final long serialVersionUID = -6411442297488926326L;

            public void actionPerformed(ActionEvent ae) {
                TimeRange timeRange = RBNBHelper.getChannelsTimeRange();
                double time = DateTimeDialog.showDialog(frame, rbnb.getLocation(), timeRange.start, timeRange.end);
                if (time >= 0) {
                    rbnb.setLocation(time);
                }
            }
        };
        updateChannelListAction = new DataViewerAction("Update Channel List", "Update the channel list", KeyEvent.VK_U, KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "icons/refresh.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = -170096772973697277L;

            public void actionPerformed(ActionEvent ae) {
                rbnb.updateMetadata();
            }
        };
        dropDataAction = new DataViewerAction("Drop Data", "Drop data if plaback can't keep up with data rate", KeyEvent.VK_D, "icons/drop_data.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = 7079791364881120134L;

            public void actionPerformed(ActionEvent ae) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) ae.getSource();
                rbnb.dropData(menuItem.isSelected());
            }
        };
        viewAction = new DataViewerAction("View", "View Menu", KeyEvent.VK_V);
        showChannelListAction = new DataViewerAction("Show Channels", "", KeyEvent.VK_L, "icons/channels.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = 4982129759386009112L;

            public void actionPerformed(ActionEvent ae) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) ae.getSource();
                channelListPanel.setVisible(menuItem.isSelected());
                layoutSplitPane();
                leftPanel.resetToPreferredSizes();
            }
        };
        showMetadataPanelAction = new DataViewerAction("Show Properties", "", KeyEvent.VK_P, "icons/properties.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = 430106771704397810L;

            public void actionPerformed(ActionEvent ae) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) ae.getSource();
                metadataPanel.setVisible(menuItem.isSelected());
                layoutSplitPane();
                leftPanel.resetToPreferredSizes();
            }
        };
        showControlPanelAction = new DataViewerAction("Show Control Panel", "", KeyEvent.VK_C, "icons/control.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = 6401715717710735485L;

            public void actionPerformed(ActionEvent ae) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) ae.getSource();
                controlPanel.setVisible(menuItem.isSelected());
            }
        };
        showAudioPlayerPanelAction = new DataViewerAction("Show Audio Player", "", KeyEvent.VK_A, "icons/audio.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = -4248275698973916287L;

            public void actionPerformed(ActionEvent ae) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) ae.getSource();
                audioPlayerPanel.setVisible(menuItem.isSelected());
            }
        };
        showMarkerPanelAction = new DataViewerAction("Show Marker Panel", "", KeyEvent.VK_M, "icons/info.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = -5253555511660929640L;

            public void actionPerformed(ActionEvent ae) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) ae.getSource();
                markerSubmitPanel.setVisible(menuItem.isSelected());
            }
        };
        dataPanelAction = new DataViewerAction("Arrange", "Arrange Data Panel Orientation", KeyEvent.VK_D);
        dataPanelHorizontalLayoutAction = new DataViewerAction("Horizontal Data Panel Orientation", "", -1, "icons/vertical.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = 3356151813557187908L;

            public void actionPerformed(ActionEvent ae) {
                dataPanelContainer.setLayout(DataPanelContainer.VERTICAL_LAYOUT);
            }
        };
        dataPanelVerticalLayoutAction = new DataViewerAction("Vertical Data Panel Orientation", "", -1, "icons/horizontal.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = -4629920180285927138L;

            public void actionPerformed(ActionEvent ae) {
                dataPanelContainer.setLayout(DataPanelContainer.HORIZONTAL_LAYOUT);
            }
        };
        showHiddenChannelsAction = new DataViewerAction("Show Hidden Channels", "", KeyEvent.VK_H, KeyStroke.getKeyStroke(KeyEvent.VK_H, menuShortcutKeyMask), "icons/hidden.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = -2723464261568074033L;

            public void actionPerformed(ActionEvent ae) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) ae.getSource();
                boolean selected = menuItem.isSelected();
                channelListPanel.showHiddenChannels(selected);
            }
        };
        hideEmptyTimeAction = new DataViewerAction("Hide time with no data", "", KeyEvent.VK_D) {

            /** serialization version identifier */
            private static final long serialVersionUID = -3123608144249355642L;

            public void actionPerformed(ActionEvent ae) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) ae.getSource();
                boolean selected = menuItem.isSelected();
                controlPanel.hideEmptyTime(selected);
            }
        };
        fullScreenAction = new DataViewerAction("Full Screen", "", KeyEvent.VK_F, KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0)) {

            /** serialization version identifier */
            private static final long serialVersionUID = -6882310862616235602L;

            public void actionPerformed(ActionEvent ae) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) ae.getSource();
                if (menuItem.isSelected()) {
                    if (enterFullScreenMode()) {
                        menuItem.setSelected(true);
                    } else {
                        menuItem.setSelected(false);
                    }
                } else {
                    leaveFullScreenMode();
                    menuItem.setSelected(false);
                }
            }
        };
        windowAction = new DataViewerAction("Window", "Window Menu", KeyEvent.VK_W);
        closeAllDataPanelsAction = new DataViewerAction("Close all data panels", "", KeyEvent.VK_C, "icons/closeall.gif") {

            /** serialization version identifier */
            private static final long serialVersionUID = -8104876009869238037L;

            public void actionPerformed(ActionEvent ae) {
                dataPanelManager.closeAllDataPanels();
            }
        };
        helpAction = new DataViewerAction("Help", "Help Menu", KeyEvent.VK_H);
        usersGuideAction = new DataViewerAction("RDV Help", "Open the RDV User's Guide", KeyEvent.VK_H, KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)) {

            /** serialization version identifier */
            private static final long serialVersionUID = -2837190869008153291L;

            public void actionPerformed(ActionEvent ae) {
                try {
                    URL usersGuideURL = new URL("http://it.nees.org/library/telepresence/rdv-19-users-guide.php");
                    DataViewer.browse(usersGuideURL);
                } catch (Exception e) {
                }
            }
        };
        supportAction = new DataViewerAction("RDV Support", "Get support from NEESit", KeyEvent.VK_S) {

            /** serialization version identifier */
            private static final long serialVersionUID = -6855670513381679226L;

            public void actionPerformed(ActionEvent ae) {
                try {
                    URL supportURL = new URL("http://it.nees.org/support/");
                    DataViewer.browse(supportURL);
                } catch (Exception e) {
                }
            }
        };
        releaseNotesAction = new DataViewerAction("Release Notes", "Open the RDV Release Notes", KeyEvent.VK_R) {

            /** serialization version identifier */
            private static final long serialVersionUID = 7223639998298692494L;

            public void actionPerformed(ActionEvent ae) {
                try {
                    URL releaseNotesURL = new URL("http://it.nees.org/library/rdv/rdv-release-notes.php");
                    DataViewer.browse(releaseNotesURL);
                } catch (Exception e) {
                }
            }
        };
        aboutAction = new DataViewerAction("About RDV", "", KeyEvent.VK_A) {

            /** serialization version identifier */
            private static final long serialVersionUID = 3978467903181198979L;

            public void actionPerformed(ActionEvent ae) {
                showAboutDialog();
            }
        };
    }
