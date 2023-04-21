    public PetiteMultiClockPipe(int dataPathSize, EntryMethod writeSide, EntryMethod readSide) {
        this(dataPathSize, writeSide.getClockPin(), readSide.getClockPin());
    }
