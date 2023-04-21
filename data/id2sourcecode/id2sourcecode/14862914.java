    public void requestPatchDump(int bankNum, int patchNum) {
        for (int i = 0; i < NUM_IN_BANK; i++) {
            send(sysexRequestDump.toSysexMessage(((byte) getChannel()), new SysexHandler.NameValue[] { new SysexHandler.NameValue("bankNum", bankNum), new SysexHandler.NameValue("patchNum", i) }));
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                ErrorMsg.reportStatus(e);
                ErrorMsg.reportError("Error", "Unable to request Patch " + i);
            }
        }
    }
