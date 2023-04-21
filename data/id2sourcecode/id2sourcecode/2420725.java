    public String getDeviceName() {
        String di = "";
        try {
            di = MidiUtil.getOutputName(getPort());
        } catch (Exception ex) {
        }
        return getManufacturerName() + " " + getModelName() + " <" + getSynthName() + ">  -  MIDI Out Port: " + ((di == "") ? "None" : di) + "  -  MIDI Channel: " + getChannel();
    }
