    private void customize() {
        if (GameDatabase.getMapExtension(roomData.getChannel(), roomData.getModName()) != null) {
            lbl_map.setVisible(true);
            cb_map.setVisible(true);
            if (GameDatabase.getMapLoaderType(roomData.getChannel(), roomData.getModName()) == MapLoaderTypes.FILE) {
                cb_map.setModel(new DefaultComboBoxModel(loadFileMaps()));
            } else if (GameDatabase.getMapLoaderType(roomData.getChannel(), roomData.getModName()) == MapLoaderTypes.PK3) {
                cb_map.setModel(new DefaultComboBoxModel(loadPK3Maps()));
            }
            cb_map.setSelectedItem(TempGameSettings.getMap());
            if (cb_map.getSelectedItem() == null && cb_map.getItemCount() > 0) {
                cb_map.setSelectedIndex(0);
            }
            cb_map.setEnabled(roomData.isHost());
        }
        GridBagConstraints firstcolumn = new GridBagConstraints();
        GridBagConstraints secondcolumn = new GridBagConstraints();
        int serverrowindex = 1;
        int localrowindex = 0;
        int localcount = 0;
        ArrayList<GameSetting> settings = GameDatabase.getGameSettings(roomData.getChannel(), roomData.getModName());
        firstcolumn.gridwidth = 1;
        firstcolumn.gridheight = 1;
        firstcolumn.fill = GridBagConstraints.HORIZONTAL;
        firstcolumn.ipadx = 40;
        firstcolumn.anchor = GridBagConstraints.EAST;
        firstcolumn.weightx = 0;
        firstcolumn.weighty = 0;
        firstcolumn.insets = new Insets(5, 5, 5, 5);
        firstcolumn.gridx = 0;
        secondcolumn.gridwidth = 1;
        secondcolumn.gridheight = 1;
        secondcolumn.fill = GridBagConstraints.HORIZONTAL;
        secondcolumn.ipadx = 0;
        secondcolumn.anchor = GridBagConstraints.CENTER;
        secondcolumn.weightx = 1.0;
        secondcolumn.weighty = 0;
        secondcolumn.insets = new Insets(5, 5, 5, 5);
        secondcolumn.gridx = 1;
        for (GameSetting gs : settings) {
            if (gs.isLocal()) {
                localcount++;
                firstcolumn.gridy = localrowindex;
                secondcolumn.gridy = localrowindex;
            } else {
                firstcolumn.gridy = serverrowindex;
                secondcolumn.gridy = serverrowindex;
            }
            JLabel label = new JLabel(gs.getName());
            label.setHorizontalAlignment(JLabel.RIGHT);
            Component input = null;
            switch(gs.getType()) {
                case TEXT:
                    {
                        input = new JTextField(gs.getDefaultValue());
                        String currentValue = TempGameSettings.getGameSettingValue(gs.getName());
                        if (currentValue != null && currentValue.length() > 0) {
                            ((JTextField) input).setText(currentValue);
                        }
                        if (!roomData.isHost() && !gs.isLocal()) {
                            input.setEnabled(false);
                        }
                        break;
                    }
                case NUMBER:
                    {
                        int def = 0;
                        def = Integer.valueOf((gs.getDefaultValue() == null || gs.getDefaultValue().length() == 0) ? "0" : gs.getDefaultValue());
                        int min = Integer.valueOf(gs.getMinValue());
                        int max = Integer.valueOf(gs.getMaxValue());
                        if (min <= max && min <= def && def <= max) {
                            input = new JSpinner(new SpinnerNumberModel(def, min, max, 1));
                        } else {
                            input = new JSpinner();
                        }
                        String currentValue = TempGameSettings.getGameSettingValue(gs.getName());
                        if (currentValue != null && currentValue.length() > 0) {
                            ((JSpinner) input).setValue(Integer.valueOf(currentValue));
                        }
                        if (!roomData.isHost() && !gs.isLocal()) {
                            input.setEnabled(false);
                        }
                        break;
                    }
                case CHOICE:
                    {
                        if (!roomData.isHost() && !gs.isLocal()) {
                            input = new JTextField(gs.getDefaultValue());
                            String currentValue = TempGameSettings.getGameSettingValue(gs.getName());
                            if (gs.getDefaultValue() != null && gs.getDefaultValue().length() > 0) {
                                ((JTextField) input).setText(gs.getDefaultValue());
                            }
                            if (currentValue != null && currentValue.length() > 0) {
                                ((JTextField) input).setText(currentValue);
                            }
                            input.setEnabled(false);
                        } else {
                            input = new JComboBox(gs.getComboboxSelectNames().toArray());
                            if (gs.getDefaultValue() != null && gs.getDefaultValue().length() > 0) {
                                int idx = -1;
                                idx = gs.getComboboxSelectNames().indexOf(gs.getDefaultValue());
                                if (idx > -1) {
                                    ((JComboBox) input).setSelectedIndex(idx);
                                }
                            }
                            String currentValue = TempGameSettings.getGameSettingValue(gs.getName());
                            if (currentValue != null && currentValue.length() > 0) {
                                ((JComboBox) input).setSelectedItem(currentValue);
                            }
                        }
                        break;
                    }
            }
            labels.add(label);
            inputfields.add(input);
            if (inputfields.size() == 1) {
            } else {
            }
            if (gs.isLocal()) {
                pnl_localSettings.add(label, firstcolumn);
                pnl_localSettings.add(input, secondcolumn);
                localrowindex++;
            } else {
                pnl_serverSettings.add(label, firstcolumn);
                pnl_serverSettings.add(input, secondcolumn);
                serverrowindex++;
            }
        }
        if (localcount == 0) {
            pnl_localSettings.setVisible(false);
        }
    }
