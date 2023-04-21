    public void setSelection(String selection) throws OneWireException {
        PotentiometerContainer pc = (PotentiometerContainer) getDeviceContainer();
        int Index = 0;
        Index = ActuatorSelections.indexOf(selection);
        byte[] state = pc.readDevice();
        pc.setCurrentWiperNumber(getChannel(), state);
        pc.writeDevice(state);
        if (Index > -1) {
            state = pc.readDevice();
            pc.setWiperPosition(Index);
            pc.writeDevice(state);
        }
    }
