    public JLabel getTransferFromNameLabel() {
        if (transferFromNameLabel == null) {
            transferFromNameLabel = new JLabel();
            transferFromNameLabel.setText("Bank Name");
            transferFromNameLabel.setBounds(175, 56, 200, 20);
            setRequiredIcon(transferFromNameLabel);
            WidgetProperties.setLabelProperties(transferFromNameLabel);
        }
        return transferFromNameLabel;
    }
