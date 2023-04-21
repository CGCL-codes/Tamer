    public PVsPanel(MTVDocument aDocument) {
        super(new BorderLayout());
        theDoc = aDocument;
        JPanel pvNamePanel0 = new JPanel(new BorderLayout());
        JPanel pvNamePanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel pvNamePanel = new JPanel(new BorderLayout());
        pvNamePanel0.add(pvNameText, BorderLayout.CENTER);
        pvNamePanel1.add(addPVtoTableButton);
        pvNamePanel.add(pvNamePanel0, BorderLayout.NORTH);
        pvNamePanel.add(pvNamePanel1, BorderLayout.SOUTH);
        addPVtoTableButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String chanName = pvNameText.getText();
                Channel chan = ChannelFactory.defaultFactory().getChannel(chanName);
                chan.connectAndWait();
                try {
                    double[] valArr = chan.getArrDbl();
                    PVTableCell cell = getNewCell(chan);
                    PVs.add(cell);
                    updatePVsTable();
                } catch (Exception excp) {
                    pvNameText.setText(null);
                    pvNameText.setText("Bad Channel Name. Try again!");
                }
            }
        });
        makePVsTable();
        pvsTable.setCellSelectionEnabled(true);
        tableScrollPane = new JScrollPane(pvsTable);
        ActionListener taskPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tableTask();
            }
        };
        timer = new javax.swing.Timer(1500, taskPerformer);
        timer.start();
        final TableCellRenderer defaultRenderer = pvsTable.getDefaultRenderer(String.class);
        TableCellRenderer newRenderer = new TableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column != 0) {
                    PVTableCell cell = (PVTableCell) pvsTableModel.getValueAt(row, column);
                    if (cell.isValueChanged()) {
                        component.setForeground(Color.blue);
                    } else {
                        component.setForeground(Color.black);
                    }
                } else {
                    component.setForeground(Color.black);
                }
                return component;
            }
        };
        pvsTable.setDefaultRenderer(String.class, newRenderer);
        pvsTable.setDefaultRenderer(PVTableCell.class, newRenderer);
        add(pvNamePanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }
