    public MidiInToBrain(final Receiver thru, final List<Connection> inputs) {
        recv = new Receiver() {

            @Override
            public void send(MidiMessage message, long timeStamp) {
                if (thru != null) thru.send(message, -1);
                if (message instanceof ShortMessage) {
                    ShortMessage shm = (ShortMessage) message;
                    if (shm.getChannel() == 9) {
                        if (shm.getData2() != 0) {
                        }
                    }
                }
                if (message instanceof ShortMessage) {
                    ShortMessage shm = (ShortMessage) message;
                    int cmd = shm.getCommand();
                    if (cmd == ShortMessage.NOTE_ON || cmd == ShortMessage.NOTE_OFF) {
                        int pitch = shm.getData1();
                        int vel = shm.getData2();
                        pitch = pitch - 36;
                        if (pitch >= inputs.size() || pitch < 0) {
                            return;
                        }
                        try {
                            inputs.get(pitch).excite(vel / 128.0f);
                        } catch (BrainException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void close() {
            }
        };
    }
