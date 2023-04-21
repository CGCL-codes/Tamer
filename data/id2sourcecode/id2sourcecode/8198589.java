    public ChordMessage(ChordMessage copy, int status) throws InvalidMidiDataException {
        this(copy.getPitches(), copy.getChannel(), copy.getVelocity(), copy.getDuration(), status);
    }
