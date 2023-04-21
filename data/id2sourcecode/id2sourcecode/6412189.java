    public BeamTrigger() {
        tDelayText.setHorizontalAlignment(JTextField.CENTER);
        tDelayText.setNormalBackground(Color.white);
        tDelayText.setDecimalFormat(tDelayFormat);
        tDelayText.setValue(0.2);
        TimingCenter tmCenter = TimingCenter.getDefaultTimingCenter();
        ch = tmCenter.getChannel(TimingCenter.TRIGGER_HANDLE);
        useTriggerButton.setSelected(true);
        useTriggerButton.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setOnOff(true);
                } else {
                    setOnOff(false);
                }
            }
        });
    }
