    public CompleteView_A_In() {
        super();
        this.setLayout(new BorderLayout());
        {
            Box box_oszilloscope = new Box(SwingConstants.VERTICAL);
            graph2 = new Graph2(this);
            box_oszilloscope.add(graph2);
            {
                jScrollBar_oszi.addAdjustmentListener(new java.awt.event.AdjustmentListener() {

                    @Override
                    public void adjustmentValueChanged(java.awt.event.AdjustmentEvent e) {
                        start = new Integer(jScrollBar_oszi.getValue());
                        fireDataChanged();
                    }
                });
                box_oszilloscope.add(jScrollBar_oszi);
            }
            this.add(box_oszilloscope, BorderLayout.CENTER);
            box_oszilloscope.setAlignmentX(0.5f);
        }
        {
            JPanel panel_control = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.ipady = 5;
            gbc.insets = new Insets(1, 20, 1, 20);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.NONE;
            panel_control.add(new JCopyableLabel("Einstellungen"), gbc);
            {
                jButton_start_reset = new JButton("Start");
                jButton_start_reset.addActionListener(new java.awt.event.ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (time_data == null) {
                            command_start();
                            jCheckBox_EnableTable.setSelected(false);
                            jCheckBox_EnableTable.setEnabled(false);
                        } else {
                            command_reset();
                            jCheckBox_EnableTable.setEnabled(true);
                        }
                    }
                });
                gbc.gridy = 1;
                gbc.insets = new Insets(1, 8, 1, 8);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                panel_control.add(jButton_start_reset, gbc);
            }
            {
                jButton_pause_unpause = new JButton("Pause");
                jButton_pause_unpause.addActionListener(new java.awt.event.ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (isPaused) {
                            jButton_pause_unpause.setText("Pause");
                            isPaused = false;
                            jCheckBox_EnableTable.setSelected(false);
                        } else {
                            jButton_pause_unpause.setText("Fortsetzen");
                            isPaused = true;
                        }
                        jCheckBox_EnableTable.setEnabled(isPaused);
                    }
                });
                gbc.gridy = 2;
                jButton_pause_unpause.setEnabled(false);
                isPaused = true;
                panel_control.add(jButton_pause_unpause, gbc);
            }
            {
                JButton jButton_save = new JButton("Speichern");
                jButton_save.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        command_save();
                    }
                });
                gbc.gridy = 3;
                panel_control.add(jButton_save, gbc);
            }
            {
                jCheckBox_EnableTable = new JCheckBox("Tabelle anzeigen", false);
                jCheckBox_EnableTable.addItemListener(new java.awt.event.ItemListener() {

                    public void itemStateChanged(java.awt.event.ItemEvent e) {
                        if (jCheckBox_EnableTable.isSelected()) {
                            CompleteView_A_In.this.add(jScrollPane_Table, BorderLayout.SOUTH);
                            osziTableModel.fireColumnInserted();
                        } else {
                            CompleteView_A_In.this.remove(jScrollPane_Table);
                        }
                        CompleteView_A_In.this.repaint();
                        SwingUtilities.updateComponentTreeUI(CompleteView_A_In.this);
                    }
                });
                gbc.insets = new Insets(0, 8, 0, 8);
                gbc.gridy = 4;
                panel_control.add(jCheckBox_EnableTable, gbc);
            }
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.NONE;
            panel_control.add(new JCopyableLabel("Kanal 1 : V shift"));
            {
                jKnob_vShift1.setEnabled(c1_enabled);
                jKnob_vShift1.setText(jKnob_vShift1.getValue() + "/255");
                jKnob_vShift1.addChangeListener(new javax.swing.event.ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent e) {
                        c1_VShift = -jKnob_vShift1.getValue();
                        graph2.repaint();
                        jKnob_vShift1.setText(jKnob_vShift1.getValue() + "/255");
                    }
                });
                gbc.gridy = 1;
                gbc.gridheight = 4;
                panel_control.add(jKnob_vShift1, gbc);
            }
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.gridheight = 1;
            panel_control.add(new JCopyableLabel("Kanal 2 : V shift"), gbc);
            {
                jKnob_vShift2 = new JKnob(-255, 255, 0, 2);
                jKnob_vShift2.setEnabled(c2_enabled);
                jKnob_vShift2.setText(jKnob_vShift2.getValue() + "/255");
                jKnob_vShift2.addChangeListener(new javax.swing.event.ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent e) {
                        c2_VShift = -jKnob_vShift2.getValue();
                        graph2.repaint();
                        jKnob_vShift2.setText(jKnob_vShift2.getValue() + "/255");
                    }
                });
                gbc.gridheight = 4;
                gbc.gridy = 1;
                panel_control.add(jKnob_vShift2, gbc);
            }
            gbc.gridx = 3;
            gbc.gridy = 0;
            gbc.gridheight = 1;
            panel_control.add(new JCopyableLabel("Time DIV : zoom"), gbc);
            {
                final JKnob jKnob_Time_DIV = new JKnob(1, 2000, (int) ((Math.log(zoom) / Math.log(2) + 15) * 100), 10);
                jKnob_Time_DIV.setStartingAngle(0);
                jKnob_Time_DIV.addChangeListener(new ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent e) {
                        zoom = new Double(Math.pow(2, jKnob_Time_DIV.getValue() / 100d - 15));
                        fireDataChanged();
                    }
                });
                gbc.gridheight = 4;
                gbc.gridy = 1;
                panel_control.add(jKnob_Time_DIV, gbc);
            }
            {
                final JCheckBox jCheckBox_enableChannel1 = new JCheckBox("Kanal 1 aktiveren", c1_enabled);
                jCheckBox_enableChannel1.addChangeListener(new javax.swing.event.ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent e) {
                        boolean isSelected = jCheckBox_enableChannel1.isSelected();
                        if (c1_enabled != isSelected) {
                            c1_enabled = isSelected;
                            jKnob_vShift1.setEnabled(isSelected);
                            command_reset();
                            if (!c1_enabled & !c2_enabled) return;
                            command_start();
                        }
                    }
                });
                gbc.gridx = 4;
                gbc.gridheight = 1;
                gbc.gridy = 0;
                panel_control.add(jCheckBox_enableChannel1, gbc);
            }
            {
                final JButton jButton_colorChannel1 = new JButton("Farbe Kanal1");
                jButton_colorChannel1.setBackground(c1_color);
                jButton_colorChannel1.setForeground(inverseColor(c1_color));
                jButton_colorChannel1.addActionListener(new java.awt.event.ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        c1_color = chooseColor(1, c1_color);
                        jButton_colorChannel1.setBackground(c1_color);
                        jButton_colorChannel1.setForeground(inverseColor(c1_color));
                        graph2.repaint();
                    }
                });
                gbc.gridy = 1;
                panel_control.add(jButton_colorChannel1, gbc);
            }
            {
                final JCheckBox jCheckBox_enableChannel2 = new JCheckBox("Kanal 2 aktivieren", c2_enabled);
                jCheckBox_enableChannel2.addChangeListener(new javax.swing.event.ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent e) {
                        boolean isSelected = jCheckBox_enableChannel2.isSelected();
                        if (c2_enabled != isSelected) {
                            c2_enabled = isSelected;
                            jKnob_vShift2.setEnabled(isSelected);
                            command_reset();
                            command_start();
                        }
                    }
                });
                gbc.gridy = 3;
                panel_control.add(jCheckBox_enableChannel2, gbc);
            }
            {
                final JButton jButton_colorChannel2 = new JButton("Farbe Kanal2");
                jButton_colorChannel2.setBackground(c2_color);
                jButton_colorChannel2.setForeground(inverseColor(c2_color));
                jButton_colorChannel2.addActionListener(new java.awt.event.ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        c2_color = chooseColor(2, c2_color);
                        jButton_colorChannel2.setBackground(c2_color);
                        jButton_colorChannel2.setForeground(inverseColor(c2_color));
                        CompleteView_A_In.this.repaint();
                    }
                });
                gbc.gridy = 4;
                panel_control.add(jButton_colorChannel2, gbc);
            }
            {
                currentMousePosition = new JTextArea("In den Graphen\nrechtsklicken,\num die Werte\nanzuzeigen");
                currentMousePosition.setEditable(false);
                currentMousePosition.setBackground(Color.WHITE);
                currentMousePosition.setBorder(BorderFactory.createTitledBorder("aktueller Wert"));
                currentMousePosition.setMinimumSize(new Dimension(120, 146));
                currentMousePosition.setPreferredSize(new Dimension(120, 146));
                gbc.gridheight = 5;
                gbc.gridx = 5;
                gbc.gridy = 0;
                panel_control.add(currentMousePosition, gbc);
            }
            panel_control.setPreferredSize(new Dimension(panel_control.getMinimumSize().width, 180));
            panel_control.setMaximumSize(panel_control.getPreferredSize());
            panel_control.setAlignmentX(0.5f);
            panel_control.setPreferredSize(panel_control.getMinimumSize());
            this.add(panel_control, BorderLayout.NORTH);
        }
        {
            werteTabelle = new JTable(2, 0);
            jScrollPane_Table = new JScrollPane(werteTabelle);
            jScrollPane_Table.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            jScrollPane_Table.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            werteTabelle.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            werteTabelle.setCellSelectionEnabled(true);
            osziTableModel = new OsziTableModel(this);
            werteTabelle.setModel(osziTableModel);
            werteTabelle.getColumnModel().setColumnSelectionAllowed(false);
            werteTabelle.getTableHeader().setReorderingAllowed(false);
            jScrollPane_Table.setMinimumSize(new Dimension(0, (int) (JK8055GUI.FONTMETRICS.getHeight() * 4.5)));
            jScrollPane_Table.setMaximumSize(new Dimension(JK8055GUI.WINDOW_SIZE.width, (int) (JK8055GUI.FONTMETRICS.getHeight() * 4.5)));
            jScrollPane_Table.setPreferredSize(new Dimension(Integer.MAX_VALUE, (int) (JK8055GUI.FONTMETRICS.getHeight() * 4.5)));
        }
        ioListener = new IOListener() {

            @Override
            public K8055Channel getChannel() {
                return null;
            }

            @Override
            public IOChannels getDataType() {
                return IOChannels.ANALOG;
            }

            @Override
            public Component getTargetComponent() {
                return CompleteView_A_In.this;
            }

            @Override
            public boolean listenToAllChannels() {
                return true;
            }
        };
        fireDataChanged();
    }
