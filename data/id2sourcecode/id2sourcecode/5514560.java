    public void storePatch(Patch p, int bankNum, int patchNum) {
        if ((((DX7FamilyDevice) (getDevice())).getSwOffMemProtFlag() & 0x01) == 1) {
            send(YamahaTX7SysexHelper.swOffMemProt.toSysexMessage(getChannel() + 0x10));
        } else {
            if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) {
                YamahaTX7Strings.dxShowInformation(toString(), YamahaTX7Strings.MEMORY_PROTECTION_STRING);
            }
        }
        sendPatchWorker(p);
    }
