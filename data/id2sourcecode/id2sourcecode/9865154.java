    public void setValues() {
        Settings settings = settingsService.getSettings();
        HashSet<CommPortIdentifier> devices = serialPortService.getAvailablePorts();
        Iterator deviceIterator = devices.iterator();
        while (deviceIterator.hasNext()) {
            CommPortIdentifier commPortIdentifier = (CommPortIdentifier) deviceIterator.next();
            String port = commPortIdentifier.getName();
            jComboBoxDevice.addItem(port);
        }
        for (BaudRate baudRate : BaudRate.values()) {
            jComboBoxBaudRate.addItem(baudRate);
        }
        for (DataBits dataBits : DataBits.values()) {
            jComboBoxDataBits.addItem(dataBits);
        }
        for (StopBits stopBits : StopBits.values()) {
            jComboBoxStopBits.addItem(stopBits);
        }
        for (Parity parity : Parity.values()) {
            jComboBoxParity.addItem(parity);
        }
        for (CommandFormat commandFormat : CommandFormat.values()) {
            jComboBoxCommandFormat.addItem(commandFormat);
        }
        jComboBoxDevice.setSelectedItem(settings.getDevice());
        jComboBoxBaudRate.setSelectedItem(settings.getBaudRate());
        jComboBoxDataBits.setSelectedItem(settings.getDataBits());
        jComboBoxStopBits.setSelectedItem(settings.getStopBits());
        jComboBoxParity.setSelectedItem(settings.getParity());
        jSpinnerPrecision.setValue(settings.getPrecision());
        jSpinnerChannels.setValue(settings.getChannels());
        jTextFieldStartCommand.setText(String.valueOf(settings.getStartCommand()));
        jTextFieldStopCommand.setText(String.valueOf(settings.getStopCommand()));
        jComboBoxCommandFormat.setSelectedItem(settings.getCommandFormat());
        jComboBoxCommandFormat.setEnabled(false);
    }
