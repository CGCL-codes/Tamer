    static TreeSet<MyNote> readFile(String name) throws InvalidMidiDataException, IOException {
        File file = new File(name);
        TreeSet<MyNote> notes = new TreeSet<MyNote>();
        Sequence seq = MidiSystem.getSequence(file);
        for (Track track : seq.getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                if (event.getMessage() instanceof ShortMessage) {
                    ShortMessage shm = (ShortMessage) event.getMessage();
                    int cmd = shm.getCommand();
                    long tick = event.getTick();
                    if (cmd == ShortMessage.NOTE_ON || cmd == ShortMessage.NOTE_OFF) {
                        int chan = shm.getChannel();
                        int pitch = shm.getData1();
                        int vel = shm.getData2();
                        notes.add(new MyNote(chan, pitch, vel, tick));
                    }
                }
            }
        }
        return notes;
    }
