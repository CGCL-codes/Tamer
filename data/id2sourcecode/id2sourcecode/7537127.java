    public void storePatch(Patch p, int bankNum, int patchNum) {
        sendPatchWorker(p);
        send(SYSEX_WRITE_EDIT_BUFFER.toSysexMessage(getChannel(), patchNum));
    }
