    public ScalarPVsValuesTable(ScalarPVs spvsIn) {
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
                    return new Boolean(spvs.getScalarPV(row).showValue());
                }
                if (col == 2) {
                    return new Boolean(spvs.getScalarPV(row).showRef());
                }
                return new Boolean(spvs.getScalarPV(row).showDif());
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
                    spvs.getScalarPV(row).showValue(((Boolean) value).booleanValue());
                }
                if (col == 2) {
                    spvs.getScalarPV(row).showRef(((Boolean) value).booleanValue());
                }
                if (col == 3) {
                    spvs.getScalarPV(row).showDif(((Boolean) value).booleanValue());
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
                double val = spvs.getScalarPV(row).getRefValue();
                return fmtVal.format(val);
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        leftTable = new JTable(leftTableModel);
        centerTable = new JTable(centerTableModel);
        rightTable = new JTable(rightTableModel);
        leftTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        leftTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        leftTable.getColumnModel().getColumn(2).setPreferredWidth(50);
        leftR0.setBackground(mainLocalPanel.getBackground());
        leftR1.setBackground(Color.blue);
        leftR2.setBackground(Color.red);
        leftR3.setBackground(Color.magenta);
        leftR0.setHorizontalAlignment(JLabel.CENTER);
        leftR1.setHorizontalAlignment(JLabel.CENTER);
        leftR2.setHorizontalAlignment(JLabel.CENTER);
        leftR3.setHorizontalAlignment(JLabel.CENTER);
        leftTable.getColumnModel().getColumn(0).setHeaderRenderer(leftR0);
        leftTable.getColumnModel().getColumn(1).setHeaderRenderer(leftR1);
        leftTable.getColumnModel().getColumn(2).setHeaderRenderer(leftR2);
        leftTable.getColumnModel().getColumn(3).setHeaderRenderer(leftR3);
        centerR0.setBackground(mainLocalPanel.getBackground());
        rightR0.setBackground(mainLocalPanel.getBackground());
        centerR0.setHorizontalAlignment(JLabel.CENTER);
        rightR0.setHorizontalAlignment(JLabel.CENTER);
        centerTable.getColumnModel().getColumn(0).setHeaderRenderer(centerR0);
        rightTable.getColumnModel().getColumn(0).setHeaderRenderer(rightR0);
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
