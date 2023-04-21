    public void send(final MidiMessage message, final long deltaTime) {
        final ShortMessage shortMessage = (ShortMessage) message;
        final int midiChannel = shortMessage.getChannel();
        if (midiIns[midiChannel] == null) return;
        final int midiCommand = shortMessage.getCommand();
        final int midiData1 = shortMessage.getData1();
        final int midiData2 = shortMessage.getData2();
        if (midiCommand == MidiEvent.NOTE_ON && midiData2 > 0) {
            final Note note = new Note(midiData1, midiData2);
            midiIns[midiChannel].sendNoteOn(note, deviceNumber, midiChannel);
        } else if (midiCommand == MidiEvent.NOTE_OFF || midiData2 == 0) {
            final Note note = new Note(midiData1, midiData2);
            midiIns[midiChannel].sendNoteOff(note, deviceNumber, midiChannel);
        } else if (midiCommand == MidiEvent.CONTROL_CHANGE) {
            final Controller controller = new Controller(midiData1, midiData2);
            midiIns[midiChannel].sendController(controller, deviceNumber, midiChannel);
        } else if (midiCommand == MidiEvent.PROGRAM_CHANGE) {
            final ProgramChange programChange = new ProgramChange(midiData1);
            midiIns[midiChannel].sendProgramChange(programChange, deviceNumber, midiChannel);
        }
    }
