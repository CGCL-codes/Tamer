    public void storePatch(Patch p, int bankNum, int patchNum) {
        if ((((DX7FamilyDevice) (getDevice())).getSwOffMemProtFlag() & 0x01) == 1) {
            YamahaTX802SysexHelpers.swOffMemProt(this, (byte) (getChannel() + 0x10));
        } else {
            if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) {
                YamahaTX802Strings.dxShowInformation(toString(), YamahaTX802Strings.MEMORY_PROTECTION_STRING);
            }
        }
        YamahaTX802SysexHelpers.chBlock(this, (byte) (getChannel() + 0x10), (byte) (bankNum));
        sendPatchWorker(p);
    }
