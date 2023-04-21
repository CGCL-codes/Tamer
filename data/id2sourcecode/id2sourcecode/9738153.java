    public void requestPatchDump(int bankNum, int patchNum) {
        int channel = getChannel();
        send(SYS_REQ.toSysexMessage(channel, new SysexHandler.NameValue("channel", channel)));
    }
