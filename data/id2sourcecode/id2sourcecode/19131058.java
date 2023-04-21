    public Patch createNewPatch() {
        byte[] sysex = new byte[296];
        System.arraycopy(NovationNova1InitPatch.initpatch, 0, sysex, 0, 296);
        sysex[6] = (byte) (getChannel() - 1);
        Patch p = new Patch(sysex, this);
        calculateChecksum(p);
        return p;
    }
