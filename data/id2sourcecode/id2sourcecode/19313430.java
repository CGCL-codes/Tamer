    private void defineGlobalButtons() {
        globalButtonsPanel.setLayout(new GridLayout(3, 5, 1, 1));
        globalButtonsPanel.add(hideGlobalButton);
        globalButtonsPanel.add(showAllGlobalButton);
        globalButtonsPanel.add(removePointGlobalButton);
        globalButtonsPanel.add(removeGraphGlobalButton);
        globalButtonsPanel.add(removeAllGlobalButton);
        globalButtonsPanel.add(wrapGraphGlobalButton);
        globalButtonsPanel.add(exportGraphGlobalButton);
        globalButtonsPanel.add(incrColorGlobalButton);
        globalButtonsPanel.add(dataColorGlobalButton);
        globalButtonsPanel.add(removeTmpGlobalButton);
        globalButtonsPanel.add(phaseShiftGlobalButton);
        globalButtonsPanel.add(removePhaseShiftGlobalButton);
        globalButtonsPanel.add(new JPanel());
        globalButtonsPanel.add(new JPanel());
        globalButtonsPanel.add(new JPanel());
        hideGlobalButton.setToolTipText("Hides a selected graph");
        showAllGlobalButton.setToolTipText("Shows all previously hidden graphs");
        removePointGlobalButton.setToolTipText("Removes a selected point");
        removeGraphGlobalButton.setToolTipText("Removes a selected graph");
        removeAllGlobalButton.setToolTipText("Removes all visible graphs");
        wrapGraphGlobalButton.setToolTipText("Wrap phase scan data using -180 +180 interval");
        exportGraphGlobalButton.setToolTipText("Writes data of a selected graph into file");
        incrColorGlobalButton.setToolTipText("Applay incremental coloring to visible graphs");
        dataColorGlobalButton.setToolTipText("Apply data color to the graph (colors by default)");
        removeTmpGlobalButton.setToolTipText("Remove a temporary graph created during analysis");
        phaseShiftGlobalButton.setToolTipText("Shift x-coordinates (phase) by +25 degrees");
        removePhaseShiftGlobalButton.setToolTipText("Un-shift x-coordinates (phase)");
        hideGlobalButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BasicGraphData gd = getChoosenDraphData();
                if (gd != null) {
                    graphAnalysis.removeGraphData(gd);
                    messageTextLocal.setText(null);
                } else {
                    messageTextLocal.setText(null);
                    messageTextLocal.setText("Please choose graph first. Use S-button on the graph panel.");
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        showAllGlobalButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateDataSetOnGraphPanel();
                messageTextLocal.setText(null);
            }
        });
        removePointGlobalButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object[] choosenObjArr = getChoosenDraphDataAndPoint();
                BasicGraphData gd = (BasicGraphData) choosenObjArr[0];
                Integer IndP = (Integer) choosenObjArr[1];
                if (gd != null && IndP != null) {
                    gd.removePoint(IndP.intValue());
                    graphAnalysis.refreshGraphJPanel();
                } else {
                    messageTextLocal.setText(null);
                    messageTextLocal.setText("Please choose graph and point first. Use S-button on the graph panel.");
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        removeGraphGlobalButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BasicGraphData gd = getChoosenDraphData();
                Integer Ind = graphAnalysis.getGraphChosenIndex();
                if (gd != null) {
                    if (gd != graphDataLocal) {
                        graphAnalysis.removeGraphData(gd);
                        for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
                            MeasuredValue mv_tmp = measuredValuesV.get(i);
                            mv_tmp.removeDataContainer(gd);
                        }
                    } else {
                        graphDataLocal.removeAllPoints();
                    }
                    graphAnalysis.refreshGraphJPanel();
                    messageTextLocal.setText(null);
                } else {
                    messageTextLocal.setText(null);
                    messageTextLocal.setText("Please choose graph and point first. Use S-button on the graph panel.");
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        removeAllGlobalButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
                    if (measuredValuesShowStateV.get(i).booleanValue()) {
                        MeasuredValue mv_tmp = measuredValuesV.get(i);
                        if (scanPV_ShowState || scanVariable.getChannel() == null) {
                            mv_tmp.removeAllDataContainersNonRB();
                        }
                        if (scanPV_RB_ShowState) {
                            mv_tmp.removeAllDataContainersRB();
                        }
                    }
                }
                updateDataSetOnGraphPanel();
                messageTextLocal.setText(null);
            }
        });
        wrapGraphGlobalButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BasicGraphData gd = getChoosenDraphData();
                if (gd != null) {
                    GraphDataOperations.unwrapData(gd);
                    graphAnalysis.refreshGraphJPanel();
                    messageTextLocal.setText(null);
                } else {
                    messageTextLocal.setText(null);
                    messageTextLocal.setText("Please choose graph first. Use S-button on the graph panel.");
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        exportGraphGlobalButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BasicGraphData gd = getChoosenDraphData();
                if (gd != null) {
                    JFileChooser ch = new JFileChooser();
                    ch.setDialogTitle("Export to ASCII");
                    if (dataFile != null) {
                        ch.setSelectedFile(dataFile);
                    }
                    int returnVal = ch.showSaveDialog(parentAnalysisPanel);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        try {
                            dataFile = ch.getSelectedFile();
                            BufferedWriter out = new BufferedWriter(new FileWriter(dataFile));
                            int nP = gd.getNumbOfPoints();
                            for (int i = 0; i < nP; i++) {
                                out.write(" " + gd.getX(i) + " " + gd.getY(i) + " " + gd.getErr(i));
                                out.newLine();
                            }
                            out.flush();
                            out.close();
                        } catch (IOException exp) {
                            Toolkit.getDefaultToolkit().beep();
                            System.out.println(exp.toString());
                        }
                    }
                    messageTextLocal.setText(null);
                } else {
                    if (canSaveAsTable()) {
                        JFileChooser ch = new JFileChooser();
                        ch.setDialogTitle("Export to ASCII as a Table");
                        if (dataFile != null) {
                            ch.setSelectedFile(dataFile);
                        }
                        int returnVal = ch.showSaveDialog(parentAnalysisPanel);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            try {
                                dataFile = ch.getSelectedFile();
                                BufferedWriter out = new BufferedWriter(new FileWriter(dataFile));
                                Vector<BasicGraphData> gdV_tmp = graphAnalysis.getAllGraphData();
                                Vector<BasicGraphData> gdV = new Vector<BasicGraphData>();
                                for (int i = 0; i < gdV_tmp.size(); i++) {
                                    gd = gdV_tmp.get(i);
                                    if (gd.getNumbOfPoints() > 0) {
                                        gdV.add(gd);
                                    }
                                }
                                gd = gdV.get(0);
                                int nP = gd.getNumbOfPoints();
                                for (int i = 0; i < gdV.size(); i++) {
                                    gd = gdV.get(i);
                                    out.write("% data # " + i + "  Legend = " + gd.getGraphProperty(graphAnalysis.getLegendKeyString()));
                                    out.newLine();
                                }
                                out.write("% x/data # ");
                                for (int i = 0; i < gdV.size(); i++) {
                                    out.write("       " + i + "    ");
                                }
                                out.newLine();
                                for (int j = 0; j < nP; j++) {
                                    gd = gdV.get(0);
                                    out.write(" " + xyPanel_Main_Format.format(gd.getX(j)));
                                    for (int i = 0; i < gdV.size(); i++) {
                                        gd = gdV.get(i);
                                        out.write(" " + xyPanel_Main_Format.format(gd.getY(j)));
                                    }
                                    out.write(" ");
                                    out.newLine();
                                }
                                out.flush();
                                out.close();
                            } catch (IOException exp) {
                                Toolkit.getDefaultToolkit().beep();
                                System.out.println(exp.toString());
                            }
                        }
                        messageTextLocal.setText(null);
                    } else {
                        messageTextLocal.setText(null);
                        messageTextLocal.setText("Please choose graph first. Use S-button on the graph panel.");
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
            }
        });
        incrColorGlobalButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Vector<BasicGraphData> gdV = graphAnalysis.getAllGraphData();
                int count = 0;
                for (int i = 0, n = gdV.size(); i < n; i++) {
                    BasicGraphData gd = gdV.get(i);
                    if (gd != graphDataLocal) {
                        gd.setGraphColor(IncrementalColor.getColor(count));
                        count++;
                    }
                }
                graphAnalysis.refreshGraphJPanel();
                messageTextLocal.setText(null);
            }
        });
        dataColorGlobalButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                restoreDataColoring();
                messageTextLocal.setText(null);
            }
        });
        removeTmpGlobalButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graphDataLocal.removeAllPoints();
                graphAnalysis.refreshGraphJPanel();
                messageTextLocal.setText(null);
            }
        });
        phaseShiftGlobalButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Vector<BasicGraphData> gdV = graphAnalysis.getAllGraphData();
                for (int i = 0, n = gdV.size(); i < n; i++) {
                    BasicGraphData gd = gdV.get(i);
                    if (gd != graphDataLocal) {
                        shiftGraphData(gd, 25.);
                    }
                }
                graphAnalysis.refreshGraphJPanel();
                messageTextLocal.setText(null);
            }
        });
        removePhaseShiftGlobalButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Vector<BasicGraphData> gdV = graphAnalysis.getAllGraphData();
                for (int i = 0, n = gdV.size(); i < n; i++) {
                    BasicGraphData gd = gdV.get(i);
                    if (gd != graphDataLocal) {
                        unShiftGraphData(gd);
                    }
                }
                graphAnalysis.refreshGraphJPanel();
                messageTextLocal.setText(null);
            }
        });
    }
