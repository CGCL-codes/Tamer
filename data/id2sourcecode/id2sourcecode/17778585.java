    public void requestPatchDump(int bankNum, int patchNum) {
        byte[] sysex = SYSEX_REQUEST_DUMP.toByteArray(getChannel(), patchNum);
        calculateChecksum(sysex, 6, sysex.length - 3, sysex.length - 2);
        send(sysex);
    }
