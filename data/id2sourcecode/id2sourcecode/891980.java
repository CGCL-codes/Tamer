        public void send(MidiMessage mess, long timeStamp) {
            if (mess.getStatus() >= ShortMessage.MIDI_TIME_CODE) {
                return;
            }
            if (mess instanceof ShortMessage) {
                ShortMessage shm = (ShortMessage) mess;
                int cmd = shm.getCommand();
                if (cmd != 240) {
                    int chn = shm.getChannel();
                    int dat1 = shm.getData1();
                    int dat2 = shm.getData2();
                    if (cmd == 144 && dat2 != 0) {
                        dat2 = 64;
                    }
                    if (cmd == 128) {
                        dat2 = 0;
                    }
                    if (cmd == 144 || cmd == 128) {
                        int status = shm.getStatus();
                        try {
                            shm.setMessage(status, dat1, dat2);
                        } catch (InvalidMidiDataException ex) {
                            Logger.getLogger(TestToot.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    System.out.println(" cmd:" + cmd + " chn:" + chn + " data:" + dat1 + " " + dat2);
                }
            }
            chained.send(mess, -1);
        }
