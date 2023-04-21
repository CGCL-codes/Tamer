    public void setSelection(String selection) throws OneWireException {
        SwitchContainer switchcontainer = (SwitchContainer) getDeviceContainer();
        int Index = 0;
        int channelValue = getChannel();
        Index = ActuatorSelections.indexOf(selection);
        boolean switch_state = false;
        if (Index > -1) {
            if (Index > 0) switch_state = true;
            byte[] state = switchcontainer.readDevice();
            switchcontainer.setLatchState(channelValue, switch_state, false, state);
            switchcontainer.writeDevice(state);
        }
    }
