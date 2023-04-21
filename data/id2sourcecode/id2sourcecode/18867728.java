    private JComponent createTable(final Channels channels) {
        tableModel = new ChannelTableModel(channels);
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(2);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(final ListSelectionEvent e) {
            }
        });
        table.addMouseListener(new MouseAdapter() {

            public void mousePressed(final MouseEvent e) {
                rowSelectedAtMousePressed = table.getSelectedRow();
            }

            public void mouseReleased(final MouseEvent e) {
                int index = table.getSelectedRow();
                if (index != -1) {
                    if (rowSelectedAtMousePressed != index) {
                        Channel channel1 = tableModel.getChannels().get(index);
                        Channel channel2 = tableModel.getChannels().get(rowSelectedAtMousePressed);
                        String name = channel1.getName();
                        channel1.setName(channel2.getName());
                        channel2.setName(name);
                    }
                }
                rowSelectedAtMousePressed = -1;
            }
        });
        return new JScrollPane(table);
    }
