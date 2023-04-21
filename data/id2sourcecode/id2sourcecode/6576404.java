    public LogPanel(LogView view, StyleSheet ss) {
        super(new BorderLayout());
        this.logView = view;
        String[] columnNames = { "Date", "ChannelRef", "Source", "Message" };
        String[][] data = new String[view.size()][4];
        for (int row = 0; row < view.size(); row++) {
            data[row][0] = ((Message) view.get(row)).getDate().toString();
            data[row][1] = ((Message) view.get(row)).getChannel();
            data[row][2] = ((Message) view.get(row)).getAvatar();
            data[row][3] = ((Message) view.get(row)).getContent();
        }
        LogTableModel tm = new LogTableModel();
        tm.setDataVector(data, columnNames);
        this.table = new JTable();
        this.table.setModel(tm);
        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(0).setMinWidth(120);
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setMinWidth(100);
        tcm.getColumn(1).setPreferredWidth(100);
        tcm.getColumn(2).setMinWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(1024);
        table.getColumn("Date").setCellRenderer(new LogRenderer(ss));
        table.getColumn("ChannelRef").setCellRenderer(new LogRenderer(ss));
        table.getColumn("Source").setCellRenderer(new LogRenderer(ss));
        table.getColumn("Message").setCellRenderer(new LogRenderer(ss));
        LogTableListener listener = new LogTableListener(tm);
        table.addMouseListener(listener);
        this.scrollPane = new JScrollPane(table);
        this.scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        this.add(scrollPane);
    }
