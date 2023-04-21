    public void requestPatchDump(int bankNum, int patchNum) {
        if ((((DX7FamilyDevice) (getDevice())).getSPBPflag() & 0x01) == 1) {
            YamahaDX7SysexHelper.mkSysInfoAvail(this, (byte) (getChannel() + 0x10));
            YamahaDX7SysexHelper.xmitBankDump(this, (byte) (getChannel() + 0x10));
        } else {
            if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) {
                YamahaDX7Strings.dxShowInformation(toString(), YamahaDX7Strings.REQUEST_VOICE_STRING);
            }
        }
    }
