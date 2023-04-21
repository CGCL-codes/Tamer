    DrumMapper() {
        int i = 0;
        for (; i < 128; i++) {
            NoteMap n = noteMap[i] = new NoteMap();
            n.note = i;
        }
        receiver = new Receiver() {

            public void send(MidiMessage message, long timeStamp) {
                try {
                    if (message instanceof ShortMessage) {
                        ShortMessage shm = (ShortMessage) message;
                        if (shm.getCommand() == ShortMessage.NOTE_ON) {
                            int note = shm.getData1();
                            Receiver recv = defRecv;
                            if (recv == null) return;
                            int noteByte = noteMap[note].note;
                            shm.setMessage(shm.getCommand(), shm.getChannel(), noteByte, shm.getData2());
                            recv.send(shm, timeStamp);
                            return;
                        }
                    }
                    if (defRecv != null) defRecv.send(message, timeStamp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void close() {
            }
        };
        receivers = new ArrayList<Receiver>();
        receivers.add(receiver);
    }
