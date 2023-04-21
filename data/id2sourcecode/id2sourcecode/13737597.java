    private void updateOnEDT(S tableRow) {
        getHistoricalDataTableModel().addRow(tableRow);
        this.currentDataTextfields.get(tableRow.getChannel()).setText(tableRow.toString());
        this.panelGroupManager.updatePanels();
    }
