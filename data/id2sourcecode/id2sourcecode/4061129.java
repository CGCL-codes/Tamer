    @Override
    protected void buttonClicked(String buttonId) {
        logger.debug("Received notification of a button click: " + buttonId);
        if (buttonId.equals(AbstractEditorPanel.ACTION_SAVE)) {
            ValueSensor sensor1 = ((CustomValueSensorListItem) testSensor1_cb.getSelectedItem()).getSensor();
            ValueSensor sensor2 = ((CustomValueSensorListItem) testSensor2_cb.getSelectedItem()).getSensor();
            String conditionName = conditionName_tf.getText();
            if (conditionName == null || conditionName.length() <= 0) {
                UIHelper.showErrorDialog(panelGroupManager.getWindowWrapper().getContainer(), "Error", "<html>The condition name may not be blank.<br>Please enter a valid condition name.</html>");
                return;
            }
            String testValueString = testValue_tf.getText();
            if (testValueString == null || testValueString.length() <= 0) {
                UIHelper.showErrorDialog(panelGroupManager.getWindowWrapper().getContainer(), "Error", "<html>The difference value may not be blank.<br>Please enter a difference value.</html>");
                return;
            }
            ValueData testValue = new ValueData(Double.parseDouble(testValueString), (Unit) availableUnits_cb.getSelectedItem());
            DifferenceValueCondition dvc = this.getUserObject();
            if (dvc == null) {
                dvc = new DifferenceValueCondition(conditionName, sensor1, ((Channel) this.testSensor1Channel_cb.getSelectedItem()).getChannelNum(), sensor2, ((Channel) this.testSensor2Channel_cb.getSelectedItem()).getChannelNum(), testValue, (ValueConditionOperator) testOperators_cb.getSelectedItem());
            } else {
                dvc.setTestValue(testValue);
                dvc.setTestOperator((ValueConditionOperator) testOperators_cb.getSelectedItem());
            }
            new StoreConditionAction(dvc, this.serverContext).runWithProgressWindow();
            panelGroupManager.requestWindowDispose();
        }
    }
