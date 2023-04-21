    public void requestPatchDump(int bankNum, int patchNum) {
        send(SYSEX_REQUEST_DUMP[patchNum].toSysexMessage(getChannel() + 0x20));
    }
