    public void actionSucceeded(String deviceName, ActionResult readResult) {
        Target device = this.devices.get(deviceName);
        try {
            device.write_attribute(readResult.getAttributesValue(), this.newValue);
            readResult.setNewValue(this.newValue);
            super.actionSucceeded(deviceName, readResult);
        } catch (Exception e) {
            super.actionFailed(deviceName, readResult, e);
        }
    }
