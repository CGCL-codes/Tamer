    public Patch createNewPatch() {
        byte[] sysex = new byte[297];
        sysex[00] = (byte) 0xF0;
        sysex[01] = (byte) 0x42;
        sysex[2] = (byte) (0x30 + getChannel() - 1);
        sysex[03] = (byte) 0x28;
        sysex[04] = (byte) 0x5A;
        sysex[296] = (byte) 0xF7;
        Patch p = new Patch(sysex, this);
        setPatchName(p, "New Patch");
        calculateChecksum(p);
        return p;
    }
