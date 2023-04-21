    public Patch getPatch(Patch bank, int patchNum) {
        try {
            byte[] sysex = new byte[264];
            sysex[0] = (byte) 0xF0;
            sysex[1] = (byte) 0x44;
            sysex[2] = (byte) 0x00;
            sysex[3] = (byte) 0x00;
            sysex[4] = (byte) (0x70 + getChannel() - 1);
            sysex[5] = (byte) 0x20;
            sysex[6] = (byte) (0x60);
            sysex[263] = (byte) 0xF7;
            System.arraycopy(((Patch) bank).sysex, getPatchStart(patchNum), sysex, 7, 264 - 7);
            Patch p = new Patch(sysex, getDevice());
            p.calculateChecksum();
            return p;
        } catch (Exception e) {
            ErrorMsg.reportError("Error", "Error in Nova1 Bank Driver", e);
            return null;
        }
    }
