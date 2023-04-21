    NoteEvent(MidiPart part, MidiEvent startEvent) {
        super(part, startEvent.getTick());
        this.startEvent = startEvent;
        ShortMessage shm = (ShortMessage) startEvent.getMessage();
        note = shm.getData1();
        velocity = shm.getData2();
        channel = shm.getChannel();
        startTick = startEvent.getTick();
    }
