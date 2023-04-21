    public ScalarPVsChartsTable(ScalarPVs spvsIn) {
        spvs = spvsIn;
        TableModel leftTableModel = new AbstractTableModel() {

            @Override
            public String getColumnName(int col) {
                return columnNamesLeftTable[col].toString();
            }

            public int getRowCount() {
                return spvs.getSize();
            }

            public int getColumnCount() {
                return 4;
            }

            public Object getValueAt(int row, int col) {
                if (col == 0) {
                    return new Integer(row + 1);
                }
                if (col == 1) {
                    return new Boolean(spvs.getScalarPV(row).showValueChart());
                }
                if (col == 2) {
                    return new Boolean(spvs.getScalarPV(row).showRefChart());
                }
                return new Boolean(spvs.getScalarPV(row).showDifChart());
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 0) {
                    return false;
                }
                return true;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                if (col == 0) {
                    return;
                }
                if (col == 1) {
                    spvs.getScalarPV(row).showValueChart(((Boolean) value).booleanValue());
                }
                if (col == 2) {
                    spvs.getScalarPV(row).showRefChart(((Boolean) value).booleanValue());
                }
                if (col == 3) {
                    spvs.getScalarPV(row).showDifChart(((Boolean) value).booleanValue());
                }
                fireTableCellUpdated(row, col);
            }

            @Override
            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        TableModel centerTableModel = new AbstractTableModel() {

            @Override
            public String getColumnName(int col) {
                return columnNamesCenterTable[col].toString();
            }

            public int getRowCount() {
                return spvs.getSize();
            }

            public int getColumnCount() {
                return 1;
            }

            public Object getValueAt(int row, int col) {
                return spvs.getScalarPV(row).getMonitoredPV().getChannelName();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        TableModel rightTableModel = new AbstractTableModel() {

            @Override
            public String getColumnName(int col) {
                return columnNamesRightTable[col].toString();
            }

            public int getRowCount() {
                return spvs.getSize();
            }

            public int getColumnCount() {
                return 1;
            }

            public Object getValueAt(int row, int col) {
                return spvs.getScalarPV(row).getChartColor();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            @Override
            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        leftTable = new JTable(leftTableModel);
        centerTable = new JTable(centerTableModel);
        rightTable = new JTable(rightTableModel);
        rightTable.setDefaultRenderer(Color.red.getClass(), new TableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel jl = new JLabel(" ");
                jl.setOpaque(true);
                jl.setBackground(spvs.getScalarPV(row).getChartColor());
                return jl;
            }
        });
        leftTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        leftTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        leftTable.getColumnModel().getColumn(2).setPreferredWidth(50);
        leftTable.setGridColor(Color.black);
        centerTable.setGridColor(Color.black);
        rightTable.setGridColor(Color.black);
        leftTable.setShowGrid(true);
        centerTable.setShowGrid(true);
        rightTable.setShowGrid(true);
        leftTablePanel.add(leftTable.getTableHeader(), BorderLayout.PAGE_START);
        leftTablePanel.add(leftTable, BorderLayout.CENTER);
        centerTablePanel.add(centerTable.getTableHeader(), BorderLayout.PAGE_START);
        centerTablePanel.add(centerTable, BorderLayout.CENTER);
        rightTablePanel.add(rightTable.getTableHeader(), BorderLayout.PAGE_START);
        rightTablePanel.add(rightTable, BorderLayout.CENTER);
        tablePanel.add(leftTablePanel, BorderLayout.WEST);
        tablePanel.add(centerTablePanel, BorderLayout.CENTER);
        tablePanel.add(rightTablePanel, BorderLayout.EAST);
        JScrollPane scrollpane = new JScrollPane(tablePanel);
        mainLocalPanel.add(scrollpane, BorderLayout.CENTER);
        leftTableModel.addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                ActionEvent stateChangedAction = new ActionEvent(spvs, 0, "changed");
                int nL = changeListenersV.size();
                for (int i = 0; i < nL; i++) {
                    ((ActionListener) changeListenersV.get(i)).actionPerformed(stateChangedAction);
                }
            }
        });
    }
