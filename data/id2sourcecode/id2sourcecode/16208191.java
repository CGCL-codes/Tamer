    public void storePatch(Patch p, int bankNum, int patchNum) {
        calculateChecksum(p);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        p.sysex[2] = (byte) (0x30 + getChannel() - 1);
        try {
            send(p.sysex);
        } catch (Exception e) {
            Logger.reportStatus(e);
        }
    }
