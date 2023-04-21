    public void requestPatchDump(int bankNum, int patchNum) {
        if ((((DX7FamilyDevice) (getDevice())).getSPBPflag() & 0x01) == 1) {
            YamahaDX7SysexHelper.mkSysInfoAvail(this, (byte) (getChannel() + 0x10));
            YamahaDX7SysexHelper.chBank(this, (byte) (getChannel() + 0x10), (byte) (bankNum + 0x25));
            YamahaDX7SysexHelper.chPatch(this, (byte) (getChannel() + 0x10), (byte) (patchNum));
        } else {
            if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) {
                YamahaDX7Strings.dxShowInformation(toString(), YamahaDX7Strings.REQUEST_VOICE_STRING);
            }
        }
    }
