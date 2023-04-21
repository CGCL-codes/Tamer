    void interceptMessage(MidiMessage msg) {
        try {
            ShortMessage shm = (ShortMessage) msg;
            if (shm.getCommand() == ShortMessage.NOTE_ON) {
                if (shm.getData2() == 0) {
                    pendingNoteOffs.remove(new Integer(shm.getChannel() << 8 | shm.getData1()));
                } else pendingNoteOffs.add(new Integer(shm.getChannel() << 8 | shm.getData1()));
            }
        } catch (Exception e) {
        }
    }
