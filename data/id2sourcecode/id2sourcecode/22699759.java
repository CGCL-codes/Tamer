    public JLabel getTransferFromLabel() {
        if (transferFromLabel == null) {
            transferFromLabel = new JLabel();
            transferFromLabel.setText("Transfer from");
            transferFromLabel.setBounds(10, 56, 141, 20);
            setRequiredIcon(transferFromLabel);
            WidgetProperties.setLabelProperties(transferFromLabel);
        }
        return transferFromLabel;
    }
