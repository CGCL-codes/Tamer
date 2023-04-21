    public void requestPatchDump(int bankNum, int patchNum) {
        YamahaTX802SysexHelpers.chBlock(this, (byte) (getChannel() + 0x10), (byte) (bankNum));
        send(sysexRequestDump.toSysexMessage(getChannel() + 0x20));
    }
