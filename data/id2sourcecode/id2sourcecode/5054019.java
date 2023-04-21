    public Color getSelectedColor(int row, Color color) {
        MidiTrack track = sequence.getMidiTrack(row);
        Float fhue = getHue(track);
        if (track.getChannel() < 0 || fhue == null) return color;
        return MidiColor.asHSB(fhue, 0.5f, 0.85f);
    }
