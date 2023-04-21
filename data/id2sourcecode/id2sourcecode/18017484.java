    private void actionSelectAllNotInGroup() {
        int index1 = getChannelsNotInGroupCount() - 1;
        notInGroupTable.getSelectionModel().setSelectionInterval(0, index1);
        updateWidgets();
    }
