    public Color getRowColor(int row, Color background) {
        MidiTrack track = sequence.getMidiTrack(row);
        Float fhue = getHue(track);
        if (track.getChannel() < 0 || fhue == null) return background;
        return MidiColor.asHSB(fhue, 0.25f, 1.0f);
    }
