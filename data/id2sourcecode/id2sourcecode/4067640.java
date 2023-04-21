    public void storePatch(Patch p, int bankNum, int patchNum) {
        ((Patch) p).sysex[2] = (byte) (0x30 + getChannel() - 1);
        ((Patch) p).sysex[5] = (byte) bankNum;
        sendPatchWorker(p);
    }
