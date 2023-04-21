    public void renderCsvDescriptor(String label, String csv, final JPanel destination) {
        String[] rows = csv.split("\n");
        String[] columnNames = rows[0].split(";");
        String[][] tableData = new String[rows.length - 1][];
        for (int i = 0; i < rows.length - 1; ++i) {
            tableData[i] = rows[i + 1].split(";");
        }
        JTable csvTable = new JTable();
        DefaultTableModel csvModel = new DefaultTableModel(tableData, columnNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        csvTable.setModel(csvModel);
        final JScrollPane scrollTable = new JScrollPane(csvTable);
        scrollTable.setBorder(BorderFactory.createTitledBorder(label));
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                destination.add(scrollTable);
                destination.validate();
                destination.repaint();
            }
        });
    }
