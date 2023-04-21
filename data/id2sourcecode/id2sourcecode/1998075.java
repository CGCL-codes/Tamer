    public void initActuator() throws OneWireException {
        PotentiometerContainer pc = (PotentiometerContainer) getDeviceContainer();
        int numOfWiperSettings;
        int resistance;
        double offset = 0.6;
        double wiperResistance;
        String selectionString;
        byte[] state = pc.readDevice();
        pc.setCurrentWiperNumber(getChannel(), state);
        pc.writeDevice(state);
        numOfWiperSettings = pc.numberOfWiperSettings(state);
        resistance = pc.potentiometerResistance(state);
        wiperResistance = (double) ((double) (resistance - offset) / (double) numOfWiperSettings);
        selectionString = resistance + " k-Ohms";
        ActuatorSelections.addElement(selectionString);
        for (int i = (numOfWiperSettings - 2); i > -1; i--) {
            double newWiperResistance = (double) (wiperResistance * (double) i);
            int roundedWiperResistance = (int) ((newWiperResistance + offset) * 10000);
            selectionString = (double) ((double) roundedWiperResistance / 10000.0) + " k-Ohms";
            ActuatorSelections.addElement(selectionString);
        }
    }
