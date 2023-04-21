    private void createTracksFromNotes(ArrayList<MidiChange> midiChanges) throws InvalidMidiDataException {
        Track[] tracks = new Track[30];
        int channel = 0;
        int pitch = 0;
        int loudness = 0;
        int onset = 0;
        int track = 0;
        for (MidiChange midiChange : midiChanges) {
            onset = midiChange.getOnset();
            pitch = midiChange.getPitch();
            loudness = midiChange.getLoudness();
            channel = midiChange.getChannel();
            track = midiChange.getTrack();
            ShortMessage msg = new ShortMessage();
            switch(midiChange.getType()) {
                case MidiChange.NOTE_ON:
                    {
                        msg.setMessage(ShortMessage.NOTE_ON, channel, pitch, loudness);
                        break;
                    }
                case MidiChange.NOTE_OFF:
                    {
                        msg.setMessage(ShortMessage.NOTE_OFF, channel, pitch, loudness);
                        break;
                    }
                case MidiChange.CONTROL_CHANGE:
                    {
                        msg.setMessage(ShortMessage.CONTROL_CHANGE, channel, pitch, loudness);
                        break;
                    }
            }
            loudness = Math.max(0, Math.min(loudness, 127));
            pitch = Math.max(0, Math.min(pitch, 127));
            if (tracks[track] == null) {
                tracks[track] = sequence.createTrack();
            }
            tracks[track].add(new MidiEvent(msg, onset));
        }
        if (tracks[0] == null) {
            tracks[0] = sequence.createTrack();
        }
        ShortMessage msg = new ShortMessage();
        msg.setMessage(ShortMessage.STOP);
        tracks[0].add(new MidiEvent(msg, onset + 1500));
    }
