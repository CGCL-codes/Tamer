        public void send(MidiMessage message, long timeStamp) {
            try {
                midiDevice.getReceiver().send(message, timeStamp);
                if (message instanceof ShortMessage) {
                    ShortMessage shm = (ShortMessage) message;
                    int channel = shm.getChannel();
                    if (shm.getCommand() == ShortMessage.NOTE_ON) {
                    } else if (shm.getCommand() == ShortMessage.CONTROL_CHANGE) {
                        if (gui != null && shm.getData1() == 7) gui.mixerSlots[channel].setVolume(shm.getData2()); else if (gui != null && shm.getData1() == 10) gui.mixerSlots[channel].setPan(shm.getData2());
                    } else if (shm.getCommand() == ShortMessage.PITCH_BEND) {
                    }
                }
            } catch (Exception e) {
            }
        }
