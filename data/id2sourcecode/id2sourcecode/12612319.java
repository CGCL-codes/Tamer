    public JSLFrame editPatch(Patch p) {
        if ((((DX7FamilyDevice) (getDevice())).getSPBPflag() & 0x01) == 1) {
            YamahaDX7SysexHelper.mkSysInfoAvail(this, (byte) (getChannel() + 0x10));
        } else {
            if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) YamahaDX7Strings.dxShowInformation(toString(), YamahaDX7Strings.PERFORMANCE_EDITOR_STRING);
        }
        return super.editPatch(p);
    }
