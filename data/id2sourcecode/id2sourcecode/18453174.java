    public void requestPatchDump(int bankNum, int patchNum) {
        send(sysexRequestDump.toSysexMessage(getChannel() + 0x20, new SysexHandler.NameValue("bankNum", bankNum >> 2), new SysexHandler.NameValue("patchNum", (bankNum & 3) * 16 + patchNum)));
    }
