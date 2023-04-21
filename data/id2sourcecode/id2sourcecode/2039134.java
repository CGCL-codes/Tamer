    public String getDeviceName() {
        String di = MidiUtil.getOutputDeviceName(getPort());
        return getManufacturerName() + " " + getModelName() + " <" + getSynthName() + ">  -  MIDI Out Port: " + ((di == "") ? "None" : di) + "  -  MIDI Channel: " + getChannel();
    }
