    public void requestPatchDump(int bankNum, int patchNum) {
        send(SYSEX_RECALL_PRESET.toSysexMessage(getChannel(), patchNum));
        send(SYSEX_REQUEST_EDIT_BUFFER.toSysexMessage(getChannel()));
    }
