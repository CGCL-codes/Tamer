        public void send(MidiMessage mess, long timeStamp) {
            if (mess.getStatus() >= ShortMessage.MIDI_TIME_CODE) {
                return;
            }
            if (mess instanceof ShortMessage) {
                ShortMessage shm = (ShortMessage) mess;
                if (isLinux) {
                    if (shm.getStatus() == ShortMessage.PITCH_BEND) {
                        short low = (byte) shm.getData1();
                        short high = (byte) shm.getData2();
                        int channel = shm.getChannel();
                        low = (byte) shm.getData1();
                        high = (byte) shm.getData2();
                        high = (short) ((high + 64) & 0x007f);
                        try {
                            shm.setMessage(ShortMessage.PITCH_BEND, channel, low, high);
                        } catch (InvalidMidiDataException e) {
                            e.printStackTrace();
                        }
                    }
                }
                int cmd = shm.getCommand();
                if (cmd != 240) {
                    int chn = shm.getChannel();
                    int dat1 = shm.getData1();
                    int dat2 = shm.getData2();
                    System.out.println(" cmd:" + cmd + " chn:" + chn + " data:" + dat1 + " " + dat2);
                }
            }
            chained.send(mess, -1);
        }
