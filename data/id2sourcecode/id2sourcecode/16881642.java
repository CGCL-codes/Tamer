    private void makeFindMinMaxPanel() {
        markerPos_Text.setEditable(true);
        pvSetVal_Text.setEditable(false);
        pvRBVal_Text.setEditable(false);
        markerPos_Text.setDecimalFormat(val_Format);
        pvSetVal_Text.setDecimalFormat(val_Format);
        pvRBVal_Text.setDecimalFormat(val_Format);
        markerPos_Text.setHorizontalAlignment(JTextField.CENTER);
        pvSetVal_Text.setHorizontalAlignment(JTextField.CENTER);
        pvRBVal_Text.setHorizontalAlignment(JTextField.CENTER);
        markerPos_Text.removeInnerFocusListener();
        pvSetVal_Text.removeInnerFocusListener();
        pvRBVal_Text.removeInnerFocusListener();
        findMinMaxPanel.setLayout(new BorderLayout());
        Border etchedBorder = BorderFactory.createEtchedBorder();
        findMinMaxPanel.setBorder(etchedBorder);
        JPanel temp_0 = new JPanel();
        temp_0.setLayout(new GridLayout(1, 2, 1, 1));
        temp_0.add(markerPos_Label);
        temp_0.add(markerPos_Text);
        JPanel temp_1 = new JPanel();
        temp_1.setLayout(new BorderLayout());
        temp_1.add(find_Button, BorderLayout.NORTH);
        temp_1.add(temp_0, BorderLayout.CENTER);
        temp_1.add(setVal_Button, BorderLayout.SOUTH);
        JPanel temp_2 = new JPanel();
        temp_2.setLayout(new GridLayout(2, 2, 1, 1));
        temp_2.add(pvSet_Label);
        temp_2.add(pvSetVal_Text);
        temp_2.add(pvRB_Label);
        temp_2.add(pvRBVal_Text);
        JPanel temp_3 = new JPanel();
        temp_3.setLayout(new BorderLayout());
        temp_3.add(temp_1, BorderLayout.NORTH);
        temp_3.add(temp_2, BorderLayout.CENTER);
        temp_3.add(readVal_Button, BorderLayout.SOUTH);
        findMinMaxPanel.add(temp_3, BorderLayout.NORTH);
        dragVerLine_Listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind = graphAnalysis.getDraggedLineIndex();
                markerPos = graphAnalysis.getVerticalValue(ind);
                markerPos -= phase_shift;
                if (phase_shift != 0.) {
                    markerPos += 180.;
                    while (markerPos < 0.) {
                        markerPos += 360.;
                    }
                    markerPos = markerPos % 360.;
                    markerPos -= 180.;
                }
                markerPos_Text.setValueQuietly(markerPos);
            }
        };
        markerPos_Text.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graphAnalysis.addDraggedVerLinesListener(null);
                markerPos = markerPos_Text.getValue();
                double phase = markerPos + phase_shift;
                if (phase_shift != 0.) {
                    phase += 180.;
                    while (phase < 0.) {
                        phase += 360.;
                    }
                    phase = phase % 360.;
                    phase -= 180.;
                }
                graphAnalysis.setVerticalLineValue(phase, 0);
                graphAnalysis.addDraggedVerLinesListener(dragVerLine_Listener);
            }
        });
        findMaxMin_Listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BasicGraphData gd = mainController.getChoosenDraphData();
                if (gd != null) {
                    graphAnalysis.removeGraphData(graphDataLocal);
                    graphDataLocal.removeAllPoints();
                    if (gd.getNumbOfPoints() > 0) {
                        GraphDataOperations.polynomialFit(gd, graphDataLocal, graphAnalysis.getCurrentMinX(), graphAnalysis.getCurrentMaxX(), 2, 10);
                        double d_max_pos = GraphDataOperations.getExtremumPosition(graphDataLocal, graphAnalysis.getCurrentMinX(), graphAnalysis.getCurrentMaxX());
                        if (d_max_pos > graphAnalysis.getCurrentMinX() && d_max_pos < graphAnalysis.getCurrentMaxX()) {
                            phase_shift = mainController.getPhaseShift(gd);
                            graphAnalysis.addDraggedVerLinesListener(null);
                            d_max_pos -= phase_shift;
                            if (phase_shift != 0.) {
                                d_max_pos += 180.;
                                while (d_max_pos < 0.) {
                                    d_max_pos += 360.;
                                }
                                d_max_pos = d_max_pos % 360.;
                                d_max_pos -= 180.;
                            }
                            markerPos_Text.setValue(d_max_pos);
                            graphAnalysis.addDraggedVerLinesListener(dragVerLine_Listener);
                            messageTextLocal.setText(null);
                            messageTextLocal.setText("Extremum has been found. The phase_shift value =" + val_Format.format(phase_shift));
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                            messageTextLocal.setText(null);
                            messageTextLocal.setText("Cannot find extremum in the specified region.");
                            graphDataLocal.removeAllPoints();
                            graphAnalysis.refreshGraphJPanel();
                        }
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                        messageTextLocal.setText(null);
                        messageTextLocal.setText("The graph does not have data points.");
                    }
                    graphAnalysis.addGraphData(graphDataLocal);
                } else {
                    messageTextLocal.setText(null);
                    messageTextLocal.setText("Please choose graph and point first. Use S-button on the graph panel.");
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        };
        setVal_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                double val = markerPos_Text.getValue();
                if (scanVariable.getChannel() != null) {
                    scanVariable.setValue(val);
                } else {
                    messageTextLocal.setText(null);
                    messageTextLocal.setText("The scan PV channel does not exist.");
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        readVal_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (scanVariable.getChannel() != null) {
                    pvSetVal_Text.setValue(scanVariable.getValue());
                } else {
                    pvSetVal_Text.setText(null);
                    pvSetVal_Text.setBackground(Color.white);
                }
                if (scanVariable.getChannelRB() != null) {
                    pvRBVal_Text.setValue(scanVariable.getValueRB());
                } else {
                    pvRBVal_Text.setText(null);
                    pvRBVal_Text.setBackground(Color.white);
                }
            }
        });
        find_Button.addActionListener(findMaxMin_Listener);
        find_Button.setForeground(Color.blue);
        setVal_Button.setForeground(Color.blue);
        readVal_Button.setForeground(Color.blue);
    }
