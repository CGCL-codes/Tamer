    public void storePatch(Patch p, int bankNum, int patchNum) {
        if ((((DX7FamilyDevice) (getDevice())).getSwOffMemProtFlag() & 0x01) == 1) {
            YamahaDX7SysexHelper.swOffMemProt(this, (byte) (getChannel() + 0x10), (byte) (bankNum + 0x21), (byte) (bankNum + 0x25));
        } else {
            if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) YamahaDX7Strings.dxShowInformation(toString(), YamahaDX7Strings.MEMORY_PROTECTION_STRING);
        }
        if ((((DX7FamilyDevice) (getDevice())).getSPBPflag() & 0x01) == 1) {
            YamahaDX7SysexHelper.mkSysInfoAvail(this, (byte) (getChannel() + 0x10));
            sendPatchWorker(p);
            YamahaDX7SysexHelper.chBank(this, (byte) (getChannel() + 0x10), (byte) (bankNum + 0x25));
            send(YamahaDX7SysexHelper.depressStore.toSysexMessage(getChannel() + 0x10));
            YamahaDX7SysexHelper.chPatch(this, (byte) (getChannel() + 0x10), (byte) (patchNum));
            send(YamahaDX7SysexHelper.releaseStore.toSysexMessage(getChannel() + 0x10));
        } else {
            if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) YamahaDX7Strings.dxShowInformation(toString(), YamahaDX7Strings.RECEIVE_STRING);
            sendPatchWorker(p);
            if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) YamahaDX7Strings.dxShowInformation(toString(), YamahaDX7Strings.STORE_SINGLE_VOICE_STRING);
        }
    }
