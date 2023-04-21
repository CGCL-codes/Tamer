    public Patch createNewPatch() {
        byte[] sysex = new byte[(264 * 16)];
        byte[] sysexHeader = new byte[7];
        sysexHeader[0] = (byte) 0xF0;
        sysexHeader[1] = (byte) 0x44;
        sysexHeader[2] = (byte) 0x00;
        sysexHeader[3] = (byte) 0x00;
        sysexHeader[4] = (byte) (0x70 + getChannel() - 1);
        sysexHeader[5] = (byte) 0x20;
        sysexHeader[6] = (byte) 0x20;
        Patch p = new Patch(sysex, this);
        for (int i = 0; i < 16; i++) {
            sysexHeader[6] = (byte) (0x20 + i);
            System.arraycopy(sysexHeader, 0, p.sysex, i * 264, 7);
            p.sysex[(263 * (i + 1))] = (byte) 0xF7;
        }
        calculateChecksum(p);
        return p;
    }
