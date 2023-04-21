    private JPopupMenu getRootPopup() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Import data...", DataViewer.getIcon("icons/import.gif"));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                ActionFactory.getInstance().getDataImportAction().importData();
            }
        });
        popup.add(menuItem);
        menuItem = new JMenuItem("Export data...", DataViewer.getIcon("icons/export.gif"));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                ActionFactory.getInstance().getDataExportAction().exportData(RBNBUtilities.getAllChannels(treeModel.getChannelTree(), treeModel.isHiddenChannelsVisible()));
            }
        });
        popup.add(menuItem);
        return popup;
    }
