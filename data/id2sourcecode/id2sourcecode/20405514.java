    public void requestPatchDump(int bankNum, int patchNum) {
        setPatchNum(patchNum);
        send(sysexRequestDump.toSysexMessage(getChannel() + 0x20));
    }
