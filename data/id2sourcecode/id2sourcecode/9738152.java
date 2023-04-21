    protected Patch createNewPatch() {
        byte[] sysex = new byte[Constants.HDR_SIZE + (Constants.SINGLE_PATCH_SIZE * Constants.PATCHES_PER_BANK) + 1];
        System.arraycopy(Constants.BANK_DUMP_HDR_BYTES, 0, sysex, 0, Constants.HDR_SIZE);
        sysex[4] = (byte) getChannel();
        sysex[Constants.HDR_SIZE + (Constants.SINGLE_PATCH_SIZE * Constants.PATCHES_PER_BANK)] = (byte) 0xF7;
        Patch p = new Patch(sysex, this);
        for (int i = 0; i < Constants.PATCHES_PER_BANK; i++) {
            System.arraycopy(Constants.NEW_SINGLE_SYSEX, Constants.HDR_SIZE, p.sysex, getPatchStart(i), Constants.SINGLE_PATCH_SIZE);
            setPatchName(p, i, "New Patch");
        }
        return p;
    }
