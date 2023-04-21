    private void refresh() {
        this.testSensor1Channel_cb.removeAllItems();
        CustomValueSensorListItem li_1 = (CustomValueSensorListItem) this.testSensor1_cb.getSelectedItem();
        ValueSensor s1 = li_1.getSensor();
        List<Channel> channels = s1.getChannels();
        for (Channel c : channels) {
            this.testSensor1Channel_cb.addItem(c);
        }
        this.testSensor2Channel_cb.removeAllItems();
        CustomValueSensorListItem li_2 = (CustomValueSensorListItem) this.testSensor2_cb.getSelectedItem();
        ValueSensor s2 = li_2.getSensor();
        channels = s2.getChannels();
        for (Channel c : channels) {
            this.testSensor2Channel_cb.addItem(c);
        }
        if (s1.getHardwareClassname().equals(s2.getHardwareClassname())) {
            testValue_tf.setEnabled(true);
            availableUnits_cb.setEnabled(true);
            testOperators_cb.setEnabled(true);
            availableUnits_cb.removeAllItems();
            Set<Unit> units = s1.getAvailableUnits();
            for (Unit unit : units) {
                availableUnits_cb.addItem(unit);
            }
            this.save_b.setEnabled(true);
        } else {
            testValue_tf.setEnabled(false);
            availableUnits_cb.setEnabled(false);
            testOperators_cb.setEnabled(false);
            this.save_b.setEnabled(false);
        }
    }
