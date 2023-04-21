    private void makeMagnetTable() {
        magnetTableModel = new MagnetTableModel(this);
        magnetTable = new JTable(magnetTableModel);
        magnetTable.setRowSelectionAllowed(true);
        magnetTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent event) {
                int col = magnetTable.columnAtPoint(event.getPoint());
                int row = magnetTable.getSelectedRow();
                System.out.println("row = " + row + " col = " + col);
                if (col == 1) {
                    Channel chan = B_Sets.get(row).getChannel();
                    theDoc.myWindow().wheelPanel.setChannel(chan);
                }
                if (col == 2) {
                    Channel chan = B_Trim_Sets.get(row).getChannel();
                    theDoc.myWindow().wheelPanel.setChannel(chan);
                }
                if (col == 4) {
                    Channel chan = B_Books.get(row).getChannel();
                    theDoc.myWindow().wheelPanel.setChannel(chan);
                }
            }
        });
    }
