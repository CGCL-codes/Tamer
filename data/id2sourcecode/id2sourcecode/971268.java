    private void alterMessage(ShortMessage message, int device, boolean note) {
        if (Game.isGameActive()) {
            if (note) {
                PressedNote[][] noteOns = deviceNoteOns.get(device);
                if (noteOns != null) {
                    PressedNote pressedNote = noteOns[message.getChannel()][message.getData1()];
                    if (pressedNote != null) {
                        pressedNote.alter(getTickPosition(), message.getData2());
                    }
                    noteOns = new PressedNote[16][128];
                    deviceNoteOns.put(device, noteOns);
                }
            } else {
                PressedNote[][] noteOns = deviceNoteOns.get(device);
                if (noteOns != null) {
                    long time = getTickPosition();
                    int velocity = message.getData2();
                    PressedNote[] channelNoteOns = noteOns[message.getChannel()];
                    for (int i = 0; i < channelNoteOns.length; ++i) {
                        if (channelNoteOns[i] != null) {
                            channelNoteOns[i].alter(time, velocity);
                        }
                    }
                }
            }
        }
    }
