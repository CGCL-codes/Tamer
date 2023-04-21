    private void loadModuleInfo(int index) {
        SensorInfo s;
        ModuleInfo mi;
        s = si[previousIndex];
        mi = s.getCi().getMi()[index];
        if (invertModuleCheckBox.isSelected() != mi.isInvert()) {
            invertModuleCheckBox.doClick();
        }
        if (activeCheckBox_1.isSelected() != mi.isActive()) {
            activeCheckBox_1.doClick();
        }
        comboBox_2.setSelectedIndex(mi.getChannel());
        if (latencyCheckBox.isSelected() != mi.isLatency()) {
            latencyCheckBox.doClick();
        }
        sensitivity2.setColoredValues(mi.getMin(), mi.getMax());
        KeyPanel2textField.setText(mi.getNote() + "");
        if (mi.isPitch()) {
            pitchRadioButton.doClick();
        } else if (mi.isVel()) {
            velocityRadioButton.doClick();
        } else {
            modulationRadioButton.doClick();
        }
        TimePanel2textField_4.setText(mi.getTime() + "");
    }
