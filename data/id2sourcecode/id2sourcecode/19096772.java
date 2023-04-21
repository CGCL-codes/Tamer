    public DualPortLutWriter(MemoryBank memory) {
        super(memory);
        assert !mpA.writes() || !mpB.writes() : "Lut based dual ports must have at least 1 port that is read only";
    }
