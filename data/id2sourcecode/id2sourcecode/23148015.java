            @Override
            public void send(MidiMessage message, long timeStamp) {
                ShortMessage shm = (ShortMessage) message;
                if (shm.getCommand() == ShortMessage.NOTE_ON && shm.getData2() > 0) {
                    channels[shm.getChannel()].noteOn(shm.getData1(), shm.getData2());
                } else if (shm.getCommand() == ShortMessage.NOTE_ON && shm.getData2() == 0) {
                    channels[shm.getChannel()].noteOff(shm.getData1());
                } else if (shm.getCommand() == ShortMessage.CONTROL_CHANGE) {
                    channels[shm.getChannel()].controlChange(shm.getData1(), shm.getData2());
                } else if (shm.getCommand() == ShortMessage.PITCH_BEND) {
                    channels[shm.getChannel()].setPitchBend((0xff & shm.getData1()) + ((0xff & shm.getData2()) << 7));
                }
            }
