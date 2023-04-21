    private void updateAddRemovePanel() {
        int i_selected = psTable.getSelectedRow();
        if (i_selected >= 0) {
            removePSButton.setEnabled(true);
            PowerSupplyCycler psc = (PowerSupplyCycler) PowerSupplyCyclerV.get(i_selected);
            if (!psc.getChannelName().equals("null")) {
                pvSetNameJText.setText(psc.getChannelName());
            } else {
                pvSetNameJText.setText(null);
            }
            if (!psc.getChannelNameRB().equals("null")) {
                pvRBNameJText.setText(psc.getChannelNameRB());
            } else {
                pvRBNameJText.setText(null);
            }
        } else {
            removePSButton.setEnabled(false);
            pvSetNameJText.setText(null);
            pvRBNameJText.setText(null);
        }
        messageTextLocal.setText(null);
    }
