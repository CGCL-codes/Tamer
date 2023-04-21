    protected ShortMessage newControlChange(IPatchDriver driver, int controlNumber, int value) throws InvalidMidiDataException {
        ShortMessage ccMessage = new ShortMessage();
        ccMessage.setMessage(ShortMessage.CONTROL_CHANGE, driver.getDevice().getChannel() - 1, controlNumber, value);
        return ccMessage;
    }
