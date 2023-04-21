    private void loadEditor() {
        SensorCondition condition = this.getUserObject();
        if (condition != null) {
            this.conditionName_tf.setText(condition.getConditionName());
            this.conditionName_tf.setEditable(false);
            this.sensors_cb.setSelectedItem(condition.getSensor());
            this.sensors_cb.setEnabled(false);
            this.channel_cb.setSelectedItem(condition.getChannel());
            this.channel_cb.setEnabled(false);
            refresh();
            if (condition instanceof ValueCondition) {
                this.testOperators_cb.setSelectedItem(((ValueCondition) condition).getTestOperator());
                this.testValue_tf.setText(((ValueCondition) condition).getTestValue().getValue().toString());
                this.availableUnits_cb.setSelectedItem(((ValueCondition) condition).getTestValue().getUnit());
            } else if (condition instanceof StateCondition) {
                this.testState_cb.setSelectedItem(((StateCondition) condition).getTestState());
            } else {
                logger.error("Incompatible condition type: " + condition.getClass().getName());
            }
        }
    }
