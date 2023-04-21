    public void renderBar(int[] notes, Track track, long startTick, int ppqn) throws InvalidMidiDataException {
        final int channel = getInstrument().getChannel();
        MidiMessage msg;
        for (int i = 0; i < notes.length; i++) {
            int note = notes[i];
            float timeOn = swing(Note.getTime(note));
            int pitch = Note.getPitch(note);
            int level = Note.getLevel(note);
            long onTick = ticks2MidiTicks(timeOn, ppqn);
            msg = ChannelMsg.createChannel(ChannelMsg.NOTE_ON, channel, pitch, level);
            track.add(new MidiEvent(msg, startTick + onTick));
            msg = ChannelMsg.createChannel(ChannelMsg.NOTE_OFF, channel, pitch, 0);
            float timeOff = swing(Note.getTime(note) + Note.getDuration(note));
            long offTick = ticks2MidiTicks(timeOff, ppqn);
            track.add(new MidiEvent(msg, startTick + offTick));
        }
    }
