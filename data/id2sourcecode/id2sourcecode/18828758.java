    public void requestPatchDump(int bankNum, int patchNum) {
        send(SysexRequestDump.toSysexMessage(getChannel(), new SysexHandler.NameValue("bankNum", (bankNum * 100 + patchNum) % 128), new SysexHandler.NameValue("patchNum", (bankNum * 100 + patchNum) / 128)));
    }
