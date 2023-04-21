    public void copyFrom(QuantitationType qt) {
        quanType = qt;
        String className = qt.getClass().getName();
        className = className.substring(className.lastIndexOf('.') + 1, className.length());
        typeCombo.setSelectedItem(className);
        nameText.setText(qt.getName());
        if (qt.getChannel() != null) {
            channelCombo.setSelectedItem(qt.getChannel().getName());
        } else {
            channelCombo.setSelectedItem("N/A");
        }
        backgroundCBox.setSelected(qt.getIsBackground());
        scaleCombo.setSelectedItem(qt.getScale().getValue());
        dTypeCombo.setSelectedItem(qt.getDataType().getValue());
        confIndText.setValue(0);
    }
