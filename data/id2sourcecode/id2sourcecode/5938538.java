    public void requestPatchDump(int bankNum, int patchNum) {
        int progNum = 0;
        send(SYS_REQ.toSysexMessage(getChannel(), new SysexHandler.NameValue("progNum", progNum)));
    }
