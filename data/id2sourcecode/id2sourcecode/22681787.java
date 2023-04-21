    public void actionPerformed(ActionEvent e) {
        ChannelSearchInputDialog searchDialog = new ChannelSearchInputDialog(ChannelEditor.application);
        int result = searchDialog.showSearchDialog(this.lastSearchText);
        if (result == ChannelSearchInputDialog.RESULT_SEARCH) {
            SearchFilter filter = searchDialog.getSearchFilter();
            this.lastSearchText = filter.getSearchText();
            if (!ChannelEditor.application.getChannelListingPanel().selectAllNodesFiltered(filter)) {
                JOptionPane.showMessageDialog(ChannelEditor.application, Messages.getString("SearchAction.2") + this.lastSearchText, Messages.getString("SearchAction.3"), JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
