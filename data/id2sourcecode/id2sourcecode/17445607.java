            public void send(MidiMessage message, long timeStamp) {
                if (targetDataLine.isOpen() && ShortMessage.class.isInstance(message)) {
                    ShortMessage shm = (ShortMessage) message;
                    int channelNo = shm.getChannel();
                    MidiChannel channel = channelNo >= 0 && channelNo <= 15 ? midiChannels[channelNo] : null;
                    switch(shm.getCommand()) {
                        case ShortMessage.PROGRAM_CHANGE:
                            channel.programChange(shm.getData1());
                            break;
                        case ShortMessage.NOTE_ON:
                            channel.noteOn(shm.getData1(), shm.getData2());
                            break;
                        case ShortMessage.NOTE_OFF:
                            channel.noteOff(shm.getData1());
                            break;
                        case ShortMessage.PITCH_BEND:
                            channel.setPitchBend((0xff & shm.getData1()) + ((0xff & shm.getData2()) << 7));
                            break;
                    }
                }
            }
