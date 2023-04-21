    public void requestPatchDump(int bankNum, int patchNum) {
        patchNum += 96;
        send(SYS_REQ.toSysexMessage(getChannel(), new SysexHandler.NameValue("channel", getChannel()), new SysexHandler.NameValue("patchNum", patchNum)));
    }
