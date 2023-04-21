    private void createComponents() {
        m_hostGame = new JButton("Host Game");
        m_joinGame = new JButton("Join Game");
        m_bootGame = new JButton("Boot Game");
        m_gameTableModel = new LobbyGameTableModel(m_messengers.getMessenger(), m_messengers.getChannelMessenger(), m_messengers.getRemoteMessenger());
        m_tableSorter = new TableSorter(m_gameTableModel);
        m_gameTable = new LobbyGameTable(m_tableSorter);
        m_tableSorter.setTableHeader(m_gameTable.getTableHeader());
        m_gameTable.setColumnSelectionAllowed(false);
        m_gameTable.setRowSelectionAllowed(true);
        m_gameTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final int dateColumn = m_gameTableModel.getColumnIndex(LobbyGameTableModel.Column.Started);
        m_tableSorter.setSortingStatus(dateColumn, TableSorter.DESCENDING);
        m_gameTable.getColumnModel().getColumn(m_gameTableModel.getColumnIndex(LobbyGameTableModel.Column.Players)).setPreferredWidth(65);
        m_gameTable.getColumnModel().getColumn(m_gameTableModel.getColumnIndex(LobbyGameTableModel.Column.Status)).setPreferredWidth(150);
        m_gameTable.getColumnModel().getColumn(m_gameTableModel.getColumnIndex(LobbyGameTableModel.Column.Name)).setPreferredWidth(150);
        m_gameTable.getColumnModel().getColumn(m_gameTableModel.getColumnIndex(LobbyGameTableModel.Column.Comments)).setPreferredWidth(150);
        m_gameTable.setDefaultRenderer(Date.class, new DefaultTableCellRenderer() {

            private final SimpleDateFormat format = new SimpleDateFormat("hh:mm a");

            @Override
            public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setText(format.format((Date) value));
                return this;
            }
        });
    }
