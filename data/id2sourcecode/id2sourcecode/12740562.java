    public void requestPatchDump(final int bankNum, final int patchNum) {
        send(sysexRequestDump.toSysexMessage(getChannel(), 0));
    }
