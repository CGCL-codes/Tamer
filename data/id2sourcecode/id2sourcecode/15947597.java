    public ArrayPVsTable(Vector arrayPVsIn) {
        arrayPVsExt = arrayPVsIn;
        fmtVal.setFixedLength(true);
        leftTableModel = new AbstractTableModel() {

            @Override
            public String getColumnName(int col) {
                return columnNamesLeftTable[col].toString();
            }

            public int getRowCount() {
                return arrayPVs.size();
            }

            public int getColumnCount() {
                return 3;
            }

            public Object getValueAt(int row, int col) {
                ArrayViewerPV arrPV = (ArrayViewerPV) arrayPVs.get(row);
                if (col == 0) {
                    boolean wrap = arrPV.getWrapDataProperty();
                    return new Boolean(wrap);
                }
                if (col == 1) {
                    return fmtVal.format(arrPV.getAvgValue());
                }
                return fmtVal.format(arrPV.getSigmaValue());
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 0) {
                    return true;
                }
                return false;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                if (col == 0) {
                    ArrayViewerPV arrPV = (ArrayViewerPV) arrayPVs.get(row);
                    arrPV.setWrapDataProperty(((Boolean) value).booleanValue());
                    fireTableCellUpdated(row, col);
                }
            }

            @Override
            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        centerTableModel = new AbstractTableModel() {

            @Override
            public String getColumnName(int col) {
                return columnNamesCenterTable[col].toString();
            }

            public int getRowCount() {
                return arrayPVs.size();
            }

            public int getColumnCount() {
                return 1;
            }

            public Object getValueAt(int row, int col) {
                ArrayViewerPV arrPV = (ArrayViewerPV) arrayPVs.get(row);
                return arrPV.getChannelName();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        rightTableModel = new AbstractTableModel() {

            @Override
            public String getColumnName(int col) {
                return columnNamesRightTable[col].toString();
            }

            public int getRowCount() {
                return arrayPVs.size();
            }

            public int getColumnCount() {
                return 1;
            }

            public Object getValueAt(int row, int col) {
                ArrayViewerPV arrPV = (ArrayViewerPV) arrayPVs.get(row);
                return arrPV.getColor();
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
                ArrayViewerPV arrPV = (ArrayViewerPV) arrayPVs.get(row);
                jl.setBackground(arrPV.getColor());
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
                ActionEvent stateChangedAction = new ActionEvent(arrayPVs, 0, "changed");
                int nL = changeListenersV.size();
                for (int i = 0; i < nL; i++) {
                    ((ActionListener) changeListenersV.get(i)).actionPerformed(stateChangedAction);
                }
            }
        });
        leftTableModelEvent = new TableModelEvent(leftTableModel);
        centerTableModelEvent = new TableModelEvent(centerTableModel);
        rightTableModelEvent = new TableModelEvent(rightTableModel);
        doLayout();
    }
