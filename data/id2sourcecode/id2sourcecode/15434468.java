    protected void initializeBPMPane(java.util.List nodes) {
        theNodes = nodes;
        bpmDelays = new Channel[nodes.size()];
        xWFChs = new Channel[nodes.size()];
        yWFChs = new Channel[nodes.size()];
        if (typeInd == 0) {
            String[] columnNames = { "BPM", "Delay (us)", "avg_start", "avg_stop", "chop freq.", "Turns Delay", "new Delay", "new avg_start", "new avg_stop", "new chop freq.", "new Turns Delay" };
            bpmTableModel = new DeviceTableModel(columnNames, nodes.size());
            bpmTable = new JTable(bpmTableModel);
            bpmTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            bpmNames = new String[nodes.size()];
            bpmAvgStarts = new Channel[nodes.size()];
            bpmAvgStops = new Channel[nodes.size()];
            bpmChopFreqs = new Channel[nodes.size()];
            bpmTDelays = new Channel[nodes.size()];
            InputPVTableCell pvCell1, pvCell2, pvCell3, pvCell4, pvCell5;
            ChannelFactory caF = ChannelFactory.defaultFactory();
            for (int i = 0; i < nodes.size(); i++) {
                bpmNames[i] = ((AcceleratorNode) nodes.get(i)).getId();
                bpmTableModel.addRowName(bpmNames[i], i);
                String dlyChName = bpmNames[i] + ":Delay00_Rb";
                String avgStartChName = bpmNames[i] + ":avgStart_Rb";
                String avgStopChName = bpmNames[i] + ":avgStop_Rb";
                String chopFreqChName = bpmNames[i] + ":chopFreq_Rb";
                String trnDlyChName = bpmNames[i] + ":TurnsDelay00_Rb";
                xWFChs[i] = caF.getChannel(bpmNames[i] + ":beamPA");
                yWFChs[i] = caF.getChannel(bpmNames[i] + ":beamIA");
                try {
                    bpmDelays[i] = caF.getChannel(dlyChName);
                    if (bpmDelays[i] != null) {
                        pvCell1 = new InputPVTableCell(bpmDelays[i], i, 1);
                        bpmTableModel.addPVCell(pvCell1, i, 1);
                        getChannelVec(bpmDelays[i]).add(pvCell1);
                    }
                    bpmAvgStarts[i] = caF.getChannel(avgStartChName);
                    if (bpmAvgStarts[i] != null) {
                        pvCell2 = new InputPVTableCell(bpmAvgStarts[i], i, 2);
                        bpmTableModel.addPVCell(pvCell2, i, 2);
                        getChannelVec(bpmAvgStarts[i]).add(pvCell2);
                    }
                    bpmAvgStops[i] = caF.getChannel(avgStopChName);
                    if (bpmAvgStops[i] != null) {
                        pvCell3 = new InputPVTableCell(bpmAvgStops[i], i, 3);
                        bpmTableModel.addPVCell(pvCell3, i, 3);
                        getChannelVec(bpmAvgStops[i]).add(pvCell3);
                    }
                    bpmChopFreqs[i] = caF.getChannel(chopFreqChName);
                    if (bpmChopFreqs[i] != null) {
                        pvCell4 = new InputPVTableCell(bpmChopFreqs[i], i, 4);
                        bpmTableModel.addPVCell(pvCell4, i, 4);
                        getChannelVec(bpmChopFreqs[i]).add(pvCell4);
                    }
                    bpmTDelays[i] = caF.getChannel(trnDlyChName);
                    if (bpmTDelays[i] != null) {
                        pvCell5 = new InputPVTableCell(bpmTDelays[i], i, 5);
                        bpmTableModel.addPVCell(pvCell5, i, 5);
                        getChannelVec(bpmTDelays[i]).add(pvCell5);
                    }
                } catch (NoSuchChannelException e) {
                    System.out.println(e);
                }
            }
        } else {
            String[] columnNames = { "BPM", "Dly (s)", "TrggEvt", "S1 trns", "S1 gain", "S1 mthd", "S2 trns", "S2 gain", "S2 mthd", "S3 trns", "S3 gain", "S3 mthd", "S4 trns", "S4 gain", "S4 mthd", "Freq_Mode", "direct_freq", "direct_beta", "1st Turn Dly" };
            bpmTableModel = new DeviceTableModel(columnNames, nodes.size());
            bpmTable = new JTable(bpmTableModel);
            bpmTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            bpmTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            bpmTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            bpmTable.getColumnModel().getColumn(4).setPreferredWidth(150);
            bpmTable.getColumnModel().getColumn(7).setPreferredWidth(150);
            bpmTable.getColumnModel().getColumn(10).setPreferredWidth(150);
            bpmTable.getColumnModel().getColumn(13).setPreferredWidth(150);
            bpmTable.getColumnModel().getColumn(18).setPreferredWidth(150);
            bpmNames = new String[nodes.size()];
            bpmDelayRBs = new Channel[nodes.size()];
            st1LenChs = new Channel[nodes.size()];
            st1GainChs = new Channel[nodes.size()];
            st1MthdChs = new Channel[nodes.size()];
            st2LenChs = new Channel[nodes.size()];
            st2GainChs = new Channel[nodes.size()];
            st2MthdChs = new Channel[nodes.size()];
            st3LenChs = new Channel[nodes.size()];
            st3GainChs = new Channel[nodes.size()];
            st3MthdChs = new Channel[nodes.size()];
            st4LenChs = new Channel[nodes.size()];
            st4GainChs = new Channel[nodes.size()];
            st4MthdChs = new Channel[nodes.size()];
            freqChs = new Channel[nodes.size()];
            freqRbChs = new Channel[nodes.size()];
            betaChs = new Channel[nodes.size()];
            betaRbChs = new Channel[nodes.size()];
            opModeChs = new Channel[nodes.size()];
            opModeRbChs = new Channel[nodes.size()];
            trnDlyChs = new Channel[nodes.size()];
            trnDlyRbChs = new Channel[nodes.size()];
            trigEvtChs = new Channel[nodes.size()];
            trigEvtRbChs = new Channel[nodes.size()];
            st1LenSetChs = new Channel[nodes.size()];
            st1GainSetChs = new Channel[nodes.size()];
            st1MthdSetChs = new Channel[nodes.size()];
            st1MthdLenChs = new Channel[nodes.size()];
            st2LenSetChs = new Channel[nodes.size()];
            st2GainSetChs = new Channel[nodes.size()];
            st2MthdSetChs = new Channel[nodes.size()];
            st2MthdLenChs = new Channel[nodes.size()];
            st3LenSetChs = new Channel[nodes.size()];
            st3GainSetChs = new Channel[nodes.size()];
            st3MthdSetChs = new Channel[nodes.size()];
            st3MthdLenChs = new Channel[nodes.size()];
            st4LenSetChs = new Channel[nodes.size()];
            st4GainSetChs = new Channel[nodes.size()];
            st4MthdSetChs = new Channel[nodes.size()];
            st4MthdLenChs = new Channel[nodes.size()];
            InputPVTableCell tDelayCell;
            ComboBoxPVCell trigEvtCell;
            InputPVTableCell s1LenCell, s2LenCell, s3LenCell, s4LenCell;
            InputPVTableCell s1GainCell, s2GainCell, s3GainCell, s4GainCell;
            InputPVTableCell s1MthdCell, s2MthdCell, s3MthdCell, s4MthdCell;
            InputPVTableCell freqCell, betaCell, opModeCell, trnDlyCell;
            ChannelFactory caF = ChannelFactory.defaultFactory();
            for (int i = 0; i < nodes.size(); i++) {
                RingBPM theNode = (RingBPM) nodes.get(i);
                bpmNames[i] = ((AcceleratorNode) nodes.get(i)).getId();
                bpmTableModel.addRowName(bpmNames[i], i);
                String dlyChName = theNode.getId() + ":TriggerDelay";
                String dlyRbChName = theNode.getId() + ":TriggerDelay_RB";
                String trigEvtChName = theNode.getId() + ":TriggerEvent";
                String trigEvtRbChName = theNode.getId() + ":TriggerEvent_RB";
                String opModeChName = theNode.getId() + ":OperMode";
                String opModeRbChName = theNode.getId() + ":OperMode_RB";
                String freqChName = theNode.getId() + ":Direct_RingFreq";
                String freqRbChName = theNode.getId() + ":Direct_RingFreq_RB";
                String betaChName = theNode.getId() + ":Direct_Beta";
                String betaRbChName = theNode.getId() + ":Direct_Beta_RB";
                String trnDlyChName = theNode.getId() + ":FirstTurnDelay";
                String trnDlyRbChName = theNode.getId() + ":FirstTurnDelay_RB";
                xWFChs[i] = caF.getChannel(bpmNames[i] + ":xTBT");
                yWFChs[i] = caF.getChannel(bpmNames[i] + ":yTBT");
                try {
                    bpmDelays[i] = caF.getChannel(dlyChName);
                    bpmDelayRBs[i] = caF.getChannel(dlyRbChName);
                    if (bpmDelayRBs[i] != null) {
                        tDelayCell = new InputPVTableCell(bpmDelayRBs[i], i, 1);
                        bpmTableModel.addPVCell(tDelayCell, i, 1);
                        getChannelVec(bpmDelayRBs[i]).add(tDelayCell);
                    }
                    trigEvtChs[i] = caF.getChannel(trigEvtChName);
                    trigEvtRbChs[i] = caF.getChannel(trigEvtRbChName);
                    if (trigEvtRbChs[i] != null) {
                        trigEvtCell = new ComboBoxPVCell(trigEvtRbChs[i], i, 2, this);
                        bpmTableModel.addPVCell(trigEvtCell, i, 2);
                        getChannelVec(trigEvtRbChs[i]).add(trigEvtCell);
                    }
                    st1LenChs[i] = theNode.getChannel(RingBPM.STAGE1_LEN_RB_HANDLE);
                    st1LenSetChs[i] = theNode.getChannel(RingBPM.STAGE1_LEN_HANDLE);
                    st1MthdLenChs[i] = caF.getChannel(theNode.getId() + ":Analysis_Turns1");
                    s1LenCell = new InputPVTableCell(st1LenChs[i], i, 3);
                    bpmTableModel.addPVCell(s1LenCell, i, 3);
                    getChannelVec(st1LenChs[i]).add(s1LenCell);
                    st1GainChs[i] = theNode.getChannel(RingBPM.STAGE1_GAIN_RB_HANDLE);
                    st1GainSetChs[i] = theNode.getChannel(RingBPM.STAGE1_GAIN_HANDLE);
                    if (st1GainChs[i] != null) {
                        s1GainCell = new ComboBoxPVCell(st1GainChs[i], i, 4, this);
                        bpmTableModel.addPVCell(s1GainCell, i, 4);
                        getChannelVec(st1GainChs[i]).add(s1GainCell);
                    }
                    st1MthdChs[i] = theNode.getChannel(RingBPM.STAGE1_METHOD_RB_HANDLE);
                    st1MthdSetChs[i] = theNode.getChannel(RingBPM.STAGE1_METHOD_HANDLE);
                    if (st1MthdChs[i] != null) {
                        s1MthdCell = new ComboBoxPVCell(st1MthdChs[i], i, 5, this);
                        bpmTableModel.addPVCell(s1MthdCell, i, 5);
                        getChannelVec(st1MthdChs[i]).add(s1MthdCell);
                    }
                    st2LenChs[i] = theNode.getChannel(RingBPM.STAGE2_LEN_RB_HANDLE);
                    st2LenSetChs[i] = theNode.getChannel(RingBPM.STAGE2_LEN_HANDLE);
                    st2MthdLenChs[i] = caF.getChannel(theNode.getId() + ":Analysis_Turns2");
                    s2LenCell = new InputPVTableCell(st2LenChs[i], i, 6);
                    bpmTableModel.addPVCell(s2LenCell, i, 6);
                    getChannelVec(st2LenChs[i]).add(s2LenCell);
                    st2GainChs[i] = theNode.getChannel(RingBPM.STAGE2_GAIN_RB_HANDLE);
                    st2GainSetChs[i] = theNode.getChannel(RingBPM.STAGE2_GAIN_HANDLE);
                    if (st2GainChs[i] != null) {
                        s2GainCell = new ComboBoxPVCell(st2GainChs[i], i, 7, this);
                        bpmTableModel.addPVCell(s2GainCell, i, 7);
                        getChannelVec(st2GainChs[i]).add(s2GainCell);
                    }
                    st2MthdChs[i] = theNode.getChannel(RingBPM.STAGE2_METHOD_RB_HANDLE);
                    st2MthdSetChs[i] = theNode.getChannel(RingBPM.STAGE2_METHOD_HANDLE);
                    if (st2MthdChs[i] != null) {
                        s2MthdCell = new ComboBoxPVCell(st2MthdChs[i], i, 8, this);
                        bpmTableModel.addPVCell(s2MthdCell, i, 8);
                        getChannelVec(st2MthdChs[i]).add(s2MthdCell);
                    }
                    st3LenChs[i] = theNode.getChannel(RingBPM.STAGE3_LEN_RB_HANDLE);
                    st3LenSetChs[i] = theNode.getChannel(RingBPM.STAGE3_LEN_HANDLE);
                    st3MthdLenChs[i] = caF.getChannel(theNode.getId() + ":Analysis_Turns3");
                    s3LenCell = new InputPVTableCell(st3LenChs[i], i, 9);
                    bpmTableModel.addPVCell(s3LenCell, i, 9);
                    getChannelVec(st3LenChs[i]).add(s3LenCell);
                    st3GainChs[i] = theNode.getChannel(RingBPM.STAGE3_GAIN_RB_HANDLE);
                    st3GainSetChs[i] = theNode.getChannel(RingBPM.STAGE3_GAIN_HANDLE);
                    if (st3GainChs[i] != null) {
                        s3GainCell = new ComboBoxPVCell(st3GainChs[i], i, 10, this);
                        bpmTableModel.addPVCell(s3GainCell, i, 10);
                        getChannelVec(st3GainChs[i]).add(s3GainCell);
                    }
                    st3MthdChs[i] = theNode.getChannel(RingBPM.STAGE3_METHOD_RB_HANDLE);
                    st3MthdSetChs[i] = theNode.getChannel(RingBPM.STAGE3_METHOD_HANDLE);
                    if (st3MthdChs[i] != null) {
                        s3MthdCell = new ComboBoxPVCell(st3MthdChs[i], i, 11, this);
                        bpmTableModel.addPVCell(s3MthdCell, i, 11);
                        getChannelVec(st3MthdChs[i]).add(s3MthdCell);
                    }
                    st4LenChs[i] = theNode.getChannel(RingBPM.STAGE4_LEN_RB_HANDLE);
                    st4LenSetChs[i] = theNode.getChannel(RingBPM.STAGE4_LEN_HANDLE);
                    st4MthdLenChs[i] = caF.getChannel(theNode.getId() + ":Analysis_Turns4");
                    s4LenCell = new InputPVTableCell(st4LenChs[i], i, 12);
                    bpmTableModel.addPVCell(s4LenCell, i, 12);
                    getChannelVec(st4LenChs[i]).add(s4LenCell);
                    st4GainChs[i] = theNode.getChannel(RingBPM.STAGE4_GAIN_RB_HANDLE);
                    st4GainSetChs[i] = theNode.getChannel(RingBPM.STAGE4_GAIN_HANDLE);
                    if (st4GainChs[i] != null) {
                        s4GainCell = new ComboBoxPVCell(st4GainChs[i], i, 13, this);
                        bpmTableModel.addPVCell(s4GainCell, i, 13);
                        getChannelVec(st4GainChs[i]).add(s4GainCell);
                    }
                    st4MthdChs[i] = theNode.getChannel(RingBPM.STAGE4_METHOD_RB_HANDLE);
                    st4MthdSetChs[i] = theNode.getChannel(RingBPM.STAGE4_METHOD_HANDLE);
                    if (st4MthdChs[i] != null) {
                        s4MthdCell = new ComboBoxPVCell(st4MthdChs[i], i, 14, this);
                        bpmTableModel.addPVCell(s4MthdCell, i, 14);
                        getChannelVec(st4MthdChs[i]).add(s4MthdCell);
                    }
                    opModeChs[i] = caF.getChannel(opModeChName);
                    opModeRbChs[i] = caF.getChannel(opModeRbChName);
                    if (opModeRbChs[i] != null) {
                        opModeCell = new ComboBoxPVCell(st4GainChs[i], i, 15, this);
                        bpmTableModel.addPVCell(opModeCell, i, 15);
                        getChannelVec(opModeRbChs[i]).add(opModeCell);
                    }
                    freqChs[i] = caF.getChannel(freqChName);
                    freqRbChs[i] = caF.getChannel(freqRbChName);
                    if (freqRbChs[i] != null) {
                        freqCell = new InputPVTableCell(freqRbChs[i], i, 16);
                        bpmTableModel.addPVCell(freqCell, i, 16);
                        getChannelVec(freqRbChs[i]).add(freqCell);
                    }
                    betaChs[i] = caF.getChannel(betaChName);
                    betaRbChs[i] = caF.getChannel(betaRbChName);
                    if (betaRbChs[i] != null) {
                        betaCell = new InputPVTableCell(betaRbChs[i], i, 17);
                        bpmTableModel.addPVCell(betaCell, i, 17);
                        getChannelVec(betaRbChs[i]).add(betaCell);
                    }
                    trnDlyChs[i] = caF.getChannel(trnDlyChName);
                    trnDlyRbChs[i] = caF.getChannel(trnDlyRbChName);
                    if (trnDlyRbChs[i] != null) {
                        trnDlyCell = new InputPVTableCell(trnDlyRbChs[i], i, 18);
                        bpmTableModel.addPVCell(trnDlyCell, i, 18);
                        getChannelVec(trnDlyRbChs[i]).add(trnDlyCell);
                    }
                } catch (NoSuchChannelException e) {
                    System.out.println(e);
                }
            }
            setUpTrigEvtColumn(bpmTable.getColumnModel().getColumn(2));
            setUpGainColumn(bpmTable.getColumnModel().getColumn(4));
            setUpGainColumn(bpmTable.getColumnModel().getColumn(7));
            setUpGainColumn(bpmTable.getColumnModel().getColumn(10));
            setUpGainColumn(bpmTable.getColumnModel().getColumn(13));
            setUpAnalysisColumn(bpmTable.getColumnModel().getColumn(5));
            setUpAnalysisColumn(bpmTable.getColumnModel().getColumn(8));
            setUpAnalysisColumn(bpmTable.getColumnModel().getColumn(11));
            setUpAnalysisColumn(bpmTable.getColumnModel().getColumn(14));
            setUpOperModeColumn(bpmTable.getColumnModel().getColumn(15));
        }
        bpmTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        ListSelectionModel rowSM = bpmTable.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                } else {
                    selectedRow = lsm.getMinSelectionIndex();
                    updatePlot(bpmNames[selectedRow]);
                    if (typeInd == 0) {
                        bpmTableModel.setValueAt(bpmTableModel.getValueAt(selectedRow, 1).toString(), selectedRow, 6);
                        bpmTableModel.setValueAt(bpmTableModel.getValueAt(selectedRow, 2).toString(), selectedRow, 7);
                        bpmTableModel.setValueAt(bpmTableModel.getValueAt(selectedRow, 3).toString(), selectedRow, 8);
                        bpmTableModel.setValueAt(bpmTableModel.getValueAt(selectedRow, 4).toString(), selectedRow, 9);
                        bpmTableModel.setValueAt(bpmTableModel.getValueAt(selectedRow, 5).toString(), selectedRow, 10);
                    }
                }
            }
        });
        final TableProdder prodder = new TableProdder(bpmTableModel);
        prodder.start();
        JScrollPane bpmScrollPane = new JScrollPane(bpmTable);
        bpmScrollPane.setPreferredSize(new Dimension(850, 400));
        EdgeLayout edgeLayout = new EdgeLayout();
        setLayout(edgeLayout);
        edgeLayout.setConstraints(bpmScrollPane, 50, 30, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        add(bpmScrollPane);
        JPanel plotPane = new JPanel();
        plotPane.setLayout(new BoxLayout(plotPane, BoxLayout.Y_AXIS));
        plotPane.setPreferredSize(new Dimension(350, 470));
        if (typeInd == 0) {
            phasePlot = new FunctionGraphsJPanel();
            phasePlot.setLimitsAndTicksX(0., 50., 10.);
            phasePlot.addCurveData(phasePlotData);
            phasePlot.setName("BPM phase: ");
            phasePlot.setAxisNames("point no.", "phase");
            phasePlot.addMouseListener(new SimpleChartPopupMenu(phasePlot));
            plotPane.add(phasePlot);
            ampPlot = new FunctionGraphsJPanel();
            ampPlot.setLimitsAndTicksX(0., 50., 500.);
            ampPlot.addCurveData(ampPlotData);
            ampPlot.setName("BPM Amplitude: ");
            ampPlot.setAxisNames("point no.", "amplitude");
            ampPlot.addMouseListener(new SimpleChartPopupMenu(ampPlot));
            plotPane.add(ampPlot);
            ampPlot.setVerLinesButtonVisible(true);
            ampPlot.setDraggedVerLinesMotionListen(true);
            ampPlot.addVerticalLine(low, Color.green);
            ampPlot.addVerticalLine(high, Color.red);
        } else {
            xTBTPlot = new FunctionGraphsJPanel();
            xTBTPlot.setLimitsAndTicksX(0., 500., 100.);
            xTBTPlot.addCurveData(xTBTPlotData);
            xTBTPlot.setName("xTBT: ");
            xTBTPlot.setAxisNames("turn no.", "x (mm)");
            xTBTPlot.addMouseListener(new SimpleChartPopupMenu(xTBTPlot));
            plotPane.add(xTBTPlot);
            yTBTPlot = new FunctionGraphsJPanel();
            yTBTPlot.setLimitsAndTicksX(0., 500., 500.);
            yTBTPlot.addCurveData(yTBTPlotData);
            yTBTPlot.setName("yTBT: ");
            yTBTPlot.setAxisNames("turn no.", "y (mm)");
            yTBTPlot.addMouseListener(new SimpleChartPopupMenu(yTBTPlot));
            plotPane.add(yTBTPlot);
            xTBTPlot.setVerLinesButtonVisible(true);
            xTBTPlot.setDraggedVerLinesMotionListen(true);
            xTBTPlot.addVerticalLine(low, Color.green);
            xTBTPlot.addVerticalLine(high, Color.red);
        }
        edgeLayout.setConstraints(plotPane, 20, 900, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        add(plotPane);
        JPanel rangePane = new JPanel();
        rangePane.setLayout(new GridLayout(2, 2));
        rangePane.setPreferredSize(new Dimension(200, 40));
        JLabel startLabel = new JLabel("start: ");
        JLabel endLabel = new JLabel("end: ");
        startField = new DecimalField();
        endField = new DecimalField();
        rangePane.add(startLabel);
        rangePane.add(startField);
        rangePane.add(endLabel);
        rangePane.add(endField);
        edgeLayout.setConstraints(rangePane, 500, 950, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        add(rangePane);
        endField.setValue(high);
        startField.setValue(low);
        if (typeInd == 0) {
            ampPlot.addDraggedVerLinesListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    FunctionGraphsJPanel fgjp = (FunctionGraphsJPanel) evt.getSource();
                    int lineInd = fgjp.getDraggedLineIndex();
                    double pos = fgjp.getVerticalValue(lineInd);
                    double temp = Double.parseDouble(((InputPVTableCell) bpmTableModel.getValueAt(selectedRow, 1)).toString());
                    int[] del = new int[5];
                    if (lineInd == 1) {
                        high = pos;
                        if (high < low) endField.setForeground(Color.red); else {
                            endField.setForeground(Color.black);
                            double startPt = low + temp;
                            double range = high - low;
                            for (int i = 0; i < bpmNames.length; i++) {
                                if (bpmNames[i].substring(0, 1).equals("M")) bpmTableModel.setValueAt(nf.format(Math.round(startPt + del[0])), i, 5); else if (bpmNames[i].substring(0, 1).equals("D")) bpmTableModel.setValueAt(nf.format(Math.round(startPt + del[1])), i, 5); else if (bpmNames[i].substring(0, 1).equals("C")) bpmTableModel.setValueAt(nf.format(Math.round(startPt + del[2])), i, 5); else if (bpmNames[i].substring(0, 1).equals("S")) bpmTableModel.setValueAt(nf.format(Math.round(startPt + del[3])), i, 5); else bpmTableModel.setValueAt(nf.format(Math.round(startPt + del[4])), i, 5);
                                bpmTableModel.setValueAt(nf.format(Math.round(range)), i, 6);
                                bpmTableModel.setValueAt(bpmTableModel.getValueAt(i, 3).toString(), i, 7);
                                bpmTableModel.setValueAt(bpmTableModel.getValueAt(i, 4).toString(), i, 8);
                            }
                        }
                        endField.setValue(Math.round(high));
                        setAll.setEnabled(true);
                    } else {
                        low = pos;
                        if (high < low) startField.setForeground(Color.red); else {
                            startField.setForeground(Color.black);
                            double startPt = low + temp;
                            double range = high - low;
                            for (int i = 0; i < bpmNames.length; i++) {
                                if (bpmNames[i].substring(0, 1).equals("M")) {
                                    bpmTableModel.setValueAt(nf.format(Math.round(startPt + del[0])), i, 5);
                                } else if (bpmNames[i].substring(0, 1).equals("D")) bpmTableModel.setValueAt(nf.format(Math.round(startPt + del[1])), i, 5); else if (bpmNames[i].substring(0, 1).equals("C")) bpmTableModel.setValueAt(nf.format(Math.round(startPt + del[2])), i, 5); else if (bpmNames[i].substring(0, 1).equals("S")) bpmTableModel.setValueAt(nf.format(Math.round(startPt + del[3])), i, 5); else bpmTableModel.setValueAt(nf.format(Math.round(startPt + del[4])), i, 5);
                                bpmTableModel.setValueAt(nf.format(Math.round(range)), i, 6);
                                bpmTableModel.setValueAt(bpmTableModel.getValueAt(i, 3).toString(), i, 7);
                                bpmTableModel.setValueAt(bpmTableModel.getValueAt(i, 4).toString(), i, 8);
                            }
                        }
                        startField.setValue(Math.round(low));
                        setAll.setEnabled(true);
                    }
                }
            });
            findOne = new JButton("Find settings for the selected BPM");
            findOne.setEnabled(false);
            findOne.setActionCommand("findOne");
            findOne.addActionListener(this);
            edgeLayout.setConstraints(findOne, 0, 380, 100, 0, EdgeLayout.BOTTOM, EdgeLayout.NO_GROWTH);
            add(findOne);
            setOne = new JButton("Set the selected rows/cols.");
            setOne.setActionCommand("setOne");
            setOne.addActionListener(this);
            edgeLayout.setConstraints(setOne, 0, 380, 70, 0, EdgeLayout.BOTTOM, EdgeLayout.NO_GROWTH);
            add(setOne);
            findAll = new JButton("Find all");
            findAll.setEnabled(false);
            findAll.setActionCommand("findAll");
            findAll.addActionListener(this);
            edgeLayout.setConstraints(findAll, 0, 380, 20, 0, EdgeLayout.BOTTOM, EdgeLayout.NO_GROWTH);
            add(findAll);
            prepAll1 = new JButton("Prepare for all");
            prepAll1.setActionCommand("prepAll1");
            prepAll1.addActionListener(this);
            edgeLayout.setConstraints(prepAll1, 0, 680, 100, 0, EdgeLayout.BOTTOM, EdgeLayout.NO_GROWTH);
            add(prepAll1);
            setAll = new JButton("Set all");
            setAll.setActionCommand("setAll");
            setAll.addActionListener(this);
            edgeLayout.setConstraints(setAll, 0, 680, 20, 0, EdgeLayout.BOTTOM, EdgeLayout.NO_GROWTH);
            add(setAll);
            setAll.setEnabled(false);
        } else {
            xTBTPlot.addDraggedVerLinesListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    FunctionGraphsJPanel fgjp = (FunctionGraphsJPanel) evt.getSource();
                    int lineInd = fgjp.getDraggedLineIndex();
                    double pos = fgjp.getVerticalValue(lineInd);
                    double temp = Double.parseDouble(((InputPVTableCell) bpmTableModel.getValueAt(selectedRow, 1)).toString());
                    if (lineInd == 1) {
                        high = pos;
                        if (high < low) endField.setForeground(Color.red); else {
                            endField.setForeground(Color.black);
                            bpmTableModel.setValueAt(nf.format(low + temp), selectedRow, 2);
                        }
                        endField.setValue(high);
                    } else {
                        low = pos;
                        if (high < low) startField.setForeground(Color.red); else {
                            startField.setForeground(Color.black);
                            bpmTableModel.setValueAt(nf.format(low + temp), selectedRow, 2);
                        }
                        startField.setValue(low);
                    }
                }
            });
            set1RBPM = new JButton("Set the selected rows/cols.");
            set1RBPM.setActionCommand("set1RBPM");
            set1RBPM.addActionListener(this);
            edgeLayout.setConstraints(set1RBPM, 0, 380, 100, 0, EdgeLayout.BOTTOM, EdgeLayout.NO_GROWTH);
            add(set1RBPM);
            findAllRTiming = new JButton("Set all BPM timing");
            findAllRTiming.setActionCommand("findAllRTiming");
            findAllRTiming.addActionListener(this);
            edgeLayout.setConstraints(findAllRTiming, 0, 380, 70, 0, EdgeLayout.BOTTOM, EdgeLayout.NO_GROWTH);
            add(findAllRTiming);
            prepAll2 = new JButton("Prepare for all");
            prepAll2.setActionCommand("prepAll2");
            prepAll2.addActionListener(this);
            edgeLayout.setConstraints(prepAll2, 0, 680, 100, 0, EdgeLayout.BOTTOM, EdgeLayout.NO_GROWTH);
            add(prepAll2);
            setAllRBPMs = new JButton("Set all BPM stages");
            setAllRBPMs.setActionCommand("setAllRBPMs");
            setAllRBPMs.addActionListener(this);
            edgeLayout.setConstraints(setAllRBPMs, 0, 680, 70, 0, EdgeLayout.BOTTOM, EdgeLayout.NO_GROWTH);
            add(setAllRBPMs);
        }
        JLabel beamWidthLbl = new JLabel("Beam gate width (turns): ");
        edgeLayout.setConstraints(beamWidthLbl, 0, 50, 100, 0, EdgeLayout.BOTTOM, EdgeLayout.NO_GROWTH);
        add(beamWidthLbl);
        beamWidthFld = new DecimalField(beamWidth, 6);
        edgeLayout.setConstraints(beamWidthFld, 0, 220, 95, 0, EdgeLayout.BOTTOM, EdgeLayout.NO_GROWTH);
        add(beamWidthFld);
        ChannelFactory cf = ChannelFactory.defaultFactory();
        Channel beamGW = cf.getChannel("ICS_Tim:Gate_BeamRef:GateWidth");
        try {
            beamWidthMonitor = beamGW.addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    beamWidth = newRecord.doubleValue();
                    beamWidthFld.setValue(beamWidth);
                }
            }, Monitor.VALUE);
        } catch (ConnectionException e) {
            System.out.println("Cannot connect to " + beamGW.getId());
        } catch (MonitorException e) {
            System.out.println("Cannot monitor " + beamGW.getId());
        }
        String[] modes = { "select rows" };
        setMode = new JComboBox(modes);
        edgeLayout.setConstraints(setMode, 0, 50, 50, 0, EdgeLayout.BOTTOM, EdgeLayout.NO_GROWTH);
        add(setMode);
        setMode.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (((String) (((JComboBox) evt.getSource()).getSelectedItem())).equals("select columns")) {
                    bpmTable.setColumnSelectionAllowed(true);
                    bpmTable.setRowSelectionAllowed(false);
                } else if (((String) (((JComboBox) evt.getSource()).getSelectedItem())).equals("select rows")) {
                    bpmTable.setColumnSelectionAllowed(false);
                    bpmTable.setRowSelectionAllowed(true);
                }
            }
        });
    }
