    public void requestPatchDump(int bankNum, int patchNum) {
        if (bankNum == 0) send(SYSEX_REQUEST_A_DUMP.toSysexMessage(getChannel())); else send(SYSEX_REQUEST_D_DUMP.toSysexMessage(getChannel()));
    }
