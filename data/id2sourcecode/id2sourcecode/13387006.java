    public Patch createNewPatch() {
        byte[] sysex = new byte[((296 * 8) + 406)];
        byte[] sysexHeader = new byte[10];
        sysexHeader[0] = (byte) 0xF0;
        sysexHeader[1] = (byte) 0x00;
        sysexHeader[2] = (byte) 0x20;
        sysexHeader[3] = (byte) 0x29;
        sysexHeader[4] = (byte) 0x01;
        sysexHeader[5] = (byte) 0x21;
        sysexHeader[6] = (byte) (getChannel() - 1);
        sysexHeader[7] = (byte) 0x00;
        sysexHeader[8] = (byte) (0x00);
        Patch p = new Patch(sysex, this);
        for (int i = 0; i < 8; i++) {
            sysexHeader[8] = (byte) i;
            System.arraycopy(sysexHeader, 0, p.sysex, i * 296, 9);
            System.arraycopy(NovationNova1InitPatch.initpatch, 9, p.sysex, (i * 296) + 9, 296 - 9);
        }
        System.arraycopy(NovationNova1InitPatch.initperf, 0, p.sysex, (8 * 296), 406);
        return p;
    }
