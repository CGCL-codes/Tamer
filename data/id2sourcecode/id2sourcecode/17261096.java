    public ContentController() {
        contentUC.setUpdateTime(1.0);
        runnerPSG.setName("PS Group");
        makeAddRemovePanel();
        makeParamsPanel();
        JPanel cntrlPanel = new JPanel();
        cntrlPanel.setLayout(new VerticalLayout());
        cntrlPanel.add(leftTopTitleText);
        groupList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "pv goups"));
        cntrlPanel.add(groupList);
        JPanel graphAndButtonPanel = new JPanel(new BorderLayout());
        graphAndButtonPanel.add(graphSubPanel, BorderLayout.CENTER);
        graphAndButtonPanel.add(addRemovePanel, BorderLayout.SOUTH);
        graphAndButtonPanel.setBorder(BorderFactory.createEtchedBorder());
        contentControllerPanel.setLayout(new BorderLayout());
        contentControllerPanel.add(cntrlPanel, BorderLayout.WEST);
        contentControllerPanel.add(graphAndButtonPanel, BorderLayout.CENTER);
        SimpleChartPopupMenu.addPopupMenuTo(graphSubPanel);
        graphSubPanel.setOffScreenImageDrawing(true);
        graphSubPanel.setName("Cycler : Current vs. Time");
        graphSubPanel.setAxisNames("time, sec", "Current, A");
        graphSubPanel.setGraphBackGroundColor(Color.white);
        graphSubPanel.setLegendButtonVisible(true);
        graphSubPanel.setLegendBackground(Color.white);
        JScrollPane scrollPane = new JScrollPane(psTable);
        scrollPane.setPreferredSize(new Dimension(1, 150));
        cntrlPanel.add(scrollPane);
        cntrlPanel.add(timeTableParamsPanel);
        groupList.setVisibleRowCount(5);
        groupList.setEnabled(true);
        groupList.setFixedCellWidth(10);
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionListener groupListListener = new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                psgSelectionChange();
            }
        };
        groupList.addListSelectionListener(groupListListener);
        AbstractTableModel tableModel = new AbstractTableModel() {

            public String getColumnName(int col) {
                if (col == 0) {
                    return "Power Supply PV";
                }
                return " Use";
            }

            public int getRowCount() {
                return PowerSupplyCyclerV.size();
            }

            public int getColumnCount() {
                return 2;
            }

            public Object getValueAt(int row, int col) {
                PowerSupplyCycler psc = (PowerSupplyCycler) PowerSupplyCyclerV.get(row);
                if (col == 1) {
                    return new Boolean(psc.getActive());
                }
                return psc.getChannelName();
            }

            public boolean isCellEditable(int row, int col) {
                if (col == 1) {
                    return true;
                }
                return false;
            }

            public Class getColumnClass(int c) {
                if (c == 1) {
                    return (new Boolean(true)).getClass();
                }
                return (new String("a")).getClass();
            }

            public void setValueAt(Object value, int row, int col) {
                if (col == 1) {
                    PowerSupplyCycler psc = (PowerSupplyCycler) PowerSupplyCyclerV.get(row);
                    psc.setActive(!psc.getActive());
                    updateGraphDataSet();
                }
                fireTableCellUpdated(row, col);
            }
        };
        psTable.setModel(tableModel);
        psTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableColumn column = null;
        for (int i = 0; i < 2; i++) {
            column = psTable.getColumnModel().getColumn(i);
            if (i == 1) {
                column.setPreferredWidth(1);
            } else {
                column.setPreferredWidth(1000);
            }
        }
        psTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                updateGraphDataSet();
                updateAddRemovePanel();
                updateParamsPanel();
            }
        });
        contentUC.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graphSubPanel.refreshGraphJPanel();
            }
        });
    }
