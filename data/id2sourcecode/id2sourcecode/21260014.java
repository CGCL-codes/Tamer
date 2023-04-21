    protected void updateView() {
        updatingView = true;
        channelField.setText(trigger.getChannelName());
        enableButton.setEnabled(trigger.canEnable());
        enableButton.setSelected(trigger.isEnabled());
        enableButton.setText(trigger.isEnabled() ? "Enabled" : "Enable");
        filterMenu.setEnabled(trigger.canEnable());
        filterMenu.setSelectedItem(trigger.getFilterLabel());
        filterTable.setEnabled(trigger.canEnable());
        filterTableModel.setParameters(trigger.getFilterParameters());
        channelField.setEnabled(!trigger.isSettingChannel());
        channelSetButton.setEnabled(!trigger.isSettingChannel());
        connectionLabel.setText(connectionString(trigger.isConnected()));
        repaint();
        updatingView = false;
    }
