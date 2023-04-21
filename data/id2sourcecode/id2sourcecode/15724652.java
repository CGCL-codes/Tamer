    public void requestPatchDump(int bankNum, int patchNum) {
        if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) YamahaTX802Strings.dxShowInformation(toString(), YamahaTX802Strings.MICRO_TUNING_CARTRIDGE_STRING);
        send(sysexRequestDump.toSysexMessage(getChannel() + 0x20));
    }
