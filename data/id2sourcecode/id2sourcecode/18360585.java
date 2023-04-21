    public void requestPatchDump(int bankNum, int patchNum) {
        SysexHandler.NameValue nv[] = new SysexHandler.NameValue[2];
        nv[0] = new SysexHandler.NameValue("bankNum", bankNum);
        nv[1] = new SysexHandler.NameValue("patchNum", patchNum);
        send(sysexRequestDump.toSysexMessage(getChannel(), nv));
    }
