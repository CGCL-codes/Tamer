    public MakeRawToEmittancePanel(FunctionGraphsJPanel GP_in) {
        GP = GP_in;
        SimpleChartPopupMenu.addPopupMenuTo(GP_sp);
        GP_sp.setOffScreenImageDrawing(true);
        GP_sp.setGraphBackGroundColor(Color.black);
        GP_sp.setGridLinesVisibleX(false);
        GP_sp.setGridLinesVisibleY(false);
        GP_sp.setName("Wires' Signal Contour Plot");
        GP_sp.setAxisNames("position #", "channel #");
        GP_sp.setNumberFormatX(int_Format);
        GP_sp.setNumberFormatY(int_Format);
        GP_sp.setLimitsAndTicksX(0., 50., 2, 4);
        GP_sp.setLimitsAndTicksY(0., 5.0, 6, 4);
        posX_Text = GP_sp.getClickedPointObject().xValueText;
        posY_Text = GP_sp.getClickedPointObject().yValueText;
        value_Text = GP_sp.getClickedPointObject().zValueText;
        GP_sp.getClickedPointObject().xValueFormat.applyPattern(int_Format.toPattern());
        GP_sp.getClickedPointObject().yValueFormat = new FortranNumberFormat("G5.1");
        GP_sp.getClickedPointObject().zValueFormat = dbl_Format;
        GP_sp.getClickedPointObject().pointColor = Color.white;
        wireSignalData.getPlotData(0).setColorGenerator(colorGen_sp);
        GP_sp.setColorSurfaceData(wireSignalData.getPlotData(0));
        SimpleChartPopupMenu.addPopupMenuTo(GP_ep);
        GP_ep.setOffScreenImageDrawing(true);
        GP_ep.setGraphBackGroundColor(Color.black);
        GP_ep.setGridLinesVisibleX(false);
        GP_ep.setGridLinesVisibleY(false);
        GP_ep.setName("Emittance Contour Plot");
        GP_ep.setAxisNames("x, [mm]", "xp, [mrad]");
        emittance3D.setColorGenerator(colorGen_ep);
        GP_ep.setColorSurfaceData(emittance3D);
        emittance3D.setScreenResolution(emScrResX, emScrResY);
        emittance3D.setSize(emSizeX, emSizeY);
        emittance3Da.setScreenResolution(emScrResX, emScrResY);
        emittance3Da.setSize(emSizeX, emSizeY);
        useFilter_Button.setSelected(false);
        useGraphData_Button.setSelected(false);
        useHarpPos_Spinner.setAlignmentX(JSpinner.CENTER_ALIGNMENT);
        distHS_Label.setHorizontalAlignment(SwingConstants.CENTER);
        energy_Label.setHorizontalAlignment(SwingConstants.CENTER);
        distWW_Label.setHorizontalAlignment(SwingConstants.CENTER);
        distHS_Text.setDecimalFormat(dbl_Format);
        energy_Text.setDecimalFormat(dbl_Format);
        distWW_Text.setDecimalFormat(dbl_Format);
        distHS_Text.setHorizontalAlignment(JTextField.CENTER);
        energy_Text.setHorizontalAlignment(JTextField.CENTER);
        distWW_Text.setHorizontalAlignment(JTextField.CENTER);
        distHS_Text.setBackground(Color.white);
        energy_Text.setBackground(Color.white);
        distWW_Text.setBackground(Color.white);
        distHS_Text.setEditable(false);
        energy_Text.setEditable(false);
        distWW_Text.setEditable(false);
        gaussAmp_Label.setHorizontalAlignment(SwingConstants.CENTER);
        gaussWidth_Label.setHorizontalAlignment(SwingConstants.CENTER);
        threshold_Label.setHorizontalAlignment(SwingConstants.CENTER);
        gaussAmp_Text.setDecimalFormat(dbl_Format);
        gaussWidth_Text.setDecimalFormat(dbl_Format);
        gaussWidth_Text.setEditable(false);
        gaussWidth_Text.setBackground(Color.white);
        threshold_Text.setDecimalFormat(dbl_Format);
        gaussAmp_Text.setHorizontalAlignment(JTextField.CENTER);
        gaussWidth_Text.setHorizontalAlignment(JTextField.CENTER);
        threshold_Text.setHorizontalAlignment(JTextField.CENTER);
        gaussAmp_Text.setBackground(Color.white);
        gaussWidth_Text.setBackground(Color.white);
        threshold_Text.setBackground(Color.white);
        posX_Label.setHorizontalAlignment(SwingConstants.CENTER);
        posY_Label.setHorizontalAlignment(SwingConstants.CENTER);
        value_Label.setHorizontalAlignment(SwingConstants.CENTER);
        posX_Text.setHorizontalAlignment(JTextField.CENTER);
        posY_Text.setHorizontalAlignment(JTextField.CENTER);
        value_Text.setHorizontalAlignment(JTextField.CENTER);
        emScrResX_Label.setHorizontalAlignment(SwingConstants.CENTER);
        emScrResY_Label.setHorizontalAlignment(SwingConstants.CENTER);
        emSizeX_Label.setHorizontalAlignment(SwingConstants.CENTER);
        emSizeY_Label.setHorizontalAlignment(SwingConstants.CENTER);
        posCenterX_Label.setHorizontalAlignment(SwingConstants.CENTER);
        posCenterXP_Label.setHorizontalAlignment(SwingConstants.CENTER);
        posCenterX_Text.setHorizontalAlignment(JTextField.CENTER);
        posCenterXP_Text.setHorizontalAlignment(JTextField.CENTER);
        posCenterX_Text.setEditable(false);
        posCenterXP_Text.setEditable(false);
        posCenterX_Text.setText(null);
        posCenterXP_Text.setText(null);
        posCenterX_Text.setBackground(Color.white);
        posCenterXP_Text.setBackground(Color.white);
        posCenterX_Text.setDecimalFormat(dbl_Format);
        posCenterXP_Text.setDecimalFormat(dbl_Format);
        emScrResX_Spinner.setAlignmentX(JSpinner.CENTER_ALIGNMENT);
        emScrResY_Spinner.setAlignmentX(JSpinner.CENTER_ALIGNMENT);
        emSizeX_Spinner.setAlignmentX(JSpinner.CENTER_ALIGNMENT);
        emSizeY_Spinner.setAlignmentX(JSpinner.CENTER_ALIGNMENT);
        alphaEm_Label.setHorizontalAlignment(SwingConstants.CENTER);
        betaEm_Label.setHorizontalAlignment(SwingConstants.CENTER);
        rmsEm_Label.setHorizontalAlignment(SwingConstants.CENTER);
        alphaEm_Text.setDecimalFormat(dbl_Format);
        betaEm_Text.setDecimalFormat(dbl_Format);
        rmsEm_Text.setDecimalFormat(dbl_Format);
        alphaEm_Text.setHorizontalAlignment(JTextField.CENTER);
        betaEm_Text.setHorizontalAlignment(JTextField.CENTER);
        rmsEm_Text.setHorizontalAlignment(JTextField.CENTER);
        alphaEm_Text.setEditable(false);
        betaEm_Text.setEditable(false);
        rmsEm_Text.setEditable(false);
        alphaEm_Text.setText(null);
        betaEm_Text.setText(null);
        rmsEm_Text.setText(null);
        alphaEm_Text.setBackground(Color.white);
        betaEm_Text.setBackground(Color.white);
        rmsEm_Text.setBackground(Color.white);
        distHS_Text.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                harpSlitDist = distHS_Text.getValue();
            }
        });
        energy_Text.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                energy = energy_Text.getValue();
                double p = Math.sqrt((energy + pMass) * (energy + pMass) - pMass * pMass);
                gammaBeta = p / pMass;
            }
        });
        distWW_Text.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                wireStep = distWW_Text.getValue();
            }
        });
        gaussAmp_Text.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                gaussAmp = gaussAmp_Text.getValue();
            }
        });
        gaussWidth_Text.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                gaussWidth = gaussWidth_Text.getValue();
            }
        });
        threshold_Text.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                threshold = threshold_Text.getValue();
            }
        });
        emScrResX_Spinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                emScrResX = ((Integer) emScrResX_Spinner.getValue()).intValue();
                if (emScrResX == 0) {
                    emScrResX = 1;
                }
                emittance3D.setScreenResolution(emScrResX, emScrResY);
                emittance3Da.setScreenResolution(emScrResX, emScrResY);
                plotEmittanceData();
            }
        });
        emScrResY_Spinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                emScrResY = ((Integer) emScrResY_Spinner.getValue()).intValue();
                if (emScrResY == 0) {
                    emScrResY = 1;
                }
                emittance3D.setScreenResolution(emScrResX, emScrResY);
                emittance3Da.setScreenResolution(emScrResX, emScrResY);
                plotEmittanceData();
            }
        });
        emSizeX_Spinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                emSizeX = ((Integer) emSizeX_Spinner.getValue()).intValue();
                if (emSizeX == 0) {
                    emSizeX = 1;
                }
            }
        });
        emSizeY_Spinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                emSizeY = ((Integer) emSizeY_Spinner.getValue()).intValue();
                if (emSizeY == 0) {
                    emSizeY = 1;
                }
            }
        });
        useFilter_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (useFilter_Button.isSelected()) {
                    useGraphData_Button.setSelected(false);
                }
            }
        });
        useGraphData_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (useGraphData_Button.isSelected()) {
                    useFilter_Button.setSelected(false);
                }
            }
        });
        makeWiresSignalPlot_Button.setForeground(Color.blue.darker());
        makeWiresSignalPlot_Button.setBorder(BorderFactory.createRaisedBevelBorder());
        makeWiresSignalPlot_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                makeWiresSignalsData();
                plotWiresSignalsData();
            }
        });
        useHarpPos_Spinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int posHarp = ((Integer) useHarpPos_Spinner.getValue()).intValue();
                wireSignalData.getPlotData(posHarp - 1).setColorGenerator(colorGen_sp);
                GP_sp.clearZoomStack();
                GP_sp.setColorSurfaceData(wireSignalData.getPlotData(posHarp - 1));
            }
        });
        makeEmittPlot_Button.setForeground(Color.blue.darker());
        makeEmittPlot_Button.setBorder(BorderFactory.createRaisedBevelBorder());
        makeEmittPlot_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                makeEmittanceData();
                plotEmittanceData();
            }
        });
        restore_Button.setForeground(Color.blue.darker());
        restore_Button.setBorder(BorderFactory.createRaisedBevelBorder());
        restore_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                wireSignalData.restoreData3D();
                GP_sp.refreshGraphJPanel();
            }
        });
        setZero_Button.setForeground(Color.blue.darker());
        setZero_Button.setBorder(BorderFactory.createRaisedBevelBorder());
        setZero_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int nChan = wireSignalData.getChannelsNumber();
                int nPosSlit = wireSignalData.getPositionsNumberSlit();
                int nPosHarp = wireSignalData.getPositionsNumberHarp();
                int posHarp = ((Integer) useHarpPos_Spinner.getValue()).intValue();
                if (nPosSlit * nChan <= 1) {
                    return;
                }
                int xMin = (int) (GP_sp.getCurrentMinX() + 0.5);
                int xMax = (int) (GP_sp.getCurrentMaxX() + 0.5);
                int yMin = (int) (GP_sp.getCurrentMinY() + 0.5);
                int yMax = (int) (GP_sp.getCurrentMaxY() + 0.5);
                if (xMin >= nPosSlit) {
                    xMin = nPosSlit;
                }
                if (xMin < 0) {
                    xMin = 0;
                }
                if (xMax >= nPosSlit) {
                    xMax = nPosSlit;
                }
                if (xMax < 0) {
                    xMax = 0;
                }
                if (yMin >= nChan) {
                    yMin = nChan;
                }
                if (yMin < 0) {
                    yMin = 0;
                }
                if (yMax >= nChan) {
                    yMax = nChan;
                }
                if (yMax < 0) {
                    yMax = 0;
                }
                for (int ip = xMin; ip < xMax; ip++) {
                    for (int ic = yMin; ic < yMax; ic++) {
                        wireSignalData.setValue(ip, posHarp - 1, ic, 0.);
                    }
                }
                wireSignalData.getPlotData(posHarp - 1).calcMaxMinZ();
                GP_sp.clearZoomStack();
                GP_sp.setColorSurfaceData(wireSignalData.getPlotData(posHarp - 1));
            }
        });
        scrollBar_sp.setBlockIncrement((scrollBar_sp.getMaximum() - scrollBar_sp.getMinimum()) / 10);
        scrollBar_sp.getModel().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int i_val = scrollBar_sp.getValue();
                double val = ((double) i_val) / scrollBar_sp.getMaximum();
                colorGen_sp.setUpperLimit(val);
                int posHarp = ((Integer) useHarpPos_Spinner.getValue()).intValue();
                wireSignalData.getPlotData(posHarp - 1).setColorGenerator(colorGen_sp);
                GP_sp.refreshGraphJPanel();
            }
        });
        scrollBar_ep.setBlockIncrement((scrollBar_ep.getMaximum() - scrollBar_ep.getMinimum()) / 10);
        scrollBar_ep.getModel().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int i_val = scrollBar_ep.getValue();
                double val = ((double) i_val) / scrollBar_ep.getMaximum();
                colorGen_ep.setUpperLimit(val);
                GP_ep.refreshGraphJPanel();
            }
        });
        scrollBar_sp.setValue(100);
        scrollBar_ep.setValue(100);
        Border etchedBorder = BorderFactory.createEtchedBorder();
        border = BorderFactory.createTitledBorder(etchedBorder, "plot emittance from raw data");
        panel.setBorder(border);
        panel.setLayout(new BorderLayout());
        panel.setBackground(panel.getBackground().darker());
        Border etchedBorderBlack = BorderFactory.createEtchedBorder(panel.getBackground().darker(), panel.getBackground().brighter());
        JPanel tmp_panel_0_0 = new JPanel();
        tmp_panel_0_0.setLayout(new GridLayout(1, 3, 1, 1));
        JPanel tmp_panel_0_0_1 = new JPanel();
        tmp_panel_0_0_1.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
        tmp_panel_0_0_1.add(useHarpPos_Label);
        tmp_panel_0_0_1.add(useHarpPos_Spinner);
        tmp_panel_0_0.add(useFilter_Button);
        tmp_panel_0_0.add(useGraphData_Button);
        tmp_panel_0_0.add(tmp_panel_0_0_1);
        JPanel tmp_panel_0_1 = new JPanel();
        tmp_panel_0_1.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
        tmp_panel_0_1.add(makeWiresSignalPlot_Button);
        JPanel tmp_panel_0 = new JPanel();
        tmp_panel_0.setLayout(new BorderLayout());
        tmp_panel_0.setBorder(etchedBorderBlack);
        tmp_panel_0.add(tmp_panel_0_0, BorderLayout.NORTH);
        tmp_panel_0.add(tmp_panel_0_1, BorderLayout.CENTER);
        JPanel tmp_panel_1_0 = new JPanel();
        tmp_panel_1_0.setLayout(new GridLayout(2, 3, 1, 1));
        tmp_panel_1_0.setBorder(etchedBorderBlack);
        tmp_panel_1_0.add(distHS_Label);
        tmp_panel_1_0.add(energy_Label);
        tmp_panel_1_0.add(distWW_Label);
        tmp_panel_1_0.add(distHS_Text);
        tmp_panel_1_0.add(energy_Text);
        tmp_panel_1_0.add(distWW_Text);
        JPanel tmp_panel_1_1 = new JPanel();
        tmp_panel_1_1.setLayout(new GridLayout(2, 3, 1, 1));
        tmp_panel_1_1.setBorder(etchedBorderBlack);
        tmp_panel_1_1.add(gaussAmp_Label);
        tmp_panel_1_1.add(gaussWidth_Label);
        tmp_panel_1_1.add(threshold_Label);
        tmp_panel_1_1.add(gaussAmp_Text);
        tmp_panel_1_1.add(gaussWidth_Text);
        tmp_panel_1_1.add(threshold_Text);
        JPanel tmp_panel_1_2 = new JPanel();
        tmp_panel_1_2.setLayout(new GridLayout(2, 4, 1, 1));
        tmp_panel_1_2.setBorder(etchedBorderBlack);
        tmp_panel_1_2.add(emScrResX_Label);
        tmp_panel_1_2.add(emScrResY_Label);
        tmp_panel_1_2.add(emSizeX_Label);
        tmp_panel_1_2.add(emSizeY_Label);
        tmp_panel_1_2.add(emScrResX_Spinner);
        tmp_panel_1_2.add(emScrResY_Spinner);
        tmp_panel_1_2.add(emSizeX_Spinner);
        tmp_panel_1_2.add(emSizeY_Spinner);
        JPanel tmp_panel_1_3 = new JPanel();
        tmp_panel_1_3.setLayout(new GridLayout(3, 1, 2, 2));
        tmp_panel_1_3.setBorder(new javax.swing.border.EmptyBorder(2, 2, 2, 2));
        tmp_panel_1_3.add(tmp_panel_1_0);
        tmp_panel_1_3.add(tmp_panel_1_1);
        tmp_panel_1_3.add(tmp_panel_1_2);
        JPanel tmp_panel_1_4 = new JPanel();
        tmp_panel_1_4.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
        tmp_panel_1_4.add(makeEmittPlot_Button);
        JPanel tmp_panel_1_5 = new JPanel();
        tmp_panel_1_5.setLayout(new GridLayout(2, 3, 2, 2));
        emmParamPanelBorder = BorderFactory.createTitledBorder(etchedBorderBlack, "rms emittance parameters");
        tmp_panel_1_5.setBorder(emmParamPanelBorder);
        tmp_panel_1_5.add(alphaEm_Label);
        tmp_panel_1_5.add(betaEm_Label);
        tmp_panel_1_5.add(rmsEm_Label);
        tmp_panel_1_5.add(alphaEm_Text);
        tmp_panel_1_5.add(betaEm_Text);
        tmp_panel_1_5.add(rmsEm_Text);
        JPanel tmp_panel_1 = new JPanel();
        tmp_panel_1.setLayout(new BorderLayout());
        tmp_panel_1.setBorder(etchedBorderBlack);
        tmp_panel_1.add(tmp_panel_1_3, BorderLayout.NORTH);
        tmp_panel_1.add(tmp_panel_1_4, BorderLayout.CENTER);
        tmp_panel_1.add(tmp_panel_1_5, BorderLayout.SOUTH);
        JPanel tmp_panel_2_0 = new JPanel();
        tmp_panel_2_0.setLayout(new GridLayout(2, 3, 1, 1));
        tmp_panel_2_0.setBorder(new javax.swing.border.EmptyBorder(2, 2, 2, 2));
        tmp_panel_2_0.add(posX_Label);
        tmp_panel_2_0.add(posY_Label);
        tmp_panel_2_0.add(value_Label);
        tmp_panel_2_0.add(posX_Text);
        tmp_panel_2_0.add(posY_Text);
        tmp_panel_2_0.add(value_Text);
        JPanel tmp_panel_2_1 = new JPanel();
        tmp_panel_2_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 1));
        tmp_panel_2_1.add(setZero_Button);
        tmp_panel_2_1.add(restore_Button);
        JPanel tmp_panel_2 = new JPanel();
        tmp_panel_2.setLayout(new BorderLayout());
        tmp_panel_2.setBorder(etchedBorderBlack);
        tmp_panel_2.add(tmp_panel_2_0, BorderLayout.NORTH);
        tmp_panel_2.add(tmp_panel_2_1, BorderLayout.CENTER);
        JPanel tmp_buttons_panel = new JPanel();
        tmp_buttons_panel.setLayout(new VerticalLayout());
        tmp_buttons_panel.add(tmp_panel_0);
        tmp_buttons_panel.add(tmp_panel_2);
        tmp_buttons_panel.add(tmp_panel_1);
        JPanel tmp_graphs_panel = new JPanel();
        tmp_graphs_panel.setLayout(new GridLayout(1, 2, 5, 5));
        JPanel tmp_graph_0_panel = new JPanel();
        tmp_graph_0_panel.setLayout(new BorderLayout());
        tmp_graph_0_panel.setBorder(etchedBorderBlack);
        tmp_graph_0_panel.add(GP_sp, BorderLayout.CENTER);
        tmp_graph_0_panel.add(scrollBar_sp, BorderLayout.SOUTH);
        JPanel tmp_graph_1_upp_panel = new JPanel();
        tmp_graph_1_upp_panel.setLayout(new GridLayout(2, 2, 2, 2));
        tmp_graph_1_upp_panel.add(posCenterX_Label);
        tmp_graph_1_upp_panel.add(posCenterXP_Label);
        tmp_graph_1_upp_panel.add(posCenterX_Text);
        tmp_graph_1_upp_panel.add(posCenterXP_Text);
        JPanel tmp_graph_1_panel = new JPanel();
        tmp_graph_1_panel.setLayout(new BorderLayout());
        tmp_graph_1_panel.setBorder(etchedBorderBlack);
        tmp_graph_1_panel.add(tmp_graph_1_upp_panel, BorderLayout.NORTH);
        tmp_graph_1_panel.add(GP_ep, BorderLayout.CENTER);
        tmp_graph_1_panel.add(scrollBar_ep, BorderLayout.SOUTH);
        tmp_graphs_panel.add(tmp_graph_0_panel);
        tmp_graphs_panel.add(tmp_graph_1_panel);
        panel.add(tmp_buttons_panel, BorderLayout.WEST);
        panel.add(tmp_graphs_panel, BorderLayout.CENTER);
    }
