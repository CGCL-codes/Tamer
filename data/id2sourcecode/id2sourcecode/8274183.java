        private void setupComponents() {
            setShadeUnder.setText("Shade under");
            crayonList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            for (int i = 0; i < StipplePattern.LINESTIPPLETYPES.length; i++) {
                setStipplePattern.addItem(StipplePattern.LINESTIPPLETYPES[i]);
            }
            crayonList.addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    configWidgets((Crayon) crayonList.getSelectedValue());
                }
            });
            addButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String seriesName = "Slot " + crayonListModel.getSize();
                    Crayon smd = currentTemplate.createCrayon(seriesName);
                    crayonListModel.addElement(smd);
                    sampleSeriesList.add(createDataSeries(smd));
                    for (int i = 0; i < repModel.getRowCount(); i++) {
                        repModel.getRow(i).addCrayon(smd);
                    }
                    repTable.repaint();
                }
            });
            delButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    final Crayon selectedCrayon = (Crayon) crayonList.getSelectedValue();
                    if (selectedCrayon == null) return;
                    final int crayonIndex = crayonList.getSelectedIndex();
                    if (templateManager.getDataGrid() != null) {
                        List<Strip> strips = templateManager.getDataGrid().getStripList();
                        for (Iterator<Strip> it = strips.iterator(); it.hasNext(); ) {
                            final Strip s = it.next();
                            InputChannelItemInterface channel = s.getChannel(crayonIndex);
                            if (channel != null) {
                                JOptionPane.showMessageDialog(templateManager, CHANNEL_IN_USE_MSG, CHANNEL_IN_USE_TITLE, JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    }
                    for (int i = 0; i < repModel.getRowCount(); i++) {
                        repModel.getRow(i).delCrayon(selectedCrayon);
                    }
                    removeDataSeries(selectedCrayon);
                    crayonListModel.removeElement(selectedCrayon);
                    currentTemplate.removeCrayon(selectedCrayon);
                    repTable.repaint();
                }
            });
            setColor.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (listen == false) return;
                    JColorChooser chooser = new JColorChooser();
                    JColorChooser.createDialog(templateManager, setColorLabel.getText(), true, chooser, new ColorActionListener(chooser) {

                        public void actionPerformed(ActionEvent e) {
                            setColor.setBackground(this.chooser.getColor());
                            Crayon smd = getCurrentCrayon();
                            smd.setColor(this.chooser.getColor());
                            getDataSeries(smd).setCrayonColor(this.chooser.getColor());
                            repModel.rebuildIcons();
                        }
                    }, null).setVisible(true);
                }
            });
            setShadeUnder.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    if (listen == false) return;
                    Crayon smd = getCurrentCrayon();
                    smd.setShadeUnder(setShadeUnder.isSelected());
                    getDataSeries(smd).setShadeUnder(setShadeUnder.isSelected());
                    repModel.rebuildIcons();
                }
            });
            setShadeOpacity.getModel().addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    if (listen == false) return;
                    Crayon smd = getCurrentCrayon();
                    smd.setShadeOpacity(((Double) setShadeOpacity.getValue()).floatValue());
                    getDataSeries(smd).setShadeOpacity(((Double) setShadeOpacity.getValue()).floatValue());
                    repModel.rebuildIcons();
                }
            });
            setStipplePattern.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (listen == false) return;
                    Crayon smd = getCurrentCrayon();
                    StipplePattern p = StipplePattern.getStippleByName((String) setStipplePattern.getSelectedItem());
                    smd.setStipplePattern(p);
                    getDataSeries(smd).setStipplePattern(p);
                    repModel.rebuildIcons();
                }
            });
        }
