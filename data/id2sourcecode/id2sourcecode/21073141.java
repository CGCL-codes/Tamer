    protected void initializeBCMPane(java.util.List nodes) {
        theNodes = nodes;
        String[] columnNames = { "BCM" };
        bcmTableModel = new DeviceTableModel(columnNames, nodes.size());
        bcmTable = new JTable(bcmTableModel);
        bcmNames = new String[nodes.size()];
        bcmWFChs = new Channel[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            bcmNames[i] = ((AcceleratorNode) nodes.get(i)).getId();
            bcmTableModel.addRowName(bcmNames[i], i);
            bcmWFChs[i] = ChannelFactory.defaultFactory().getChannel(bcmNames[i] + ":currentTBT");
        }
        JScrollPane bcmScrollPane = new JScrollPane(bcmTable);
        bcmScrollPane.setPreferredSize(new Dimension(200, 400));
        EdgeLayout edgeLayout = new EdgeLayout();
        setLayout(edgeLayout);
        edgeLayout.setConstraints(bcmScrollPane, 50, 30, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        add(bcmScrollPane);
        JPanel plotPane = new JPanel();
        plotPane.setLayout(new BoxLayout(plotPane, BoxLayout.Y_AXIS));
        plotPane.setPreferredSize(new Dimension(750, 470));
        bcmPlot = new FunctionGraphsJPanel();
        bcmPlot.setLimitsAndTicksX(0., 1200., 400.);
        bcmPlot.addCurveData(bcmPlotData);
        bcmPlot.setName("BCM Waveform: ");
        bcmPlot.setAxisNames("point no.", "I (mA)");
        bcmPlot.addMouseListener(new SimpleChartPopupMenu(bcmPlot));
        plotPane.add(bcmPlot);
        edgeLayout.setConstraints(plotPane, 20, 470, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        add(plotPane);
        bcmTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        ListSelectionModel rowSM = bcmTable.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                } else {
                    selectedRow = lsm.getMinSelectionIndex();
                    updatePlot(bcmNames[selectedRow]);
                }
            }
        });
    }
