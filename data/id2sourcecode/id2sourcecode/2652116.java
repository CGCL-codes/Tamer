    public void requestPatchDump(int bankNum, int patchNum) {
        if (patchNum == 100) {
            patchNum = 127;
        }
        send(SYS_REQ.toSysexMessage(getChannel(), new SysexHandler.NameValue("ID", getDeviceID() + 0x1F), new SysexHandler.NameValue("patchNum", patchNum)));
    }
