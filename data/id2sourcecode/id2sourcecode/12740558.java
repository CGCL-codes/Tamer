    public void storePatch(final Patch p, final int bankNum, final int patchNum) {
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
