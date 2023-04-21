    public void requestPatchDump(int bankNum, int patchNum) {
        if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) YamahaDX7IIStrings.dxShowInformation(toString(), YamahaDX7IIStrings.FRACTIONAL_SCALING_CARTRIDGE_STRING);
        YamahaDX7IISysexHelpers.chVoiceMode(this, (byte) (getChannel() + 0x10));
        setPatchNum(patchNum + 32 * bankNum);
        send(sysexRequestDump.toSysexMessage(getChannel() + 0x20));
    }
