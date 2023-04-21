    public void storePatch(Patch p, int bankNum, int patchNum) {
        setBankNum(bankNum);
        setPatchNum(patchNum);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        p.sysex[2] = (byte) (0x30 + getChannel() - 1);
        p.sysex[05] = (byte) bankNum;
        try {
            send(p.sysex);
        } catch (Exception e) {
            Logger.reportStatus(e);
        }
    }
