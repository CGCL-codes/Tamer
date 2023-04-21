    private JComponent createTable(final ShowContext context) {
        tableModel = new DimmerTableModel(context);
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(1).setPreferredWidth(140);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        JComboBox combo = new JComboBox();
        combo.setRenderer(new ChannelNameListCellRenderer());
        combo.addItem(new Channel(0, "not patched"));
        Channels channels = context.getShow().getChannels();
        for (int i = 0; i < channels.size(); i++) {
            combo.addItem(channels.get(i));
        }
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(combo));
        table.setDefaultRenderer(Channel.class, new ChannelNameTableCellRenderer());
        return new JScrollPane(table);
    }
