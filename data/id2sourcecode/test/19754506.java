    public void requestPatchDump(int bankNum, int patchNum) {
        if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) YamahaDX7sStrings.dxShowInformation(toString(), YamahaDX7sStrings.MICRO_TUNING_CARTRIDGE_STRING);
        send(sysexRequestDump.toSysexMessage(getChannel() + 0x20));
    }
