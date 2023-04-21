    protected Patch getPatch(Patch bank, int patchNum) {
        byte[] sysex = new byte[singleSize];
        System.arraycopy(Constants.VAMP2_DUMP_HDR_BYTES, 0, sysex, 0, Constants.HDR_SIZE);
        sysex[4] = (byte) getChannel();
        sysex[7] = (byte) patchNum;
        sysex[singleSize - 1] = (byte) 0xF7;
        System.arraycopy(bank.sysex, getPatchStart(patchNum), sysex, Constants.HDR_SIZE, Constants.SINGLE_PATCH_SIZE);
        try {
            return new Patch(sysex, getDevice());
        } catch (Exception e) {
            Logger.reportError("Error", "Error in Bass POD Bank Driver", e);
            return null;
        }
    }
