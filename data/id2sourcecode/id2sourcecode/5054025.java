    public String getProgramName(int row) {
        MidiTrack track = sequence.getMidiTrack(row);
        if (track.getChannel() < 0) return "";
        int prg = getProgram(row);
        if (prg < 0) return "";
        String name = isDrumTrack(row) ? GM.drumProgramName(prg) : GM.melodicProgramName(prg);
        return (1 + prg) + " " + name;
    }
