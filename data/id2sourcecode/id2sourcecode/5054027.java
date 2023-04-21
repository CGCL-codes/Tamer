    public Object getValueAt(int trk, int col) {
        MidiTrack track = getSequence().getMidiTrack(trk);
        TrackControls controls = sequencer.getTrackControls(track);
        switch(col) {
            case MUTE:
                return controls == null ? false : Boolean.valueOf(controls.isMute());
            case SOLO:
                return controls == null ? false : Boolean.valueOf(controls.isSolo());
            case TRACK:
                return track.getTrackName();
            case PROGRAM:
                return getProgramName(trk);
            case BANK:
                int bank = getBank(trk);
                if (bank < 0) return "";
                return 1 + bank;
            case CHANNEL:
                return 1 + track.getChannel();
            default:
                break;
        }
        return "?";
    }
