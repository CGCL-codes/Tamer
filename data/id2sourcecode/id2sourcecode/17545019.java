    public void initActuator() throws OneWireException {
        SwitchContainer switchcontainer = (SwitchContainer) getDeviceContainer();
        ActuatorSelections.addElement(getMin());
        ActuatorSelections.addElement(getMax());
        int initValue;
        int channelValue;
        int switchStateIntValue = 0;
        Integer init = new Integer(getInit());
        initValue = init.intValue();
        channelValue = getChannel();
        byte[] state = switchcontainer.readDevice();
        boolean switch_state = switchcontainer.getLatchState(channelValue, state);
        if (switch_state) switchStateIntValue = 1; else switchStateIntValue = 0;
        if (initValue != switchStateIntValue) {
            switchcontainer.setLatchState(channelValue, !switch_state, false, state);
            switchcontainer.writeDevice(state);
        }
    }
