        public void send(MidiMessage message, long timeStamp) {
            try {
                if (ShortMessage.class.isInstance(message)) {
                    ShortMessage shm = (ShortMessage) message;
                    int synthIndex = shm.getChannel();
                    MidiChannel channel = midiChannels[shm.getChannel()];
                    if (channel != null) {
                        if (shm.getCommand() == ShortMessage.NOTE_ON) {
                            if (shm.getData2() == 0) channel.noteOff(shm.getData1()); else if (!channel.getMute()) channel.noteOn(shm.getData1(), shm.getData2());
                        } else if (shm.getCommand() == ShortMessage.NOTE_OFF) channel.noteOff(shm.getData1()); else if (shm.getCommand() == ShortMessage.CONTROL_CHANGE) {
                            channel.controlChange(shm.getData1(), shm.getData2());
                            if (gui != null && shm.getData1() == 7) gui.synthStrips[synthIndex].setVolume(shm.getData2()); else if (gui != null && shm.getData1() == 10) gui.synthStrips[synthIndex].setPan(shm.getData2());
                        } else if (shm.getCommand() == ShortMessage.PITCH_BEND) channel.setPitchBend((0xff & shm.getData1()) + ((0xff & shm.getData2()) << 7));
                    }
                    if (shm.getCommand() == ShortMessage.PROGRAM_CHANGE) {
                        midiChannels[shm.getChannel()] = synths[shm.getData1()];
                    }
                } else if (MetaMessage.class.isInstance(message)) {
                    byte[] msgBytes = message.getMessage();
                    if (msgBytes[0] == -1 && msgBytes[1] == 0x51 && msgBytes[2] == 3) {
                        int mpq = ((msgBytes[3] & 0xff) << 16) | ((msgBytes[4] & 0xff) << 8) | (msgBytes[5] & 0xff);
                        tempoBPM = ((60000000f / mpq));
                        java.util.logging.Logger.getLogger(this.getClass().getName()).fine("Tempo set to " + tempoBPM + " bpm");
                    }
                }
            } catch (Exception e) {
            }
        }
