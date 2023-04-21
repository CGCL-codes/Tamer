    private void btnAddVdrActionPerformed(ActionEvent evt) {
        if (!tableVdrModel.contains("")) {
            int last = ConfigController.getInstance().getUMCConfig().getTv().getChannelArray().length;
            ConfigController.getInstance().getUMCConfig().getTv().insertNewChannel(last);
            tableVdrModel.addChannel(ConfigController.getInstance().getUMCConfig().getTv().getChannelArray(last));
            tableVdrModel.fireTableRowsInserted(tableVdrModel.getRowCount() - 1, tableVdrModel.getRowCount());
            tableVdr.setRowSelectionInterval(tableVdrModel.getRowCount() - 1, tableVdrModel.getRowCount() - 1);
        }
    }
