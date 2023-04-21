    public void setTrackInstrument(int track, Instrument instr) throws NoChannelAssignedException {
        int program = instr.getPatch().getProgram();
        int bank = instr.getPatch().getProgram();
        int channel = getTrackChannel(track);
        Track t = getTrack(track);
        Vector<MidiEvent> events = ProjectHelper.filterShortMessages(t, ShortMessage.PROGRAM_CHANGE);
        ShortMessage sm = new ShortMessage();
        try {
            sm.setMessage(ShortMessage.PROGRAM_CHANGE, channel, program, bank);
        } catch (InvalidMidiDataException e) {
        }
        if (events.size() == 0) {
            MidiEvent e = new MidiEvent(sm, 0);
            t.add(e);
        } else {
            for (MidiEvent e : events) {
                t.remove(e);
                t.add(new MidiEvent(sm, e.getTick()));
            }
        }
        synthesizer.getChannels()[channel].programChange(bank, program);
        reloadSequence();
        this.notifyObservers(Action.TRACK_PARAMETERS_EDITED);
    }
