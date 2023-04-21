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
