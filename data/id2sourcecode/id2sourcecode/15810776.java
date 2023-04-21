    public String readSensor() throws OneWireException {
        String returnString = "";
        byte[] switchState;
        int switchChannel = getChannel();
        SwitchContainer Container;
        Container = (SwitchContainer) DeviceContainer;
        if (Container.hasLevelSensing()) {
            switchState = Container.readDevice();
            if (Container.getLevel(switchChannel, switchState)) {
                returnString = getMax();
            } else {
                returnString = getMin();
            }
        }
        return returnString;
    }
