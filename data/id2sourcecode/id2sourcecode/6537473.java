    @Override
    protected void readInParams() {
        emuConfig = parent.getEmuConfig();
        Harddiskdrive hddConfig = emuConfig.getArchitecture().getModules().getAta().getHarddiskdrive().get(0);
        Integer updateInt = emuConfig.getArchitecture().getModules().getAta().getUpdateintervalmicrosecs().intValue();
        boolean isEnabled = hddConfig.isEnabled();
        int channelIndex = hddConfig.getChannelindex().intValue();
        boolean isMaster = hddConfig.isMaster();
        boolean autoDetect = hddConfig.isAutodetectcylinders();
        int cylinders = hddConfig.getCylinders().intValue();
        int heads = hddConfig.getHeads().intValue();
        int sectors = hddConfig.getSectorspertrack().intValue();
        String imageFormatPath = Utilities.resolvePathAsString(hddConfig.getImagefilepath());
        this.updateIntField.setValue(updateInt);
        this.enabledCheckBox.setSelected(isEnabled);
        this.channelIndexFTextField.setValue(channelIndex);
        this.masterCheckBox.setSelected(isMaster);
        this.autoDetectCheckBox.setSelected(autoDetect);
        this.cylindersTextField.setValue(cylinders);
        this.headsTextField.setValue(heads);
        this.sectorsTextField.setValue(sectors);
        this.imageFilePathLabel.setText(imageFormatPath);
        this.selectedFile = new File(imageFormatPath);
    }
