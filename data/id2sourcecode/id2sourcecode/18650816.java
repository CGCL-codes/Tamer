    private synchronized void loadSensorInfo(int index) {
        SensorInfo s;
        ContinousInfo ci;
        TriggerInfo ti;
        int i, min;
        System.out.println("Loading state of " + index + ", invert " + si[index].isInvert());
        s = si[index];
        SensorNametextField.setText(s.getName());
        if (radioCheckBox.isSelected() != s.isRadiometric()) {
            radioCheckBox.doClick();
        }
        reactionSlider.setValue(s.getReaction());
        if (activeCheckBox.isSelected() != s.isActive()) {
            activeCheckBox.doClick();
        }
        if (triggerCheckBox.isSelected() != s.isTrigger()) {
            triggerCheckBox.doClick();
        }
        if (invertCheckBox.isSelected() != s.isInvert()) {
            invertCheckBox.doClick();
        }
        ti = s.getTi();
        if (releaseCheckBox.isSelected() != ti.isRelease()) {
            releaseCheckBox.doClick();
        }
        comboBox.setSelectedIndex(ti.getChannel());
        sensitivity.setColoredValues(ti.getMin(), ti.getMax());
        KeyPanel1textField_3.setText(ti.getNote() + "");
        if (suspendCheckBox.isSelected() != ti.isSuspend()) {
            suspendCheckBox.doClick();
        }
        TimePanel1textField_2.setText(ti.getTime() + "");
        ci = s.getCi();
        comboBox_1.setSelectedIndex(ci.getModule());
        loadModuleInfo(ci.getModule());
        System.out.println("Check for active sensors in " + index + "... " + si[index].isActive());
        if (si[index].isActive() && index < 8) {
            min = 0;
            for (i = 0; i < 8 && min == 0; i++) {
                if (i != index && si[i].isActive()) {
                    min = min + 1;
                }
            }
            System.out.println("Sensors active: " + min);
            if (min == 0) {
                activeCheckBox.setEnabled(false);
            }
        }
    }
