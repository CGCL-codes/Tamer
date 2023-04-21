    public Object getValueAt(int row, int col) {
        Device myDevice = (Device) PatchEdit.appConfig.getDevice(row);
        switch(col) {
            case SYNTH_NAME:
                return myDevice.getSynthName();
            case DEVICE:
                return (myDevice.getManufacturerName() + " " + myDevice.getModelName());
            case MIDI_IN:
                if (PatchEdit.newMidiAPI) {
                    return MidiUtil.isInputAvailable() ? MidiUtil.getInputMidiDeviceInfo(myDevice.getInPort()).getName() : "not available";
                } else {
                    try {
                        return (myDevice.getInPort() + ": " + PatchEdit.MidiOut.getInputDeviceName(myDevice.getInPort()));
                    } catch (Exception e) {
                        return "-";
                    }
                }
            case MIDI_OUT:
                if (PatchEdit.newMidiAPI) {
                    return MidiUtil.isOutputAvailable() ? MidiUtil.getOutputMidiDeviceInfo(myDevice.getPort()).getName() : "not available";
                } else {
                    try {
                        return (myDevice.getPort() + ": " + PatchEdit.MidiOut.getOutputDeviceName(myDevice.getPort()));
                    } catch (Exception e) {
                        return "-";
                    }
                }
            case MIDI_CHANNEL:
                return new Integer(myDevice.getChannel());
            case MIDI_DEVICE_ID:
                return new Integer(myDevice.getDeviceID());
            default:
                return null;
        }
    }
