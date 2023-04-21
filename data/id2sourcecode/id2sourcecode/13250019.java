    public void storePatch(Patch p, int bankNum, int patchNum) {
        setBankNum(bankNum);
        setPatchNum(patchNum);
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
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        byte[] sysex = new byte[8];
        sysex[0] = (byte) 0xF0;
        sysex[1] = (byte) 0x42;
        sysex[2] = (byte) (0x30 + getChannel() - 1);
        sysex[3] = (byte) 0x28;
        sysex[4] = (byte) 0x1A;
        sysex[5] = (byte) (bankNum);
        sysex[6] = (byte) (patchNum);
        sysex[7] = (byte) 0xF7;
        try {
            send(sysex);
        } catch (Exception e) {
            Logger.reportStatus(e);
        }
    }
