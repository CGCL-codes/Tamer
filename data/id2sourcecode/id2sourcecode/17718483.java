    public JScrollPane createTable(List theNodes, String[] inputPVs) {
        AcceleratorNode tempNode = (AcceleratorNode) (theNodes.get(0));
        final String typeID = new String(tempNode.getType());
        JInternalFrame theFrame = new JInternalFrame(typeID + "s", true, true, true);
        int nCols = 2;
        int nRows = theNodes.size() + 1;
        InputPVTableCell pvCell;
        XioTableCellEditor cellEditor = new XioTableCellEditor(nRows);
        if (inputPVs != null) nCols += inputPVs.length;
        String[] colNames = new String[nCols];
        colNames[0] = typeID + "s";
        colNames[1] = "s";
        int cnt = 2;
        if (inputPVs != null) for (int i = 0; i < inputPVs.length; i++, cnt++) {
            colNames[cnt] = inputPVs[i];
        }
        IODiagTableModel theTableModel = new IODiagTableModel(colNames, nRows);
        theDoc.tableModelMap.put(typeID, theTableModel);
        JTable theTable = new JTable(theTableModel);
        theTable.setRowSelectionAllowed(false);
        theTable.setColumnSelectionAllowed(false);
        theTable.setCellSelectionEnabled(true);
        theTable.setCellEditor(cellEditor);
        for (int i = 0; i < theTable.getColumnCount(); i++) {
            TableColumn tc = theTable.getColumnModel().getColumn(i);
            XioTableCellEditor colCellEditor = new XioTableCellEditor(nRows);
            tc.setCellRenderer(colCellEditor);
        }
        Iterator it = theNodes.iterator();
        AcceleratorNode theNode;
        double[] xGrid = new double[theNodes.size()];
        theChannels = new Channel[theNodes.size()][inputPVs.length];
        while (it.hasNext()) {
            theNode = ((AcceleratorNode) (it.next()));
            xGrid[row] = theDoc.getSelectedSequence().getPosition(theNode);
            theTableModel.addRowName(theNode.getId(), row);
            java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(3);
            theTableModel.setValueAt(nf.format(xGrid[row]), row, 1);
            int numINs = 0;
            if (inputPVs != null) {
                numINs += inputPVs.length;
                pvs = inputPVs.length;
                for (int ci = 0; ci < inputPVs.length; ci++) {
                    try {
                        theChannels[row][ci] = theNode.getChannel(inputPVs[ci]);
                        if (theChannels[row] != null) {
                            pvCell = new InputPVTableCell(theChannels[row][ci], row, ci + 2);
                            theTableModel.addPVCell(pvCell, row, ci + 2);
                            getChannelVec(theChannels[row][ci]).add(pvCell);
                        }
                    } catch (NoSuchChannelException e) {
                    }
                }
            }
            row++;
        }
        IODiagPlotter thePlotter = new IODiagPlotter(theNodes, theTable, theDoc, typeID);
        theDoc.tableXYPlotMap.put(typeID, thePlotter);
        JButton newBtn = new JButton("X-Y Plot");
        newBtn.addActionListener(thePlotter);
        theTableModel.addJButton(0, newBtn);
        JButton[] plotButtons = new JButton[inputPVs.length];
        final WaterfallPlot[] wfps = new WaterfallPlot[inputPVs.length];
        for (int ci = 0; ci < inputPVs.length; ci++) {
            String pName = ((AcceleratorNode) theNodes.get(0)).getType() + colNames[ci + 1];
            final WaterfallPlot theWaterfallPlot = new WaterfallPlot(pName, xGrid, theTable, ci + 2, theDoc.getMainWindow());
            wfps[ci] = theWaterfallPlot;
            plotButtons[ci] = new JButton("Water Fall");
            theTableModel.h2OPlots.put(colNames[ci + 1], wfps[ci]);
            theTableModel.addJButton(ci + 1, plotButtons[ci]);
            plotButtons[ci].addActionListener(wfps[ci]);
        }
        theTable.getColumnModel().getColumn(0).setPreferredWidth(125);
        JScrollPane theScrollPane = new JScrollPane(theTable);
        theTable.setPreferredScrollableViewportSize(theTable.getPreferredSize());
        final TableProdder prodder = new TableProdder(theTableModel);
        prodder.start();
        return theScrollPane;
    }
