    public void requestPatchDump(int bankNum, int patchNum) {
        int progNum = bankNum * 4 + patchNum;
        send(SYS_REQ.toSysexMessage(getChannel(), new SysexHandler.NameValue("progNum", progNum)));
    }
