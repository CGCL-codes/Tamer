        public Object getValueAt(int row, int col) {
            Device myDevice = AppConfig.getDevice(row);
            switch(col) {
                case SYNTH_NAME:
                    return myDevice.getSynthName();
                case DEVICE:
                    return (myDevice.getManufacturerName() + " " + myDevice.getModelName());
                case MIDI_IN:
                    if (MidiUtil.isInputAvailable()) {
                        try {
                            int port = multiMIDI ? myDevice.getInPort() : AppConfig.getInitPortIn();
                            return MidiUtil.getInputName(port);
                        } catch (Exception ex) {
                            return "not available";
                        }
                    } else {
                        return "not available";
                    }
                case MIDI_OUT:
                    if (MidiUtil.isOutputAvailable()) {
                        try {
                            int port = multiMIDI ? myDevice.getPort() : AppConfig.getInitPortOut();
                            return MidiUtil.getOutputName(port);
                        } catch (Exception ex) {
                            return "not available";
                        }
                    } else {
                        return "not available";
                    }
                case MIDI_CHANNEL:
                    return new Integer(myDevice.getChannel());
                case MIDI_DEVICE_ID:
                    return new Integer(myDevice.getDeviceID());
                default:
                    return null;
            }
        }
