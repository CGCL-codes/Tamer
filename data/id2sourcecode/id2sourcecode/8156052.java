    public void requestPatchDump(int bankNum, int patchNum) {
        if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) YamahaDX7IIStrings.dxShowInformation(toString(), YamahaDX7IIStrings.MICRO_TUNING_CARTRIDGE_STRING);
        send(sysexRequestDump.toSysexMessage(getChannel() + 0x20));
    }
