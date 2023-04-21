    public String readSensor() throws OneWireException {
        String returnString = "";
        byte[] switchState;
        SwitchContainer Container;
        Container = (SwitchContainer) DeviceContainer;
        if (Container.hasActivitySensing()) {
            switchState = Container.readDevice();
            if (Container.getSensedActivity(getChannel(), switchState)) {
                returnString = getMax();
                Container.clearActivity();
                switchState = Container.readDevice();
            }
        } else {
            returnString = "";
        }
        return returnString;
    }
