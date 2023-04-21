        @Override
        public Object getValueAt(int row, int col) {
            Device dev = appConfig.getDevice(row);
            switch(col) {
                case SYNTH_NAME:
                    return dev.getSynthName();
                case DEVICE:
                    return dev.getManufacturerName() + " " + dev.getModelName();
                case MIDI_IN:
                    if (MidiUtil.isInputAvailable()) {
                        try {
                            int port = multiMIDI ? dev.getInPort() : appConfig.getInitPortIn();
                            return MidiUtil.getInputDeviceName(port);
                        } catch (Exception ex) {
                            return "not available";
                        }
                    } else {
                        return "not available";
                    }
                case MIDI_OUT:
                    if (MidiUtil.isOutputAvailable()) {
                        try {
                            int port = multiMIDI ? dev.getPort() : appConfig.getInitPortOut();
                            return MidiUtil.getOutputDeviceName(port);
                        } catch (Exception ex) {
                            return "not available";
                        }
                    } else {
                        return "not available";
                    }
                case MIDI_CHANNEL:
                    return Integer.valueOf(dev.getChannel());
                case MIDI_DEVICE_ID:
                    return Integer.valueOf(dev.getDeviceID());
                default:
                    throw new IndexOutOfBoundsException();
            }
        }
