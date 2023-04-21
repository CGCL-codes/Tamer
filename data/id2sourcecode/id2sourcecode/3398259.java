    public void storePatch(Patch p, int bankNum, int patchNum) {
        int patchValue = patchNum;
        int bankValue = 0;
        if (bankNum == 1 || bankNum == 3) {
            patchValue += 64;
        }
        if (bankNum > 1) {
            bankValue = 1;
        }
        setBankNum(bankValue);
        setPatchNum(patchValue);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        ((Patch) p).sysex[2] = (byte) (0x30 + getChannel() - 1);
        try {
            send(((Patch) p).sysex);
        } catch (Exception e) {
            ErrorMsg.reportStatus(e);
        }
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        byte[] sysex = new byte[8];
        sysex[0] = (byte) 0xF0;
        sysex[1] = (byte) 0x42;
        sysex[2] = (byte) (0x30 + getChannel() - 1);
        sysex[3] = (byte) 0x51;
        sysex[4] = (byte) 0x11;
        sysex[5] = (byte) (bankValue);
        sysex[6] = (byte) (patchValue);
        sysex[7] = (byte) 0xF7;
        try {
            send(sysex);
        } catch (Exception e) {
            ErrorMsg.reportStatus(e);
        }
    }
